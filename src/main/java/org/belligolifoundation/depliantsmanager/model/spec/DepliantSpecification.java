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
        if (searchString != null) {
            searchString = "%" + searchString + "%";
        }

        String finalSearchString = searchString;
        return (Root<Depliant> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (finalSearchString == null) {
                return cb.conjunction();
            }

            return cb.or(
                    cb.like(root.get("description"), finalSearchString),
                    cb.like(root.get("eventName"), finalSearchString),
                    cb.like(root.get("notes"), finalSearchString),
                    cb.like(root.get("language"), finalSearchString)
            );
        };
    }
}
