package br.com.exemplo.demofileapi.util.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class FileHelper {

    private static long startTimeInMillis = 0;
    private static long endTimeInMillis = 0;

    public static void logInicio(final String extension) {
        startTimeInMillis = System.currentTimeMillis();

        Calendar calStart = new GregorianCalendar();
        calStart.setTimeInMillis(startTimeInMillis);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");
        String dataInicioFormatada = simpleDateFormat.format(calStart.getTime());

        System.out.println("================================================================================");
        System.out.println("[" + extension.toUpperCase() + " File Handler] INICIO - " + dataInicioFormatada);
        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void logFim(final String extension) {
        endTimeInMillis = System.currentTimeMillis();

        Calendar calEnd = new GregorianCalendar();
        calEnd.setTimeInMillis(endTimeInMillis);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");
        String dataFimFormatada = simpleDateFormat.format(calEnd.getTime());

        System.out.println("---------------------------------------------------------------------------");
        System.out.println("[" + extension.toUpperCase() + " File Handler] FIM - " + dataFimFormatada);
        System.out.println("[" + extension.toUpperCase() + " File Handler] Tempo gasto (millisegundos): " + String.valueOf((endTimeInMillis - startTimeInMillis)) + " ms");
        System.out.println("===========================================================================");

        startTimeInMillis = 0;
        endTimeInMillis = 0;
    }

    // remover?
    public static void createTextFiles(final Map<Path, List<String>> filePartsMap) throws IOException {
        for (Map.Entry<Path, List<String>> entry : filePartsMap.entrySet()) {
            File filePart = entry.getKey().toFile();
            List<String> splitLines = entry.getValue();

            FileUtils.writeLines(filePart, StandardCharsets.UTF_8.name(), splitLines,
                    System.getProperty("line.separator"), false);
        }
    }

    // remover
    private List<String> getTextFileLines(final File textFile) throws IOException {
        // TODO Charset cp1252 = Charset.forName("CP1252"); ? (ANSI)
        LineIterator it = org.apache.commons.io.FileUtils.lineIterator(textFile, StandardCharsets.UTF_8.name());
        List<String> lines = org.apache.commons.io.FileUtils.readLines(textFile, StandardCharsets.UTF_8);

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
}
