package com.ist.recordevalution.util;

import com.ist.recordevalution.model.IndexSurvey;
import com.ist.recordevalution.model.ProcessSurvey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyUtil {
    public static List<Map<String, Integer>> getProcessSurveyResult(List<ProcessSurvey> surveys) {
        List<Map<String, Integer>> processSurveyResults = new ArrayList<>();
        for(ProcessSurvey survey : surveys) {
            processSurveyResults.add(survey.getResult());
        }
        return processSurveyResults;
    }

    public static List<Map<String, Integer>> getIndexSurveyResult(List<IndexSurvey> surveys) {
        List<Map<String, Integer>> indexSurveyResults = new ArrayList<>();
        for(IndexSurvey indexSurvey : surveys) {
            indexSurveyResults.add(indexSurvey.getResult());
        }
        return indexSurveyResults;
    }

    public static Map<String, List<Integer>> getStringListMap(List<Map<String, Integer>> surveyResults) {
        Map<String, List<Integer>> resultsMap = new HashMap<>();
        for(Map<String, Integer> surveyResult : surveyResults) {
            for (String s : surveyResult.keySet())  {
                if (!resultsMap.containsKey(s)) {
                    resultsMap.put(s, new ArrayList<>());
                }
                resultsMap.get(s).add(surveyResult.get(s));
            }
        }
        return resultsMap;
    }
}
