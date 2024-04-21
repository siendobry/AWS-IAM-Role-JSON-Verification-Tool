package com.remitly.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RolePolicy(
        @JsonProperty("PolicyName")
        String policyName,

        @JsonProperty("PolicyDocument")
        PolicyDocument policyDocument
) {
}
