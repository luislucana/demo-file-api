package ignorar.br.com.exemplo.demofileapi.util.file;

import br.com.exemplo.demofileapi.util.FileConstants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CSVFileHandler implements FileHandler {

    public static final String[] HEADERS = { "author", "title" };

    private static final String dir = Paths.get(System.getProperty("user.home") + "/arquivosteste/").toAbsolutePath().toString();

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
    public void split(final File file, final int kbPerSplit) throws IOException {
        List<String> lines = FileUtils.readLines(file, "UTF-8");

        final long bytesPerSplit = 1024L * kbPerSplit;
        int partNumber = 1;
        for (int i = 0; i < lines.size();) {

            Path fileName =
                Paths.get(FileConstants.DEFAULT_DIRECTORY
                    + "/" + file.getName().substring(0, file.getName().length()-4)
                    + "_parte" + String.valueOf(partNumber) + "." + FileConstants.Extension.CSV);

            File splitFile = fileName.toFile();

            long qtdeBytes = 0;
            List<String> splitLines = new ArrayList<>();

            while ((i < lines.size()) && ((qtdeBytes + lines.get(i).getBytes().length) < bytesPerSplit)) {
                byte[] bytes = lines.get(i).getBytes();
                qtdeBytes = qtdeBytes + bytes.length;
                splitLines.add(lines.get(i));
                i++;
            }

            FileUtils.writeLines(splitFile, splitLines, true);
            partNumber++;
        }
    }

    private List<String> readFile(final File file) throws IOException {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

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
