package com.pragma.foodcourtservice.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class PageModel<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

}
