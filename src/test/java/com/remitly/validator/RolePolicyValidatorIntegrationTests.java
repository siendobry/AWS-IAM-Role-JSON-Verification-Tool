package com.remitly.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitly.exception.RolePolicyValidationException;
import com.remitly.model.RolePolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class RolePolicyValidatorIntegrationTests {

    private final String properRobustJson = """
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

    private final String properNonRobustJson = """
            {
              "PolicyDocument": {
                "Statement": [
                  {
                    "Resource": "test:resource"
                  }
                ]
              }
            }""";

    private final String improperRobustJson = """
            {
              "PolicyDocument": {
                "Version": "2021-10-17",
                "Statement": [
                  {
                    "Sid": "IamListAccess",
                    "Effect": "Boom",
                    "Principal": {"principal":  "prrrrrrra"},
                    "Action": [],
                    "Resource": [
                      "two",
                      "*",
                      "one"
                    ]
                  }
                ]
              }
            }""";

    private final String improperNonRobustJson = """
            {
              "PolicyDocument": {
                "Statement": [
                  {
                    "Resource": "*"
                  }
                ]
              }
            }""";

    private final ObjectMapper objectMapper;

    public RolePolicyValidatorIntegrationTests() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void successfulRobustValidationTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properRobustJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = RolePolicyValidator.validate(rolePolicy, true);

        // Then
        assertTrue(result);
    }

    @Test
    public void failingRobustValidationTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperRobustJson, new TypeReference<RolePolicy>() {});;

        // When
        Executable action = () -> RolePolicyValidator.validate(rolePolicy, true);

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }

    @Test
    public void successfulNonRobustValidationTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properNonRobustJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = RolePolicyValidator.validate(rolePolicy, false);

        // Then
        assertTrue(result);
    }

    @Test
    public void failingNonRobustValidationTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperNonRobustJson, new TypeReference<RolePolicy>() {});;

        // When
        boolean result =  RolePolicyValidator.validate(rolePolicy, false);

        // Then
        assertFalse(result);
    }

    @Test
    public void successfulPolicyDocumentCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properRobustJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = RolePolicyValidator.policyDocumentCheck(rolePolicy.policyDocument());

        // Then
        assertTrue(result);
    }

    @Test
    public void failingPolicyDocumentCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperRobustJson, new TypeReference<RolePolicy>() {});;

        // When
        Executable action = () -> RolePolicyValidator.policyDocumentCheck(rolePolicy.policyDocument());

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }

    @Test
    public void successfulPolicyStatementCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properRobustJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = RolePolicyValidator.policyStatementCheck(rolePolicy.policyDocument().statement());

        // Then
        assertTrue(result);
    }

    @Test
    public void failingPolicyStatementCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperRobustJson, new TypeReference<RolePolicy>() {});;

        // When
        Executable action = () -> RolePolicyValidator.policyStatementCheck(rolePolicy.policyDocument().statement());

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }
}
