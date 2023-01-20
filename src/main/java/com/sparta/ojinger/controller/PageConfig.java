package com.sparta.ojinger.controller;

import com.sparta.ojinger.exception.CustomException;
import com.sparta.ojinger.exception.ErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageConfig {

    //페이징
    public static Pageable pageableSetting(int pageChoice) {
        if (pageChoice < 1) {
            throw new CustomException(ErrorCode.PAGINATION_IS_NOT_EXIST);
        }

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "id");
        return PageRequest.of(pageChoice - 1, 10, sort);
    }
}
