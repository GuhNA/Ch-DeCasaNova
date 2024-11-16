package com.fho.housewarmingparty.api.product.repository;

import java.math.BigDecimal;

import com.fho.housewarmingparty.api.product.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public BigDecimal countTotalAmount(Specification<Product> specification) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<Product> root = query.from(Product.class);

        // Ajuste para converter a soma para BigDecimal explicitamente
        query.select(cb.coalesce(cb.sum(root.get("price")), BigDecimal.ZERO));

        // Aplica os filtros do Specification
        Predicate predicate = specification.toPredicate(root, query, cb);
        if (predicate != null) {
            query.where(predicate);
        }

        // Executa a consulta
        return entityManager.createQuery(query).getSingleResult();
    }
}
