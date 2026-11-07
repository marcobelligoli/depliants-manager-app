# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 3.5 (Java 17) server-rendered web app for cataloguing depliants (brochures) collected at events/fairs.
Thymeleaf UI, Spring Security form login, JPA + PostgreSQL, Liquibase migrations, Lombok + MapStruct. Each user may only
see/modify their own depliants.

## Commands

Use the Maven wrapper (`./mvnw` / `mvnw.cmd`) or `mvn`.

- Build + all tests (what CI runs on push to `develop`): `mvn --batch-mode clean verify`
- Unit tests only (surefire, `*Test` / `*Tests`): `mvn test`
- Single unit test: `mvn test -Dtest=DepliantServiceImplTest` (or `-Dtest=DepliantServiceImplTest#methodName`)
- Integration tests (failsafe, `*IT`): `mvn verify`; single
  IT: `mvn verify -Dit.test=DepliantControllerIT -Dtest=skip -Dsurefire.failIfNoSpecifiedTests=false`
- Package without tests: `mvn clean package -DskipTests -DskipITs` (produces `target/depliants-manager-app.jar`)
- Run locally: `mvn spring-boot:run` → http://localhost:8080
- Full stack in Docker: `docker compose -f src/main/docker/depliants-manager-app/docker-compose.yml up --build -d`

Local DB: `application.properties` points to `localhost:5445/dmadb` (user/pass `dma`), which is the port exposed by the
compose `database` service — start just that service to run the app from the IDE. The compose file
reads `src/main/docker/depliants-manager-app/.env` (incl. `PROJECT_ROOT` as build context and `SPRING_DATASOURCE_*`
overrides).

## Testing notes

- Spring context tests (`*IT` and `DepliantsManagerAppApplicationTests`) need Docker: they
  use `@ExtendWith(ITContainersExtension.class)` + `@DynamicPropertySource` delegating
  to `ITContainersExtension.configureProperties`, which starts a single shared `postgres:18-alpine` Testcontainer per
  JVM. Without Docker they are skipped (`@Testcontainers(disabledWithoutDocker = true)`), not failed.
- Liquibase builds the schema in the container; fixture data comes from `src/test/resources/sql/data.sql` via `@Sql`.
  Controller ITs authenticate with `@WithUserDetails("test1")` / `("test2")`, users defined in that script — test
  assertions depend on its IDs/ownership.
- Service tests (`*ServiceImplTest`) are plain Mockito unit tests.

## Architecture

Package root: `org.belligolifoundation.depliantsmanager`. Layering: `controller` → `service` (
interface + `*Impl`) → `repository` (Spring Data JPA) → `model` entities, with `model/dto` used across the
controller/service boundary.

Key cross-cutting points:

- **Per-user authorization** is enforced in two ways:
    - Listing filters by the logged-in user via JPA `Specification`s (`model/spec/DepliantSpecification`: `withUserId` +
      free-text `search` over description/eventName/notes/language).
    - Single-item operations (get/update/delete) use `@PreAuthorize("@depliantAclServiceImpl.canAccess(#id)")`
      on `DepliantServiceImpl`; `DepliantAclServiceImpl` checks `existsByIdAndUserId` against the `DMAUserDetails`
      principal. New per-depliant service methods must carry the same annotation.
- **Principal**: `security/DMAUserDetails` exposes `getUserId()`; controllers obtain it
  with `@AuthenticationPrincipal DMAUserDetails` and set `userId` on the DTO server-side (never trust the form).
- **Mapping**: MapStruct mappers are used via the static `INSTANCE` (not Spring-injected). `DepliantMapper`
  maps `user.id` ↔ `userId` and builds a stub `User` for the entity side. Lombok/MapStruct ordering is configured
  in `pom.xml` annotation processor paths.
- **HTTP method override**: `spring.mvc.hiddenmethod.filter.enabled=true`; Thymeleaf forms POST with a
  hidden `_method` (`put`/`delete`) to hit `@PutMapping`/`@DeleteMapping` endpoints.
- **CSRF is enabled** (Spring Security default). Forms must use `th:action` so Thymeleaf injects the `_csrf` token;
  MockMvc tests for POST/PUT/DELETE need `.with(csrf())`.
- **Never bind client-supplied ids on create**: mappers' `toEntity`
  ignore `id` (`@Mapping(target = "id", ignore = true)`), otherwise `JpaRepository.save` merges into an existing row (
  this was an account-takeover / ownership-transfer bug). `Depliant.user` is `updatable = false`.
- ITs that insert rows should be `@Transactional` (rolled back): `data.sql` resets the id sequences before every test,
  so persisted inserts collide on ids.
- **Security routes**: `/users/register`, `/users/login`, and static `/css`, `/js`, `/img` are public; login is
  processed at `/login`, success redirects to `/depliants`.
- **Controller error handling**: form submissions catch exceptions and re-render `depliants/form` with an `errorData`
  model attribute (`DataIntegrityViolationException` → generic message).
- **Schema changes**: add a new changelog under `src/main/resources/db/changelog/` and include it in `master.yaml`; do
  not edit already-applied changesets.

## Workflow

Work happens on `develop`; `main` is the release branch. Versions are bumped in `pom.xml` (`x.y.z` release commit,
then `x.(y+1).0-SNAPSHOT`).

**Dependabot** (`.github/dependabot.yml`) opens weekly PRs against `develop` for Maven, GitHub Actions (pinned SHA
and version comment updated together), the Dockerfile base images and the docker-compose images.
`dependabot-auto-merge.yml` enables squash auto-merge only for `semver-patch` updates; GitHub merges them once the
`Maven Compile and Verify` check passes, which `develop`'s branch protection requires (admins can still push
directly). That is why `build-develop.yml` also runs on PRs into `develop`, without `paths-ignore`. Minor and major
updates stay open for review. CI does not build the Docker image, so a Dockerfile/compose patch bump is merged
without being exercised.
