package com.ist.recordevalution.common;

import lombok.Data;

@Data
public class Result<T> {
    T data;
    String message;
    Integer code;
}
