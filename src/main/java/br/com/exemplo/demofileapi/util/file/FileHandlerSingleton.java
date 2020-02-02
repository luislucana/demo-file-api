package br.com.exemplo.demofileapi.util.file;

import br.com.exemplo.demofileapi.util.FileConstants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Baseado nos artigos:
 * https://dzone.com/articles/java-singletons-using-enum
 * https://howtodoinjava.com/design-patterns/creational/singleton-design-pattern-in-java/#enum_singleton
 * https://dzone.com/articles/enum-tricks-two-ways-to-extend-enum-functionality
 * https://www.baeldung.com/creational-design-patterns
 */
public enum FileHandlerSingleton {

    TXT_INSTANCE {
        @Override
        public List<String> read(File file) throws IOException {
            List<String> lines = null;

            try {
                FileHelper.logInicio(FileConstants.Extension.TXT);

                lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

            } finally {
                FileHelper.logFim(FileConstants.Extension.TXT);
            }

            return lines;
        }

        @Override
        public void splitAndStore(File file, int kbPerSplit) throws IOException {
            try {
                FileHelper.logInicio(FileConstants.Extension.TXT);

                final long bytesPerSplit = 1024L * kbPerSplit;
                int partNumber = 1;

                // Apache Commons IO
                long fileSize = FileUtils.sizeOf(file); // bytes

                // tamanho do arquivo <= tamanho máximo de arquivo
                if (fileSize <= bytesPerSplit) {
                    //return null;
                }

                List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

                for (int i = 0; i < lines.size(); partNumber++) {
                    Path fileName =
                            Paths.get(FileConstants.DEFAULT_DIRECTORY
                                    + "/" + file.getName().substring(0, file.getName().length() - 4)
                                    + "_parte" + String.valueOf(partNumber) + "." + FileConstants.Extension.TXT);

                    File filePart = fileName.toFile();

                    long qtdeBytes = 0;
                    List<String> filePartLines = new ArrayList<>();

                    while ((i < lines.size()) && ((qtdeBytes + lines.get(i).getBytes().length) < bytesPerSplit)) {
                        // TODO remover esta linha que somente printa as linhas do arquivo no console
                        //System.out.println(lines.get(i));

                        byte[] bytes = lines.get(i).getBytes();
                        qtdeBytes = qtdeBytes + bytes.length;
                        filePartLines.add(lines.get(i));
                        i++;
                    }

                    FileUtils.writeLines(filePart, StandardCharsets.UTF_8.name(), filePartLines,
                            System.getProperty("line.separator"), false);
                }
            } finally {
                FileHelper.logFim(FileConstants.Extension.TXT);
            }
        }
    },

    CSV_INSTANCE {
        @Override
        public List<String> read(File file) throws IOException {
            List<String> lines = null;

            try {
                FileHelper.logInicio(FileConstants.Extension.CSV);

                lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
                //lines = readCSVFile(file);

            } finally {
                FileHelper.logFim(FileConstants.Extension.CSV);
            }

            return lines;
        }

        @Override
        public void splitAndStore(File file, int kbPerSplit) throws IOException {
            try {
                FileHelper.logInicio(FileConstants.Extension.CSV);

                final long bytesPerSplit = 1024L * kbPerSplit;
                int partNumber = 1;

                // Apache Commons IO
                long fileSize = FileUtils.sizeOf(file); // bytes

                // tamanho do arquivo <= tamanho máximo de arquivo
                if (fileSize <= bytesPerSplit) {
                    //return null;
                }

                List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

                for (int i = 0; i < lines.size(); partNumber++) {
                    Path fileName =
                            Paths.get(FileConstants.DEFAULT_DIRECTORY
                                    + "/" + file.getName().substring(0, file.getName().length() - 4)
                                    + "_parte" + String.valueOf(partNumber) + "." + FileConstants.Extension.CSV);

                    File filePart = fileName.toFile();

                    long qtdeBytes = 0;
                    List<String> filePartLines = new ArrayList<>();

                    while ((i < lines.size()) && ((qtdeBytes + lines.get(i).getBytes().length) < bytesPerSplit)) {
                        // TODO remover esta linha que somente printa as linhas do arquivo no console
                        //System.out.println(lines.get(i));

                        byte[] bytes = lines.get(i).getBytes();
                        qtdeBytes = qtdeBytes + bytes.length;
                        filePartLines.add(lines.get(i));
                        i++;
                    }

                    FileUtils.writeLines(filePart, StandardCharsets.UTF_8.name(), filePartLines,
                            System.getProperty("line.separator"), false);
                }
            } finally {
                FileHelper.logFim(FileConstants.Extension.CSV);
            }
        }

        private List<String> readCSVFile(final File file) throws IOException {
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
    },

    XLS_INSTANCE {
        @Override
        public List<String> read(File file) throws IOException {
            List<String> lines = null;

            try {
                FileHelper.logInicio(FileConstants.Extension.XLS);

                lines = this.readFile(file);

            } finally {
                FileHelper.logFim(FileConstants.Extension.XLS);
            }

            return lines;
        }

        /**
         * Ver
         * https://github.com/eugenp/tutorials/blob/master/spring-mvc-java/src/main/java/com/baeldung/excel/ExcelPOIHelper.java
         * https://stackoverflow.com/questions/37366599/how-to-split-a-excel-file-into-multiple-files-based-on-row-count-using-apache-po/37369058
         *
         * @param file
         * @param kbPerSplit
         * @return
         * @throws IOException
         */
        @Override
        public void splitAndStore(File file, int kbPerSplit) throws IOException {
            try {
                final int maxRows = 100;
                ZipSecureFile.setMinInflateRatio(0); // TODO verificar se realmente eh necessaria esta chamada

                FileHelper.logInicio(FileConstants.Extension.XLS);

                // Read in the original Excel file.
                //OPCPackage pkg = OPCPackage.open(file); // deu ruim aqui!
                //Workbook workbook = WorkbookFactory.create(file); // este funciona!
                HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file)); // este tb funciona!
                Sheet sheet = workbook.getSheetAt(0);

                // Only split if there are more rows than the desired amount.
                if (sheet.getPhysicalNumberOfRows() >= maxRows) {
                    List<Workbook> wbs = splitWorkbook(workbook);
                    writeWorkBooks(file.getAbsolutePath(), wbs);
                }

                /*FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                BufferedOutputStream buffer = new BufferedOutputStream(fileOutputStream);
                IOUtils.writeLines(lines, System.getProperty("line.separator"), buffer, StandardCharsets.UTF_8.name());
                buffer.flush();
                fileOutputStream.close();*/

                workbook.close();
            //} catch (EncryptedDocumentException | IOException | InvalidFormatException e) {
            } catch (EncryptedDocumentException e) {
                e.printStackTrace();
                // TODO implementar
            } finally {
                FileHelper.logFim(FileConstants.Extension.XLS);
            }
        }

