package org.belligolifoundation.depliantsmanager.model.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.belligolifoundation.depliantsmanager.model.Depliant;
import org.springframework.data.jpa.domain.Specification;

public class DepliantSpecification {

    private DepliantSpecification() {
        throw new IllegalStateException("Specification class");
    }

    public static Specification<Depliant> withUserId(Long userId) {
        return (Root<Depliant> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (userId == null) {
                throw new IllegalStateException("Param userId cannot be null!");
            }
            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Depliant> search(String searchString) {
        return (Root<Depliant> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            if (searchString == null || searchString.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + searchString.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("description")), pattern),
                    cb.like(cb.lower(root.get("eventName")), pattern),
                    cb.like(cb.lower(root.get("notes")), pattern),
                    cb.like(cb.lower(root.get("language")), pattern)
            );
        };
    }
}
