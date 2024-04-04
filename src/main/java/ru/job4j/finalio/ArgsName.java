package ru.job4j.finalio;

import java.util.HashMap;
import java.util.Map;

public class ArgsName {
    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException(String.format("This key: '%s' is missing", key));
        }
        return values.get(key);
    }

    private void parse(String[] args) {
        for (String s : args) {
            String[] substrings = s.split("=", 2);
            validateSubstrings(s, substrings);
            values.put(substrings[0].substring(1), substrings[1]);
        }
    }

    private void validateSubstrings(String s, String[] substrings) {
        if (!s.startsWith("-")) {
            throw new IllegalArgumentException(
                    String.format("Error: This argument '%s' does not start with a '-' character", s)
            );
        }
        if (!s.contains("=")) {
            throw new IllegalArgumentException(
                    String.format("Error: This argument '%s' does not contain an equal sign", s)
            );
        }
        if (substrings[0].substring(1).isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Error: This argument '%s' does not contain a key", s)
            );
        }
        if (substrings[1].isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Error: This argument '%s' does not contain a value", s)
            );
        }
    }

    public static ArgsName of(String[] args) {
        ArgsName names = new ArgsName();
        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        names.parse(args);
        return names;
    }

    public static void main(String[] args) {
        ArgsName jvm = ArgsName.of(new String[]{"-Xmx=512", "-encoding=UTF-8"});
        System.out.println(jvm.get("Xmx"));
        ArgsName zip = ArgsName.of(new String[]{"-out=project.zip", "-encoding=UTF-8"});
        System.out.println(zip.get("out"));
    }
}
