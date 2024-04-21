package com.remitly.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitly.model.RolePolicy;

import javax.management.relation.Role;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JSONParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JSONParser() {}

    public static RolePolicy parseDocument(String json) throws IOException {
        return objectMapper.readValue(json, new TypeReference<RolePolicy>() {});
    }

    public static RolePolicy parseDocumentFromPath(String filepath) throws IOException {
        return objectMapper.readValue(new File(filepath), new TypeReference<RolePolicy>() {});
    }

    public static ArrayList<RolePolicy> parseDocuments(String json) throws IOException {
        return objectMapper.readValue(json, new TypeReference<ArrayList<RolePolicy>>() {});
    }

    public static ArrayList<RolePolicy> parseDocumentsFromPath(String filepath) throws IOException {
        return objectMapper.readValue(new File(filepath), new TypeReference<ArrayList<RolePolicy>>() {});
    }

}
