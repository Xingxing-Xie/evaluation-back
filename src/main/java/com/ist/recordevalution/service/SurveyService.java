package com.ist.recordevalution.service;

import com.ist.recordevalution.dto.SurveyCountDto;
import com.ist.recordevalution.dto.SurveyDto;
import com.ist.recordevalution.model.IndexSurvey;
import com.ist.recordevalution.model.ProcessSurvey;
import com.ist.recordevalution.repo.IndexSurveyRepo;
import com.ist.recordevalution.repo.ProcessSurveyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SurveyService {
    @Autowired
    IndexSurveyRepo indexSurveyRepo;

    @Autowired
    ProcessSurveyRepo processSurveyRepo;

    public void saveSurvey(boolean isIndex, SurveyDto surveyDto) throws IOException {
        if (isIndex) {
            IndexSurvey indexSurvey = new IndexSurvey(surveyDto.getResult());
            indexSurveyRepo.append(indexSurvey);
        } else {
            ProcessSurvey processSurvey = new ProcessSurvey(surveyDto.getResult());
            processSurveyRepo.append(processSurvey);
        }
    }

    public SurveyCountDto count() throws IOException {
        SurveyCountDto surveyCountDto = new SurveyCountDto();
        List<ProcessSurvey> processSurveys = processSurveyRepo.findAll();
        List<IndexSurvey> indexSurveys = indexSurveyRepo.findAll();
        surveyCountDto.setProcessSurveyCount(processSurveys.size());
        surveyCountDto.setIndexSurveyCount(indexSurveys.size());
        return surveyCountDto;
    }
}
