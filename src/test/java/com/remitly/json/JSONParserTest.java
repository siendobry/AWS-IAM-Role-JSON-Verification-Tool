package com.remitly.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitly.model.RolePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONParserTest {

    private final String rolePolicyJson = """
            {
              "PolicyName": "root",
              "PolicyDocument": {
                "Version": "2012-10-17",
                "Statement": [
                  {
                    "Sid": "IamListAccess",
                    "Effect": "Allow",
                    "Action": [
                      "iam:ListRoles",
                      "iam:ListUsers"
                    ],
                    "Resource": "test:resource"
                  }
                ]
              }
            }""";

    private final String rolePolicyJsonFilepath = "./src/main/resources/document.json";
    private final String rolePolicyJsonListFilepath = "./src/main/resources/documents.json";

    private final ObjectMapper objectMapper;

    public JSONParserTest() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testParseDocument() throws IOException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(rolePolicyJson, new TypeReference<RolePolicy>() {});

        // When
        RolePolicy parsedRolePolicyJson = JSONParser.parseDocument(rolePolicyJson);

        // Then
        assertEquals(rolePolicy, parsedRolePolicyJson);
    }

    @Test
    public void testParseDocumentFromPath() throws IOException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(rolePolicyJson, new TypeReference<RolePolicy>() {});

        // When
        RolePolicy parsedRolePolicyJson = JSONParser.parseDocumentFromPath(rolePolicyJsonFilepath);

        // Then
        assertEquals(rolePolicy, parsedRolePolicyJson);
    }

    @Test
    public void testParseDocuments() throws IOException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(rolePolicyJson, new TypeReference<RolePolicy>() {});
        ArrayList<RolePolicy> rolePolicyList = new ArrayList<>();
        rolePolicyList.add(rolePolicy);

        // When
        ArrayList<RolePolicy> parsedRolePolicyJsonList = JSONParser.parseDocuments("[" + rolePolicyJson + "]");

        // Then
        assertEquals(rolePolicyList.get(0), parsedRolePolicyJsonList.get(0));
    }

    @Test
    public void testParseDocumentsFromPath() throws IOException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(rolePolicyJson, new TypeReference<RolePolicy>() {});
        ArrayList<RolePolicy> rolePolicyList = new ArrayList<>();
        rolePolicyList.add(rolePolicy);

        // When
        ArrayList<RolePolicy> parsedRolePolicyJsonList = JSONParser.parseDocumentsFromPath(rolePolicyJsonListFilepath);

        // Then
        assertEquals(rolePolicyList.get(0), parsedRolePolicyJsonList.get(0));
    }
}
