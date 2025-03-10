package com.sivalabs.springblog.domain.services;

import java.text.Normalizer;

public class StringUtils {

    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Normalize to remove accents (e.g., "café" -> "cafe")
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{M}", "");

        // Remove non-alphanumeric characters (except spaces)
        String alphanumeric = withoutAccents.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Replace spaces with hyphens and convert to lowercase
        String slug = alphanumeric.trim().replaceAll("\\s+", "-").toLowerCase();

        return slug;
    }
}
