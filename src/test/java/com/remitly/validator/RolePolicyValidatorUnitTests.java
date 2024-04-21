package com.remitly.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitly.exception.RolePolicyValidationException;
import com.remitly.model.RolePolicy;
import com.remitly.model.Statement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class RolePolicyValidatorUnitTests {

    private final String properJson = """
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

    private final String improperJson = """
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

    private final String missingResourcesJson = """
            {
              "PolicyDocument": {
                "Version": "2021-10-17",
                "Statement": [
                  {
                    "Sid": "IamListAccess",
                    "Effect": "Boom",
                    "Principal": {"principal":  "prrrrrrra"},
                    "Action": []
                  }
                ]
              }
            }""";

    private final ObjectMapper objectMapper;

    public RolePolicyValidatorUnitTests() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void successfulRolePolicyCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = RolePolicyValidator.rolePolicyCheck(rolePolicy);

        // Then
        assertTrue(result);
    }

    @Test
    public void failingRolePolicyCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = null;

        // When
        Executable action = () -> RolePolicyValidator.rolePolicyCheck(rolePolicy);

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }

    @Test
    public void successfulPolicyNameCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = RolePolicyValidator.policyNameCheck(rolePolicy.policyName());

        // Then
        assertTrue(result);
    }

    @Test
    public void failingPolicyNameCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperJson, new TypeReference<RolePolicy>() {});

        // When
        Executable action = () -> RolePolicyValidator.policyNameCheck(rolePolicy.policyName());

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }

    @Test
    public void successfulPolicyVersionCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = RolePolicyValidator.policyVersionCheck(rolePolicy.policyDocument().version());

        // Then
        assertTrue(result);
    }

    @Test
    public void failingPolicyVersionCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperJson, new TypeReference<RolePolicy>() {});

        // When
        Executable action = () -> RolePolicyValidator.policyVersionCheck(rolePolicy.policyDocument().version());

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }

    @Test
    public void successfulStatementEffectCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = true;
        for (Statement statement : rolePolicy.policyDocument().statement()) {
            if (!RolePolicyValidator.statementEffectCheck(statement.effect())) {
                result = false;
                break;
            }
        }

        // Then
        assertTrue(result);
    }

    @Test
    public void failingStatementEffectCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperJson, new TypeReference<RolePolicy>() {});

        // When
        Executable action = () -> {
            boolean result = true;
            for (Statement statement : rolePolicy.policyDocument().statement()) {
                if (!RolePolicyValidator.statementEffectCheck(statement.effect())) {
                    result = false;
                    break;
                }
            }
        };

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }

    @Test
    public void successfulStatementPrincipalCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = true;
        for (Statement statement : rolePolicy.policyDocument().statement()) {
            if (!RolePolicyValidator.statementPrincipalCheck(statement.principal())) {
                result = false;
                break;
            }
        }

        // Then
        assertTrue(result);
    }

    @Test
    public void failingStatementPrincipalCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperJson, new TypeReference<RolePolicy>() {});

        // When
        Executable action = () -> {
            boolean result = true;
            for (Statement statement : rolePolicy.policyDocument().statement()) {
                if (!RolePolicyValidator.statementPrincipalCheck(statement.principal())) {
                    result = false;
                    break;
                }
            }
        };

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }

    @Test
    public void successfulStatementActionCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = true;
        for (Statement statement : rolePolicy.policyDocument().statement()) {
            if (!RolePolicyValidator.statementActionCheck(statement.action())) {
                result = false;
                break;
            }
        }

        // Then
        assertTrue(result);
    }

    @Test
    public void failingStatementActionCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperJson, new TypeReference<RolePolicy>() {});

        // When
        Executable action = () -> {
            boolean result = true;
            for (Statement statement : rolePolicy.policyDocument().statement()) {
                if (!RolePolicyValidator.statementActionCheck(statement.action())) {
                    result = false;
                    break;
                }
            }
        };

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }

    @Test
    public void successfulStatementResourceCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(properJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = true;
        for (Statement statement : rolePolicy.policyDocument().statement()) {
            if (!RolePolicyValidator.statementResourceCheck(statement.resource())) {
                result = false;
                break;
            }
        }

        // Then
        assertTrue(result);
    }

    @Test
    public void missingStatementResourceCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(missingResourcesJson, new TypeReference<RolePolicy>() {});

        // When
        Executable action = () -> {
            boolean result = true;
            for (Statement statement : rolePolicy.policyDocument().statement()) {
                if (!RolePolicyValidator.statementResourceCheck(statement.resource())) {
                    result = false;
                    break;
                }
            }
        };

        // Then
        assertThrows(RolePolicyValidationException.class, action);
    }

    @Test
    public void failingStatementResourceCheckTest() throws JsonProcessingException {
        // Given
        RolePolicy rolePolicy = objectMapper.readValue(improperJson, new TypeReference<RolePolicy>() {});

        // When
        boolean result = true;
        for (Statement statement : rolePolicy.policyDocument().statement()) {
            if (!RolePolicyValidator.statementResourceCheck(statement.resource())) {
                result = false;
                break;
            }
        }

        // Then
        assertFalse(result);
    }
}
