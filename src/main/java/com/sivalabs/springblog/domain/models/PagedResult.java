package com.sivalabs.springblog.domain.models;

import java.util.List;
import java.util.function.Function;

public record PagedResult<T>(
        List<T> data,
        long totalElements,
        int pageNumber,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious) {

    public static <T> PagedResult<T> empty() {
        return new PagedResult<>(List.of(), 0, 1, 0, false, false, false, false);
    }

    public static <T> PagedResult<T> of(List<T> data, int pageNo, int pageSize, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        boolean isFirst = pageNo == 1;
        boolean isLast = pageNo == totalPages || totalPages == 0;
        boolean hasNext = pageNo < totalPages;
        boolean hasPrevious = pageNo > 1;

        return new PagedResult<>(data, totalElements, pageNo, totalPages, isFirst, isLast, hasNext, hasPrevious);
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
