package com.ist.recordevalution.controller;

import com.ist.recordevalution.common.Result;
import com.ist.recordevalution.service.ScoreService;
import com.ist.recordevalution.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class ScoreController {
    @Autowired
    ScoreService scoreService;

    @GetMapping("/score")
    public Result<Map<String, Double>> getScore() throws IOException {
        return ResultUtil.success(scoreService.computeScore());
    }
}
