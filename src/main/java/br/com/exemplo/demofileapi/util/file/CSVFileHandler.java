package br.com.exemplo.demofileapi.util.file;

import br.com.exemplo.demofileapi.util.FileConstants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CSVFileHandler implements CustomFileHandler {

    public static final String[] HEADERS = { "author", "title" };

    // TODO calcular quantidade de linhas que cada split de arquivo deverá ter
    private final int maxRows = 100;

    @Override
    public List<String> read(final File file) {
        List<String> lines = null;

        long startTimeInMillis = System.currentTimeMillis();

        Calendar calStart = new GregorianCalendar();
        calStart.setTimeInMillis(startTimeInMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");
        String dataInicioFormatada = simpleDateFormat.format(calStart.getTime());

        System.out.println("===========================================================================");
        System.out.println("[CSV Reader] INICIO - " + dataInicioFormatada);
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
        System.out.println("[CSV Reader] FIM - " + dataFimFormatada);
        System.out.println("[CSV Reader] Tempo gasto (millisegundos): " + String.valueOf((endTimeInMillis - startTimeInMillis)) + " ms");
        System.out.println("===========================================================================");

        return lines;
    }

    @Override
    public void write() {

    }

    @Override
    public void split(final File file, final int kbPerSplit) {

    }

    private List<String> readFile(final File file) throws IOException {
        List<String> lines = FileUtils.readLines(file, "UTF-8");

        //Reader in = new FileReader("src/test/resources/book.csv");
        Reader in = new FileReader(file);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(CSVHeaders.class)
                .withFirstRecordAsHeader().withDelimiter(FileConstants.CSV_SEPARATOR).parse(in);

        for (CSVRecord record : records) {
            String author = record.get(CSVHeaders.AUTHOR);
            String title = record.get(CSVHeaders.TITLE);

            System.out.println("Author: " + author);
            System.out.println("Title: " + title);
        }

        return lines;
    }
}
