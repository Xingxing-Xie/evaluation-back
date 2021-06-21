package com.ist.recordevalution.repo;

import com.ist.recordevalution.model.Weight;
import org.springframework.stereotype.Repository;

@Repository
public class WeightRepo extends FileBasedRepo<Weight> {
    static final private String WEIGHT_FILE = "weight.repo";

    @Override
    protected String getFileName() {
        return WEIGHT_FILE;
    }

    @Override
    protected Class<Weight> getClazz() {
        return Weight.class;
    }
}
