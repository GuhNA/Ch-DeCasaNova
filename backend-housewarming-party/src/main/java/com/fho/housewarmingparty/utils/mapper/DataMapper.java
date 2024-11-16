package com.fho.housewarmingparty.utils.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public interface DataMapper<E, D> {

    E toEntity(D dto);

    default List<E> toEntity(Collection<D> dtos) {
        return mapFromCollection(dtos, this::toEntity);
    }

    D toDto(E entity);

    default List<D> toDto(List<E> entities) {
        return mapFromCollection(entities, this::toDto);
    }

    default Page<D> toDto(Page<E> page) {
        if (page == null) {
            return Page.empty();
        }

        List<D> dtoList = mapFromCollection(page.getContent(), this::toDto);
        return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
    }

    SimpleDTO toSimpleDto(E entity);

    default List<SimpleDTO> toSimpleDtoList(List<E> entities) {
        return DataMapper.mapFromCollection(entities, this::toSimpleDto);
    }

    static <E, R> List<R> mapFromCollection(Collection<E> elements, Function<E, R> mapper) {
        return elements != null
                ? elements.stream().map(mapper).toList()
                : Collections.emptyList();
    }
}
