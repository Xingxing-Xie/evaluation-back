package com.ist.recordevalution.controller;

import com.ist.recordevalution.common.Result;
import com.ist.recordevalution.dto.SurveyCountDto;
import com.ist.recordevalution.dto.SurveyDto;
import com.ist.recordevalution.service.SurveyService;
import com.ist.recordevalution.service.WeightService;
import com.ist.recordevalution.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SurveyController {
    @Autowired
    SurveyService surveyService;

    @Autowired
    WeightService weightService;

    @PostMapping("/index/survey")
    public Result SubmitIndexSurvey(@RequestBody SurveyDto surveyDto) throws IOException {
        surveyService.saveSurvey(true, surveyDto);
        weightService.computeWeightAndSave();
        return ResultUtil.success();
    }

    @PostMapping("/survey")
    public Result submitProcessSurvey(@RequestBody SurveyDto surveyDto) throws IOException {
        surveyService.saveSurvey(false, surveyDto);

        return ResultUtil.success();
    }

    @GetMapping("/survey")
    public Result<SurveyCountDto> countSurvey() throws IOException {
        return ResultUtil.success(surveyService.count());
    }
}
