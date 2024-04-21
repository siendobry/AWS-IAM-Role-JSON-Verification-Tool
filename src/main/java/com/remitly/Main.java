package com.remitly;

import com.remitly.json.JSONParser;
import com.remitly.validator.RolePolicyValidator;
import com.remitly.model.RolePolicy;

public class Main {
    public static void main(String[] args) {
        try {
            String json = ""; // input your document/s here (remember to use proper methods if you do so!)
            String filepath = getFilepathFromArgs(args); // filepath can be specified here directly
            boolean isRobust = getMode(args); // mode can be specified here directly

            RolePolicy rolePolicy = JSONParser.parseDocumentFromPath(filepath);
            System.out.println(RolePolicyValidator.validate(rolePolicy, isRobust));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static String getFilepathFromArgs(String[] args) {
        if (args.length == 1) {
            return args[0];
        } else if (args.length == 2 && getMode(args)) {
            return args[1];
        } else {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
    }

    private static boolean getMode(String[] args) {
        return args.length > 0 && (args[0].equals("--robust") || args[0].equals("-r"));
    }
}