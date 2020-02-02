package br.com.exemplo.demofileapi.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Utils {

    static void printMemoryUsage() {
        Runtime r = Runtime.getRuntime();
        System.out.println("Memory Used: " + (r.totalMemory() - r.freeMemory()));
    }

    static void printAndGetTime(String mensagem) {
        long timeInMillis = System.currentTimeMillis();

    }

    // remover
    private List<String> getTextFileLines(final File textFile) throws IOException {
        LineIterator it = FileUtils.lineIterator(textFile, StandardCharsets.UTF_8.name());
        List<String> lines = FileUtils.readLines(textFile, StandardCharsets.UTF_8);

        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                //System.out.println(line);
                //System.out.println(line.toCharArray().length + " - " + line.);
            }
        } finally {
            LineIterator.closeQuietly(it);
        }

        return lines;
    }

    public static void createTextFiles(final Map<Path, List<String>> filePartsMap) throws IOException {
        for (Map.Entry<Path, List<String>> entry : filePartsMap.entrySet()) {
            File filePart = entry.getKey().toFile();
            List<String> splitLines = entry.getValue();

            FileUtils.writeLines(filePart, StandardCharsets.UTF_8.name(), splitLines,
                    System.getProperty("line.separator"), false);
        }
    }
}
