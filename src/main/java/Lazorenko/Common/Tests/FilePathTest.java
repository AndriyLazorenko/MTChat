package Lazorenko.Common.Tests;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by andriylazorenko on 12.07.15.
 */
public class FilePathTest {
    private static final String defaultPath = "./src/main/resources";
    public static void main(String[] args) {
        Path path = Paths.get(defaultPath);
        Path normalized = Paths.get(path.normalize().toString());
        String absoluteFilePath = normalized.toAbsolutePath().toString()+"/file.txt";
        System.out.println(path.toString());
        System.out.println(normalized.toString());
        System.out.println(absoluteFilePath);
    }
}
