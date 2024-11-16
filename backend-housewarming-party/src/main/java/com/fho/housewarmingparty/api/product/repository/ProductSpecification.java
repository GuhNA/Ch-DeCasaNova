package com.fho.housewarmingparty.api.product.repository;

import com.fho.housewarmingparty.api.product.dto.ProductCriteria;
import com.fho.housewarmingparty.api.product.entity.Product;
import com.fho.housewarmingparty.security.authentication.LoggedUser;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductSpecification implements Specification<Product> {

    private ProductCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.equal(root.get("user").get("id"), LoggedUser.getLoggedInUserId()));

        if (criteria.getStatus() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("status"), criteria.getStatus()));
        }

        if (criteria.getFilter() != null && !criteria.getFilter().isEmpty()) {
            String likePattern = "%" + criteria.getFilter().toLowerCase() + "%";
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
                    ));
        }

        return predicate;
    }
}
