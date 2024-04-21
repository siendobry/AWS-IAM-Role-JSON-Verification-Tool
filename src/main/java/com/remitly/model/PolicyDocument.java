package com.remitly.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public record PolicyDocument(
        @JsonProperty("Version")
        String version,

        @JsonProperty("Statement")
        ArrayList<Statement> statement
) {
}
