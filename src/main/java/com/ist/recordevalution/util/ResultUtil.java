package com.ist.recordevalution.util;

import com.ist.recordevalution.common.Result;

public class ResultUtil {

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setData(data);
        result.setCode(0);
        result.setMessage("success");
        return result;
    }

    public static Result fail(Integer code) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage("failed");
        return result;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(0);
        result.setMessage("success");
        return result;
    }
}
