package com.ist.recordevalution.service;

import com.ist.recordevalution.model.IndexSurvey;
import com.ist.recordevalution.model.IndexSystem;
import com.ist.recordevalution.model.Weight;
import com.ist.recordevalution.repo.IndexSurveyRepo;
import com.ist.recordevalution.repo.ProcessSurveyRepo;
import com.ist.recordevalution.repo.WeightRepo;
import com.ist.recordevalution.util.SurveyUtil;
import com.ist.recordevalution.util.WeightUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

@Service
public class WeightService {
    @Autowired
    private WeightRepo weightRepo;

    @Autowired
    private IndexSurveyRepo indexSurveyRepo;


    public Map<String, Double> getWeightMap() throws IOException {
        Weight weight = weightRepo.findOne();
        if (weight == null) {
            Map<String, Double> res = new HashMap<>();
            IndexSystem.travarse(IndexSystem.root, (node) -> {
                res.put(node.getId(), 0.0);
            });
            return res;
        } else {
            return weight.getWeightMap();
        }
    }

    public void computeWeightAndSave() throws IOException {
        List<IndexSurvey> indexSurveys = indexSurveyRepo.findAll();
        List<Map<String, Integer>> indexSurveyResult = SurveyUtil.getIndexSurveyResult(indexSurveys);
        saveWeight(computeWeightByAHP(indexSurveyResult));
    }

    private void saveWeight(Map<String, Double> weightMap) throws IOException {
        Weight weight = weightRepo.findOne();
        if(weight == null) {
            weight = new Weight();
        }
        weight.setWeightMap(weightMap);
        weightRepo.save(weight);
    }

    private Map<String, Double> computeWeightByAHP(List<Map<String, Integer>> surveyResults) {
        // 初始化，存放计算的权重集
        Map<String, Double> res = new HashMap<>();
        // 所有问卷的权重集
        Map<String, List<Double>> weights = new HashMap<>();
        // 将指标按层级和相关性分组
        List<List<String>> indexGroup = group(IndexSystem.root);
        // 遍历每一组指标
        for (int i = 0; i < indexGroup.size(); i++) {
            List<String> indexList = indexGroup.get(i);
            // 遍历每一份问卷结果
            for (int j = 0; j < surveyResults.size(); j++) {
                // 问卷结果按分好组的指标的分组
                Map<String, Integer> surveyResult = surveyResults.get(j);
                Map<String, Integer> groupedSurveyResult = new HashMap<>();
                for (String index: indexList) {
                    groupedSurveyResult.put(index, surveyResult.get(index));
                }
                // 由这一份问卷结果计算指标权重
                Map<String, Double> weight = WeightUtil.ahpWeight(groupedSurveyResult);
                // 将该份问卷这一组指标的权重计算结果存放至所有问卷的权重集中
                for (String in : weight.keySet()) {
                    if (weights.containsKey(in)) {
                        weights.get(in).add(weight.get(in));
                    } else {
                        weights.put(in, new ArrayList<>(Collections.singleton(weight.get(in))));
                    }
                }
            }
        }
        // 将所有问卷的权重集中每一个指标的权重进行加权平均
        for (String index : weights.keySet()) {
            double sum = 0.0;
            for (int j = 0; j < weights.get(index).size(); j++) {
                sum += weights.get(index).get(j);
            }
            res.put(index, sum / weights.get(index).size());
        }
        return res;
    }

    private List<List<String>> group(IndexSystem.IndexTreeNode node) {
        if (CollectionUtils.isEmpty(node.getChildren())) {
            return new ArrayList<>();
        } else {
            List<String> index = new ArrayList<>();
            List<List<String>> res = new ArrayList<>();
            for (IndexSystem.IndexTreeNode child : node.getChildren()) {
                index.add(child.getId());
                List<List<String>> lists = group(child);
                for (int i = 0; i < lists.size(); i++) {
                    res.add(lists.get(i));
                }
            }
            res.add(index);
            return res;
        }
    }
}
