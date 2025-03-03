package com.sivalabs.springblog.domain.models;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public record PagedResult<T>(
        List<T> data,
        long totalElements,
        int pageNumber,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious) {

    public static <T> PagedResult<T> from(Page<T> page) {
        return new PagedResult<>(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious());
    }

    public <R> PagedResult<R> map(Function<T, R> converter) {
        return new PagedResult<>(
                this.data.stream().map(converter).toList(),
                this.totalElements,
                this.pageNumber,
                this.totalPages,
                this.isFirst,
                this.isLast,
                this.hasNext,
                this.hasPrevious);
    }
}
