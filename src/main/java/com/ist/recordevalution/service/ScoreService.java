package com.ist.recordevalution.service;

import com.ist.recordevalution.model.IndexSystem;
import com.ist.recordevalution.model.ProcessSurvey;
import com.ist.recordevalution.model.Weight;
import com.ist.recordevalution.repo.ProcessSurveyRepo;
import com.ist.recordevalution.repo.WeightRepo;
import com.ist.recordevalution.util.SurveyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ScoreService {
    @Autowired
    ProcessSurveyRepo processSurveyRepo;

    @Autowired
    WeightRepo weightRepo;

    public Map<String, Double> computeScore() throws IOException {
        List<ProcessSurvey> surveys = processSurveyRepo.findAll();
        Weight weight = weightRepo.findOne();
        if (surveys.isEmpty() || weight == null) {
            Map<String, Double> res = new HashMap<>();
            IndexSystem.travarse(IndexSystem.root, (node) -> {
                res.put(node.getId(), 0.0);
            });
            return res;
        } else {
            Map<String, Double> weights = weightRepo.findOne().getWeightMap();
            return compute(weights);
        }
    }

    private Map<String, Double> compute(Map<String, Double> weights) throws IOException {
        // 计算每个指标的综合评估结果向量（三级指标关于评价等级的隶属度）
        Map<String, List<Double>> membershipMap = computeMembership(weights);
        // 每个评价等级的分数，评分集
        List<Integer> levelScore = Arrays.asList(100, 90, 75, 60, 50);
        // 每个指标的分数=该指标的综合评估结果向量*评分集，并四舍五入
        Map<String, Double> res = new HashMap<>();
        for (String index : membershipMap.keySet()) {
            List<Double> membership = membershipMap.get(index);
            double score = 0;
            for (int i = 0; i < 5; i++) {
                score += membership.get(i) * levelScore.get(i);
            }
            BigDecimal bg = new BigDecimal(score);
            double format = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            res.put(index, format);
        }
        return res;
    }

    private Map<String, List<Double>> computeMembership(Map<String, Double> weights) throws IOException {
        // 初始化，存放各指标的综合评估结果向量
        Map<String, List<Double>> membershipMap = new HashMap<>();

        // 预处理所有评价问卷结果中每个三级指标对应的结果
        List<ProcessSurvey> processSurveys = processSurveyRepo.findAll();
        List<Map<String, Integer>> processSurveyResult = SurveyUtil.getProcessSurveyResult(processSurveys);
        Map<String, List<Integer>> resultsMap = SurveyUtil.getStringListMap(processSurveyResult);

        // 每个三级指标关于每个评价等级对应的模糊子集的隶属度
        for (String index : resultsMap.keySet()) {
            List<Integer> results = resultsMap.get(index);
            List<Integer> sum = Arrays.asList(0, 0, 0, 0, 0);
            for (int assess : results) {
                sum.set(assess - 1, sum.get(assess - 1) + 1);
            }
            List<Double> membership = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                membership.add((sum.get(i) / (double) results.size()));
            }
            membershipMap.put(index, membership);
        }

        // 建立模糊综合评价矩阵，由三级指标隶属度依次递推出二级、一级指标以及最终的综合评估结果向量
        List<String> secondLayerIndexList = IndexSystem.findOneLayerIndex(2);
        groupChildren(secondLayerIndexList, membershipMap, weights);

        List<String> firstLayerIndexList = IndexSystem.findOneLayerIndex(1);
        groupChildren(firstLayerIndexList, membershipMap, weights);

        List<String> topLayerIndexList = IndexSystem.findOneLayerIndex(0);
        groupChildren(topLayerIndexList, membershipMap, weights);

        return membershipMap;
    }

    private void groupChildren(List<String> layerIndexList,
                               Map<String, List<Double>> membershipMap,
                               Map<String, Double> weights) {
        // 遍历这一层的指标
        for (int i = 0; i < layerIndexList.size(); i++) {
            // 通过指标体系的树结构找到该指标的子节点
            String index = layerIndexList.get(i);
            Map<String, IndexSystem.IndexTreeNode> indexTreeNodeMap = IndexSystem.indexTreeNodeMap();
            List<IndexSystem.IndexTreeNode> children = indexTreeNodeMap.get(index).getChildren();

            // 该指标的综合评估结果向量=子节点的权重集*子节点的隶属度（综合评估结果向量）
            List<Double> thisMembership = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                double membership = 0;
                for (IndexSystem.IndexTreeNode child : children) {
                    String childId = child.getId();
                    membership += weights.get(childId) * membershipMap.get(childId).get(j);
                }
                thisMembership.add(membership);
            }
            membershipMap.put(index, thisMembership);
        }
    }
}
