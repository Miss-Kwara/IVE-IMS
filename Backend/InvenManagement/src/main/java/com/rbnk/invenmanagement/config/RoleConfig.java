package com.rbnk.invenmanagement.config;

import java.util.HashMap;

public class RoleConfig {
    static HashMap<String, Integer> tier = new HashMap<>();

    static {
        tier.put("admin", 1);
        tier.put("technician", 2);
        tier.put("student", 3);
    }
}
