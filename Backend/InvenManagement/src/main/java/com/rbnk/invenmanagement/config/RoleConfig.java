package com.rbnk.invenmanagement.config;

import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class RoleConfig {
    static HashMap<String, Integer> tier = new HashMap<>();

    static {
        tier.put("admin", 1);
        tier.put("technician", 2);
        tier.put("student", 3);
    }

    public Integer getTier(String role) {
        return tier.get(role);
    }

    public String getTierName(int tier) {
        return switch (tier) {
            case 1 -> "Admin";
            case 2 -> "Technician";
            case 3 -> "Student";
            default -> null;
        };
    }
}