        private List<Workbook> splitWorkbook(Workbook workbook) throws IOException {
            final int maxRows = 100;
            List<Workbook> workbooks = new ArrayList<org.apache.poi.ss.usermodel.Workbook>();

            Workbook wb = WorkbookFactory.create(false);
            Sheet newSheet = wb.createSheet();

            Row newRow = null;
            Cell newCell = null;

            int rowCount = 0;
            int colCount = 0;

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                // Time to create a new workbook?
                if (rowCount == maxRows) {
                    workbooks.add(wb);
                    wb = WorkbookFactory.create(false);
                    newSheet = wb.createSheet();
                    rowCount = 0;
                }

                newRow = newSheet.createRow(rowCount++);

                for (Cell cell : row) {
                    newCell = newRow.createCell(colCount++);
                    newCell = setValue(newCell, cell);

                    CellStyle newStyle = wb.createCellStyle();
                    newStyle.cloneStyleFrom(cell.getCellStyle());
                    newCell.setCellStyle(newStyle);
                }

                colCount = 0;

                // TODO Verificar se o exemplo do link abaixo possui melhor desempenho
                // https://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/xssf/eventusermodel/XLSX2CSV.java
                // TODO Verificar tambem se o uso de Spliterator torna mais facil este processo de split
                // https://www.baeldung.com/java-spliterator
            }

            // Only add the last workbook if it has content
            if (wb.getSheetAt(0).getPhysicalNumberOfRows() > 0) {
                workbooks.add(wb);
            }

            return workbooks;
        }

        private Cell setValue(Cell newCell, Cell cell) {
            switch (cell.getCellType()) {
                case STRING:
                    newCell.setCellValue(cell.getRichStringCellValue().getString());
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        newCell.setCellValue(cell.getDateCellValue());
                    } else {
                        newCell.setCellValue(cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    newCell.setCellValue(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    newCell.setCellFormula(cell.getCellFormula());
                    break;
                default:
                    System.out.println("Could not determine cell type");
            }
            return newCell;
        }

        // Write all the workbooks to disk.
        private void writeWorkBooks(String filename, List<Workbook> wbs) throws IOException {
            for (int i = 0; i < wbs.size(); i++) {
                String newFileName = filename.substring(0, filename.length() - 4);
                FileOutputStream out = new FileOutputStream(new File(newFileName + "_" + (i + 1) + "."
                        + FileConstants.Extension.XLS));
                wbs.get(i).write(out);
                out.close();
            }
        }

        private List<String> readFile(File file) throws IOException {
            List<String> lines = new ArrayList<>();
            Map<Integer, List<String>> dataMap = new HashMap<>();

            FileInputStream fileInputStream = new FileInputStream(file);
            //Workbook workbook = new XSSFWorkbook(fileInputStream); // Nao funcionou!
            Workbook workbook = WorkbookFactory.create(file); // Deste jeito funciona!
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for (Row row : sheet) {
                dataMap.put(i, new ArrayList<String>());

                for (Cell cell : row) {
                    //switch (cell.getCellTypeEnum()) { // deprecated
                    switch (cell.getCellType()) {
                        case STRING:
                            dataMap.get(i).add(cell.getRichStringCellValue().getString());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                dataMap.get(i).add(cell.getDateCellValue() + "");
                            } else {
                                dataMap.get(i).add((int) cell.getNumericCellValue() + "");
                            }
                            break;
                        case BOOLEAN:
                            dataMap.get(i).add(cell.getBooleanCellValue() + "");
                            break;
                        case FORMULA:
                            dataMap.get(i).add(cell.getCellFormula() + "");
                            break;
                        default:
                            dataMap.get(i).add(" ");
                    }
                }
                i++;
            }

            if (workbook != null) {
                workbook.close();
            }

            // iterando com lambdas
            // dataMap.forEach((k, v) -> System.out.println((k + ":" + v)));

            // iterando com lambdas e Stream API
            dataMap.entrySet().stream().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
            dataMap.entrySet().stream().forEach(e -> lines.add(e.getValue().toString()));

            return lines;
        }
    };

    private FileHandlerSingleton() {
        // to prevent creating another instance
        // https://javarevisited.blogspot.com/2012/07/why-enum-singleton-are-better-in-java.html
    }

    public abstract List<String> read(final File file) throws IOException;

    public abstract void splitAndStore(final File file, final int kbPerSplit) throws IOException;
}
