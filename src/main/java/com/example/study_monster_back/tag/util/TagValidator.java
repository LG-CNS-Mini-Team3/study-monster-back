package com.example.study_monster_back.tag.util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class TagValidator {

    private static final int MAX_TAG_LENGTH = 50;
    private static final Pattern TAG_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣_#.+]+$");

    public boolean isValidTagName(String tagName) {

        if (tagName == null || tagName.trim().length() == 0) {
            return false;
        }

        if (tagName.length() > MAX_TAG_LENGTH) {
            return false;
        }

        return tagName.matches(TAG_PATTERN.pattern());
    }

    public Set<String> filterValidTags(List<String> rawTags) {

        if (rawTags == null) {
            return new HashSet<>();
        }

        return rawTags.stream()
                .filter(this::isValidTagName)
                .map(tag -> tag.toLowerCase().trim())
                .collect(Collectors.toSet());
    }
}
