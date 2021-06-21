package com.ist.recordevalution.repo;

import com.ist.recordevalution.model.IndexSurvey;
import org.springframework.stereotype.Repository;

@Repository
public class IndexSurveyRepo extends FileBasedRepo<IndexSurvey> {
    static final private String INDEX_SURVEY_FILE = "index_survey.repo";

    @Override
    protected String getFileName() {
        return INDEX_SURVEY_FILE;
    }

    @Override
    protected Class<IndexSurvey> getClazz() {
        return IndexSurvey.class;
    }
}
