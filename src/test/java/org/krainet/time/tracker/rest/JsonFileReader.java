package org.krainet.time.tracker.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

@Component
class JsonFileReader {
    public String readJsonFromFile(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(ResourceUtils.getFile("classpath:" + filePath), Object.class);
            return mapper.writeValueAsString(json);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }
}