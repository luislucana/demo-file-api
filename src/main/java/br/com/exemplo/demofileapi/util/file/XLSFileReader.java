package br.com.exemplo.demofileapi.util.file;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class XLSFileReader implements CustomFileReader {

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
