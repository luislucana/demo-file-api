package br.com.exemplo.demofileapi.util.file;

import br.com.exemplo.demofileapi.util.FileConstants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Baseado nos artigos:
 * https://dzone.com/articles/java-singletons-using-enum
 * https://howtodoinjava.com/design-patterns/creational/singleton-design-pattern-in-java/#enum_singleton
 * https://dzone.com/articles/enum-tricks-two-ways-to-extend-enum-functionality
 */
public enum FileHandlerSingleton {

    TXT_INSTANCE {
        @Override
        public List<String> read(File file) throws IOException {
            List<String> lines = null;

            long startTimeInMillis = System.currentTimeMillis();

            Calendar calStart = new GregorianCalendar();
            calStart.setTimeInMillis(startTimeInMillis);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");
            String dataInicioFormatada = simpleDateFormat.format(calStart.getTime());

            System.out.println("===========================================================================");
            System.out.println("[TXT File Handler] INICIO - " + dataInicioFormatada);
            System.out.println("---------------------------------------------------------------------------");

            lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

            long endTimeInMillis = System.currentTimeMillis();
            Calendar calEnd = new GregorianCalendar();
            calEnd.setTimeInMillis(endTimeInMillis);
            String dataFimFormatada = simpleDateFormat.format(calEnd.getTime());
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("[TXT File Handler] FIM - " + dataFimFormatada);
            System.out.println("[TXT File Handler] Tempo gasto (millisegundos): " + String.valueOf((endTimeInMillis - startTimeInMillis)) + " ms");
            System.out.println("===========================================================================");

            return lines;
        }

        @Override
        public Map<Path, List<String>> split(File file, int kbPerSplit) throws IOException {
            Map<Path, List<String>> filePartsMap = new HashMap<>();

            final long bytesPerSplit = 1024L * kbPerSplit;
            int partNumber = 1;

            // (3) Apache Commons IO
            long fileSize = FileUtils.sizeOf(file); // bytes

            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); partNumber++) {
                Path fileName =
                        Paths.get(FileConstants.DEFAULT_DIRECTORY
                                + "/" + file.getName().substring(0, file.getName().length() - 4)
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

                filePartsMap.put(fileName, splitLines);
                //FileUtils.writeLines(splitFile, splitLines, true);
            }

            return filePartsMap;
        }
    },

    CSV_INSTANCE {
        @Override
        public List<String> read(File file) throws IOException {
            List<String> lines = null;

            long startTimeInMillis = System.currentTimeMillis();

            Calendar calStart = new GregorianCalendar();
            calStart.setTimeInMillis(startTimeInMillis);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");
            String dataInicioFormatada = simpleDateFormat.format(calStart.getTime());

            System.out.println("===========================================================================");
            System.out.println("[CSV File Handler] INICIO - " + dataInicioFormatada);
            System.out.println("---------------------------------------------------------------------------");

            //lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            lines = readCSVFile(file);

            long endTimeInMillis = System.currentTimeMillis();
            Calendar calEnd = new GregorianCalendar();
            calEnd.setTimeInMillis(endTimeInMillis);
            String dataFimFormatada = simpleDateFormat.format(calEnd.getTime());
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("[CSV File Handler] FIM - " + dataFimFormatada);
            System.out.println("[CSV File Handler] Tempo gasto (millisegundos): " + String.valueOf((endTimeInMillis - startTimeInMillis)) + " ms");
            System.out.println("===========================================================================");

            return lines;
        }

        @Override
        public Map<Path, List<String>> split(File file, int kbPerSplit) throws IOException {
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

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

            return null;
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

            long startTimeInMillis = System.currentTimeMillis();

            Calendar calStart = new GregorianCalendar();
            calStart.setTimeInMillis(startTimeInMillis);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");
            String dataInicioFormatada = simpleDateFormat.format(calStart.getTime());

            System.out.println("===========================================================================");
            System.out.println("[XLS File Handler] INICIO - " + dataInicioFormatada);
            System.out.println("---------------------------------------------------------------------------");

            lines = this.readFile(file);

            long endTimeInMillis = System.currentTimeMillis();
            Calendar calEnd = new GregorianCalendar();
            calEnd.setTimeInMillis(endTimeInMillis);
            String dataFimFormatada = simpleDateFormat.format(calEnd.getTime());
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("[XLS File Handler] FIM - " + dataFimFormatada);
            System.out.println("[XLS File Handler] Tempo gasto (millisegundos): " + String.valueOf((endTimeInMillis - startTimeInMillis)) + " ms");
            System.out.println("===========================================================================");

            return lines;
        }

        @Override
        public Map<Path, List<String>> split(File file, int kbPerSplit) throws IOException {
            final int maxRows = 100;
            ZipSecureFile.setMinInflateRatio(0);

            try {
                /* Read in the original Excel file. */
                //OPCPackage pkg = OPCPackage.open(file); // deu ruim aqui!
                Workbook workbook = WorkbookFactory.create(file);
                //XSSFWorkbook workbook = new XSSFWorkbook(pkg);
                //XSSFSheet sheet = workbook.getSheetAt(0);
                Sheet sheet = workbook.getSheetAt(0);

                /* Only split if there are more rows than the desired amount. */
                if (sheet.getPhysicalNumberOfRows() >= maxRows) {
                    List<Workbook> wbs = splitWorkbook(workbook);
                    writeWorkBooks(file.getAbsolutePath(), wbs);
                }
                workbook.close();
                //} catch (EncryptedDocumentException | IOException | InvalidFormatException e) {
            } catch (EncryptedDocumentException | IOException e) {
                e.printStackTrace();
                // TODO implementar
            }

            return null;
        }

        private List<Workbook> splitWorkbook(org.apache.poi.ss.usermodel.Workbook workbook) throws IOException {
            final int maxRows = 100;
            List<org.apache.poi.ss.usermodel.Workbook> workbooks = new ArrayList<org.apache.poi.ss.usermodel.Workbook>();

            //Workbook wb = new SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Workbook wb = WorkbookFactory.create(false);
            Sheet newSheet = wb.createSheet();

            Row newRow = null;
            Cell newCell = null;

            int rowCount = 0;
            int colCount = 0;

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                /* Time to create a new workbook? */
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
            }

            /* Only add the last workbook if it has content */
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

        /* Write all the workbooks to disk. */
        private void writeWorkBooks(String filename, List<Workbook> wbs) {
            FileOutputStream out = null;

            try {
                for (int i = 0; i < wbs.size(); i++) {
                    String newFileName = filename.substring(0, filename.length() - 4);
                    out = new FileOutputStream(new File(newFileName + "_" + (i + 1) + ".xls"));
                    wbs.get(i).write(out);
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private List<String> readFile(File file) throws IOException {
            List<String> lines = null;

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

            return lines;
        }
    };

    private FileHandlerSingleton() {
        // to prevent creating another instance
        // https://javarevisited.blogspot.com/2012/07/why-enum-singleton-are-better-in-java.html
    }

    public abstract List<String> read(final File file) throws IOException;

    public abstract Map<Path, List<String>> split(final File file, final int kbPerSplit) throws IOException;
}
