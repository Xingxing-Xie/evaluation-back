package com.ist.recordevalution.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexSystem {
    public static IndexTreeNode root;

    static {
        try {
            Resource resource = new ClassPathResource("index.json");
            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readValue(resource.getInputStream(), IndexTreeNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void travarse(IndexTreeNode node, Visitor visitor) {
        if (CollectionUtils.isEmpty(node.getChildren())) {
            visitor.accept(node);
        }
        else {
            for (IndexTreeNode child: node.getChildren()) {
                travarse(child, visitor);
            }
            if (node != root) {
                visitor.accept(node);
            }
        }
    }
    public static Map<String, IndexTreeNode> indexTreeNodeMap() {
        Map<String, IndexTreeNode> res = new HashMap<>();
        travarse(root, (node)->{
            res.put(node.getId(), node);
        });
        return res;
    }


    public static List<String> findOneLayerIndex(Integer layer) {
        List<String> oneLayerIndex = new ArrayList<>();
        Map<String, IndexTreeNode> indexNodeMap = indexTreeNodeMap();
        for (IndexTreeNode node : indexNodeMap.values()) {
            if (node.getLayer() == layer) {
                oneLayerIndex.add(node.getId());
            }
        }
        return oneLayerIndex;
    }

    @Data
    public static class IndexTreeNode {
        private String id;

        private Integer layer;

        private List<IndexTreeNode> children = new ArrayList<>();
    }

    public interface Visitor {
        void accept(IndexTreeNode node);
    }
}
