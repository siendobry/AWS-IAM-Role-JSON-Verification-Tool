package com.remitly.validator;

import com.remitly.exception.RolePolicyValidationException;
import com.remitly.model.PolicyDocument;
import com.remitly.model.RolePolicy;
import com.remitly.model.Statement;

import java.util.ArrayList;

public class RolePolicyValidator {

    private RolePolicyValidator() {}

    public static boolean validate(RolePolicy rolePolicy, boolean isRobust) {
        if (isRobust) {
            return (rolePolicyCheck(rolePolicy)
                 && policyNameCheck(rolePolicy.policyName())
                 && policyDocumentCheck(rolePolicy.policyDocument())
            );
        } else {
            if (rolePolicy == null
             || rolePolicy.policyDocument() == null
             || rolePolicy.policyDocument().statement() == null
            ) {
                return false;
            }

            for (Statement statement : rolePolicy.policyDocument().statement()) {
                if (!statementResourceCheck(statement.resource())) {
                    return false;
                }
            }

            return true;
        }
    }

    protected static boolean rolePolicyCheck(RolePolicy rolePolicy) {
        if (rolePolicy == null) {
            throw new RolePolicyValidationException("Role policy is null");
        }

        return true;
    }

    protected static boolean policyNameCheck(String policyName) {
        if (policyName == null) {
            throw new RolePolicyValidationException("Role policy name not specified");
        } else if (!policyName.matches("[\\w+=,.@-]+")) {
            throw new RolePolicyValidationException("Role policy name is of wrong format");
        } else if (policyName.isEmpty()) {
            throw new RolePolicyValidationException("Role policy name is too short (minimum length is 1)");
        } else if (policyName.length() > 128) {
            throw new RolePolicyValidationException("Role policy name is too long (maximum length is 128)");
        }

        return true;
    }

    protected static boolean policyDocumentCheck(PolicyDocument policyDocument) {
        return (policyVersionCheck(policyDocument.version())
             && policyStatementCheck(policyDocument.statement())
        );
    }

    protected static boolean policyVersionCheck(String version) {
        if (version == null) {
            throw new RolePolicyValidationException("Policy document version not specified");
        }
        if (!version.equals("2012-10-17") && !version.equals("2008-10-17")) {
            throw new RolePolicyValidationException("Unsupported Version field value");
        }

        return true;
    }

    protected static boolean policyStatementCheck(ArrayList<Statement> statements) {
        if (statements == null) {
            throw new RolePolicyValidationException("Statement field not specified");
        }

        for (Statement statement : statements) {
            if (!statementEffectCheck(statement.effect())
             || !statementPrincipalCheck(statement.principal())
             || !statementActionCheck(statement.action())
             || !statementResourceCheck(statement.resource()))
            {
                return false;
            }
        }

        return true;
    }

    protected static boolean statementEffectCheck(String effect) {
        if (!effect.equals("Allow") && !effect.equals("Deny")) {
            throw new RolePolicyValidationException("Received 'Effect' field value is not allowed");
        }

        return true;
    }

    protected static boolean statementPrincipalCheck(Object principal) {
        if (principal != null) {
            throw new RolePolicyValidationException("Principal cannot be specified in a attached policy document");
        }

        return true;
    }

    protected static boolean statementActionCheck(ArrayList<String> actions) {
        if (actions == null || actions.isEmpty()) {
            throw new RolePolicyValidationException("At least one action has to be specified");
        }

        return true;
    }

    protected static boolean statementResourceCheck(ArrayList<String> resources) {
        if (resources == null) {
            throw new RolePolicyValidationException("Resource field not specified");
        }

        for (String resource : resources) {
            if (resource.equals("*")) {
                return false;
            }
        }

        return true;
    }

}
