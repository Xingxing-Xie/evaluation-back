package com.ist.recordevalution.repo;

import com.ist.recordevalution.model.ProcessSurvey;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessSurveyRepo extends FileBasedRepo<ProcessSurvey> {
    static final private String EVALUATE_SURVEY_FILE = "evaluate_survey.repo";

    @Override
    protected String getFileName() {
        return EVALUATE_SURVEY_FILE;
    }

    @Override
    protected Class<ProcessSurvey> getClazz() {
        return ProcessSurvey.class;
    }
}
