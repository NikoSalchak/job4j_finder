package ru.job4j.finalio;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class SearchCriteria {
    public static void searchFiles(ArgsName argsName) throws IOException {
        validate(argsName);
        String findSequence = argsName.get("n");
        if ("mask".equals(argsName.get("t"))) {
            int position = findSequence.indexOf(".");
            findSequence = addChar(findSequence, '\\', position);
            findSequence = findSequence.replace('?', '.');
            if (findSequence.startsWith("*")) {
                findSequence = addChar(findSequence, '.', 0);
            }
        }
        Pattern pattern = Pattern.compile(findSequence);
        Path path = Path.of(argsName.get("d"));
        FileSearcher fileSearcher = new FileSearcher(pattern);
        Files.walkFileTree(path, fileSearcher);
        try (PrintStream out = new PrintStream(argsName.get("o"))) {
            for (Path file : fileSearcher.getPaths()) {
                out.println(file);
            }
        }
    }

    public static String addChar(String str, char ch, int position) {
        int len = str.length();
        char[] updatedArr = new char[len + 1];
        str.getChars(0, position, updatedArr, 0);
        updatedArr[position] = ch;
        str.getChars(position, len, updatedArr, position + 1);
        return new String(updatedArr);
    }

    public static void validate(ArgsName argsName) {
        boolean filter = true;
        if (!(Files.isDirectory(Path.of(argsName.get("d"))))) {
            throw new IllegalArgumentException(
                    String.format("Error: "
                            + "the value of the %s key must contain the path of the directory to search for", "-d")
            );
        }
        if (argsName.get("n").isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Error: "
                            + "the value of the %s key must contain the file name, mask, or regular expression", "-n")
            );
        }
        for (String substring : "mask,name,regex".split(",")) {
            if (argsName.get("t").contains(substring)) {
                filter = false;
            }
        }
        if (filter) {
            throw new IllegalArgumentException(
                    String.format("Error: "
                            + "the value of the %s key must contain the type of search: name, mask or regex", "-t")
            );
        }
        if (!(argsName.get("o").endsWith("txt"))) {
            throw new IllegalArgumentException(
                    String.format("Error: "
                            + "the value of the %s key must contain the file path for the search result", "o")
            );
        }
    }

    public static void main(String[] args) throws IOException {
        ArgsName argsName = ArgsName.of(args);
        searchFiles(argsName);
    }
}
