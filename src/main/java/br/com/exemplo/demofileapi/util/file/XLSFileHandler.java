package br.com.exemplo.demofileapi.util.file;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XLSFileHandler implements CustomFileHandler {

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
        System.out.println("[XLS Reader] INICIO - " + dataInicioFormatada);
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
        System.out.println("[XLS Reader] FIM - " + dataFimFormatada);
        System.out.println("[XLS Reader] Tempo gasto (millisegundos): " + String.valueOf((endTimeInMillis - startTimeInMillis)) + " ms");
        System.out.println("===========================================================================");

        return lines;
    }

    @Override
    public void write() {

    }

    @Override
    public void split(final File file, final int kbPerSplit) {
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
            // TODO mensagem de erro
        }
    }

    private List<Workbook> splitWorkbook(Workbook workbook) throws IOException {
        List<Workbook> workbooks = new ArrayList<Workbook>();

        //Workbook wb = new SXSSFWorkbook();
        Workbook wb = WorkbookFactory.create(false);
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
                String newFileName = filename.substring(0, filename.length() - 5);
                out = new FileOutputStream(new File(newFileName + "_" + (i + 1) + ".xls"));
                wbs.get(i).write(out);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readFile(File file) throws IOException {
        //List<String> lines = FileUtils.readLines(file, "UTF-8");
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
                switch (cell.getCellTypeEnum()) {
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
}
