package com.ist.recordevalution.util;

import java.util.*;

public class WeightUtil {
    public static Map<String, Double> ahpWeight(Map<String, Integer> map) {
        Map<String, Double> weightMap = new HashMap<>();

        // 判断矩阵
        List<List<Double>> matrix = new ArrayList<>();
        List<String> indexList = new ArrayList<>(map.keySet());
        for (String index : indexList) {
            List<Double> m = new ArrayList<>();
            for (String i : indexList) {
                if(index.equals(i)) {
                    m.add(1.0);
                } else {
                    if (map.get(index) <= map.get(i)) {
                        m.add((double) (2 * (map.get(i) - map.get(index)) + 1));
                    } else {
                        m.add(1.0 / (2 * (map.get(index) - map.get(i)) + 1));
                    }
                }
            }
            matrix.add(m);
        }

        // 列向量归一化
        for (int j = 0; j < matrix.get(0).size(); j++) {
            double sum = 0.0;
            for (int i = 0; i < matrix.size(); i++) {
                sum += matrix.get(i).get(j);
            }
            for (int i = 0; i < matrix.size(); i++) {
                matrix.get(i).set(j, matrix.get(i).get(j) / sum);
            }
        }

        // 行向量加权平均
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            double sum = 0.0;
            for (int j = 0; j < matrix.get(0).size(); j++) {
                sum += matrix.get(i).get(j);
            }
            weights.add(sum / matrix.size());
        }

        // 权重集
        for (int i = 0; i < weights.size(); ++i) {
            weightMap.put(indexList.get(i), weights.get(i));
        }

        return weightMap;
    }
}
