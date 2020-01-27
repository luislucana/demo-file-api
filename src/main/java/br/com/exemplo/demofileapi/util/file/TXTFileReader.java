package br.com.exemplo.demofileapi.util.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 *
 */
public class TXTFileReader implements CustomFileReader {

    @Override
    public List<String> read(final File file) {
        List<String> lines = null;

        long startTimeInMillis = System.currentTimeMillis();

        Calendar calStart = new GregorianCalendar();
        calStart.setTimeInMillis(startTimeInMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");
        String dataInicioFormatada = simpleDateFormat.format(calStart.getTime());

        System.out.println("===========================================================================");
        System.out.println("[TXT Reader] INICIO - " + dataInicioFormatada);
        System.out.println("---------------------------------------------------------------------------");

        try {
            lines = this.readFile(file);
        } catch (IOException e) {
            System.out.println("Erro na leitura do arquivo.");
            e.printStackTrace();
        }

        long endTimeInMillis = System.currentTimeMillis();
        Calendar calEnd = new GregorianCalendar();
        calEnd.setTimeInMillis(endTimeInMillis);
        String dataFimFormatada = simpleDateFormat.format(calEnd.getTime());
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("[TXT Reader] FIM - " + dataFimFormatada);
        System.out.println("[TXT Reader] Tempo gasto (millisegundos): " + String.valueOf((endTimeInMillis - startTimeInMillis)) + " ms");
        System.out.println("===========================================================================");

        return lines;
    }

    private List<String> readFile(final File file) throws IOException {
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");

        List<String> lines = FileUtils.readLines(file, "UTF-8");

        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                System.out.println(line);
            }
        } finally {
            LineIterator.closeQuietly(it);
        }

        return lines;
    }
}
