package com.fho.housewarmingparty.api.image.repository;

import com.fho.housewarmingparty.api.image.dto.ImageCriteria;
import com.fho.housewarmingparty.api.image.entity.Image;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ImageSpecification implements Specification<Image> {

    private ImageCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Image> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (criteria.getType() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get("type"), criteria.getType()));
        }

        return predicate;
    }
}
