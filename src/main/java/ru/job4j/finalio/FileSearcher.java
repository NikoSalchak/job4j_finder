package ru.job4j.finalio;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSearcher extends SimpleFileVisitor<Path> {
    private List<Path> paths = new ArrayList<>();
    private Pattern pattern;

    public FileSearcher(Pattern pattern) {
        this.pattern = pattern;
    }

    public List<Path> getPaths() {
        return paths;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Matcher matcher = pattern.matcher(file.toFile().getName());
        if (matcher.find()) {
            System.out.println(file.toAbsolutePath());
            paths.add(file.toAbsolutePath());
        }
        return super.visitFile(file, attrs);
    }
}
