package com.remitly.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;

import java.util.ArrayList;

public record Statement(
        @JsonProperty("Sid")
        String sid,

        @JsonProperty("Effect")
        String effect,

        @JsonProperty("Principal")
        Object principal,

        @JsonProperty("Action")
        ArrayList<String> action,

        @JsonProperty("Resource")
        ArrayList<String> resource
) {

    @JsonCreator
    public static Statement of (
            @JsonProperty("Sid") String sid,
            @JsonProperty("Effect") String effect,
            @JsonProperty("Principal") Object principal,
            @JsonProperty("Action") Object action,
            @JsonProperty("Resource") Object resource
    ) throws JsonParseException {

        return new Statement(
                sid,
                effect,
                principal,
                actionFromJson(action),
                resourceFromJson(resource)
        );
    }

    public static ArrayList<String> actionFromJson(Object input) throws JsonParseException {
        try {
            return parseInput(input);
        } catch (JsonParseException ex) {
            throw new JsonParseException(ex.getMessage() + "Action");
        }
    }

    public static ArrayList<String> resourceFromJson(Object input) throws JsonParseException {
        try {
            return parseInput(input);
        } catch (JsonParseException ex) {
            throw new JsonParseException(ex.getMessage() + "Resource");
        }
    }

    private static ArrayList<String> parseInput(Object input) throws JsonParseException {
        if (input instanceof String) {
            ArrayList<String> arrayList = new ArrayList<>(1);
            arrayList.add((String) input);
            return arrayList;
        } else if (input instanceof ArrayList) {
            return (ArrayList<String>) input;
        } else if (input == null) {
            return null;
        } else {
            throw new JsonParseException("Could not deserialize value of field: ");
        }
    }
}
