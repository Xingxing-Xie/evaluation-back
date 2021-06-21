package com.ist.recordevalution.repo;

import com.ist.recordevalution.model.IndexSurvey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
class IndexSurveyRepoTest {
    static final private IndexSurveyRepo indexSurveyRepo = new IndexSurveyRepo();

    @Test
    void testAppend() throws IOException {
        Map<String, Integer> result = new HashMap<>();
        result.put("1", 1);
        result.put("2", 2);
        IndexSurvey indexSurvey = new IndexSurvey();
        indexSurvey.setResult(result);
        indexSurveyRepo.append(indexSurvey);
    }

    @Test
    void testFindAll() throws IOException {
        List<IndexSurvey> indexSurveys = indexSurveyRepo.findAll();
        log.info("{}", indexSurveys);
    }
}