package com.ist.recordevalution.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class FileBasedRepo<T> {
    static final private String SPLITTER = "#";
    static final private ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public void append(T object) throws IOException {
        write2File(object, true);
    }

    public void save(T object) throws IOException {
        write2File(object, false);
    }

    private void write2File(T object, boolean append) throws IOException {
        File file = new File(getFileName());
        boolean isFirstStore = false;
        if (!file.exists()) {
            file.createNewFile();
            isFirstStore = true;
        }
        FileWriter fileWriter = new FileWriter(file, append);
        if (!isFirstStore && append) {
            fileWriter.append(SPLITTER);
        }
        fileWriter.write(OBJECT_MAPPER.writeValueAsString(object));
        fileWriter.close();
    }

    public List<T> findAll() throws IOException {
        File file = new File(getFileName());
        if (!file.exists()) {
            return new ArrayList<>();
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[1024 * 1024];
        while (fileInputStream.read(buffer) != -1) {
            sb.append(new String(buffer));
        }
        fileInputStream.close();
        String[] indexSurveyStringList = sb.toString().split(SPLITTER);
        List<T> res = new ArrayList<>();
        for (String indexSurveyString: indexSurveyStringList) {
            if (StringUtils.hasText(indexSurveyString)) {
                res.add(OBJECT_MAPPER.readValue(indexSurveyString, getClazz()));
            }
        }
        return res;
    }

    public T findOne() throws IOException {
        List<T> res = findAll();
        if (res.isEmpty()) {
            return null;
        }
        else {
            return res.get(0);
        }
    }

    protected abstract String getFileName();

    protected abstract Class<T> getClazz();
}
