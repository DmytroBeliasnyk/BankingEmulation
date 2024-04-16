package org.emuba.bankingemulation.dto;

import lombok.Data;

@Data
public class PageCountDTO {
    private long pageCount;
    private int pageSize;

    private PageCountDTO(long pageCount, int pageSize) {
        this.pageCount = pageCount;
        this.pageSize = pageSize;
    }

    public static PageCountDTO of(long pageCount, int pageSize) {
        return new PageCountDTO(pageCount, pageSize);
    }
}
