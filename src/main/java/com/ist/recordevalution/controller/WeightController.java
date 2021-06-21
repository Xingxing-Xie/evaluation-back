package com.ist.recordevalution.controller;

import com.ist.recordevalution.common.Result;
import com.ist.recordevalution.service.WeightService;
import com.ist.recordevalution.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class WeightController {
    @Autowired
    private WeightService weightService;

    @GetMapping("/weight")
    public Result<Map<String, Double>> getWeight() throws IOException {
        return ResultUtil.success(weightService.getWeightMap());
    }
}
