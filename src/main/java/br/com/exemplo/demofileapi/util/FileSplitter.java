package br.com.exemplo.demofileapi.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FileSplitter {

    String fileName;
    File file;
    String outputDirectory;

    private AtomicInteger counter = new AtomicInteger(0);

    public int getNextUniqueIndex() {
        return counter.getAndIncrement();
    }

    public FileSplitter(String aInFileName, String aInOutputDirectory) {
        fileName = aInFileName;
        file = new File(fileName);
        outputDirectory = aInOutputDirectory;
    }

    public void splitIntoFiles() {
        long startTime = System.currentTimeMillis();
        try (Stream<String> lFileStream = Files.lines(file.toPath()).parallel()) {

            System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    + "] Start Splitting Process");
            lFileStream.filter(line -> !line.contains("PERMCO")).forEach(line -> {
                String premno = StringInutils.getPremnoFromline(line);

                File outputFile =
                        new File(outputDirectory + File.separator + "CompanyGrossData_"
                                + premno + ".txt");//creation of the file variable with an existing file name or
                // with a new one
                dumpLineIntoFile(outputFile, line);//write concurrently into the output file

                getNextUniqueIndex();

                if (counter.intValue() % (100000) == 0) {
                    System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                            + "] Number Of treated lines are: " + counter
                            + " Spent Time: " + (System.currentTimeMillis() - startTime) / 1000 + " Seconds");
                }
            });

        } catch (IOException e) {
            System.err.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    + "] Split Into Files Error");
            System.err.println(e.getMessage());
        } finally {
            System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    + "] End Splitting Process total spent time is: " +
                    (System.currentTimeMillis() - startTime) / 1000 + " Seconds");
            System.out.println("[" + ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    + "] Total parsed lines are: " + counter);
        }
    }


    private void dumpLineIntoFile(File aInOutputFile, String aInLine) {
        if (!aInOutputFile.exists()) {
            try (BufferedWriter writer = Files.newBufferedWriter(aInOutputFile.toPath(), StandardOpenOption.CREATE)) {

                writer.write(aInLine + "\n");

            } catch (IOException e) {
            }
        } else {
            try (BufferedWriter writer = Files.newBufferedWriter(aInOutputFile.toPath(), StandardOpenOption.APPEND)) {
                writer.write(aInLine + "\n");
            } catch (IOException e) {
            }
        }
    }
}
