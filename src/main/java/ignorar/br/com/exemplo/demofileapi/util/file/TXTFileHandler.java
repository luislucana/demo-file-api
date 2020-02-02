package ignorar.br.com.exemplo.demofileapi.util.file;

import br.com.exemplo.demofileapi.util.FileConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 *
 */
public class TXTFileHandler implements FileHandler {

    // TODO calcular quantidade de linhas que cada split de arquivo deverá ter
    private final int maxRows = 100;

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
                    Paths.get(dir + "/" + file.getName().substring(0, file.getName().length()-4)
                            + "_parte" + String.valueOf(partNumber) + "." + FileConstants.Extension.TXT);
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

    /*@Override
    public void split(final File file, final int kbPerSplit) throws IOException {
        if (kbPerSplit <= 0) {
            throw new IllegalArgumentException("kbPerSplit must be more than zero");
        }

        List<Path> partFiles = new ArrayList<>();
        String originalName = file.getName();
        final long sourceSize = Files.size(file.toPath());
        //final long bytesPerSplit = 1024L * 1024L * mBperSplit;
        final long bytesPerSplit = 1024L * kbPerSplit;
        final long numSplits = sourceSize / bytesPerSplit;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;

        try (RandomAccessFile sourceFile = new RandomAccessFile(file, "r");
             FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++) {
                //write multipart files.
                writePartToFile(bytesPerSplit, originalName, position, position * bytesPerSplit, sourceChannel, partFiles);
            }

            if (remainingBytes > 0) {
                writePartToFile(remainingBytes, originalName, position, position * bytesPerSplit, sourceChannel, partFiles);
            }
        }
    }*/

    private static void writePartToFile(long byteSize, String originalFileName, long partNumber,
                                        long position, FileChannel sourceChannel, List<Path> partFiles)
            throws IOException {
        Path fileName =
                Paths.get(dir + "/" + originalFileName.substring(0, originalFileName.length()-4)
                        + "_parte" + String.valueOf(partNumber) + "." + FileConstants.Extension.TXT);

        try (RandomAccessFile toFile = new RandomAccessFile(fileName.toFile(), "rw");
                FileChannel toChannel = toFile.getChannel()) {
            sourceChannel.position(position);
            toChannel.transferFrom(sourceChannel, 0, byteSize);
        }

        partFiles.add(fileName);
    }

    private List<String> readFile(final File file) throws IOException {
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");

        List<String> lines = FileUtils.readLines(file, "UTF-8");

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
