package br.com.exemplo.demofileapi.util;

import br.com.exemplo.demofileapi.util.file.FileHandlerSingleton;
import br.com.exemplo.demofileapi.util.file.layout.Field;
import br.com.exemplo.demofileapi.util.file.layout.Header;
import br.com.exemplo.demofileapi.util.file.layout.LayoutFile;
import br.com.exemplo.demofileapi.util.file.layout.LayoutValidator;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Testador {
    public static void main(String[] args) throws IOException {

        // fazer algum teste aqui
        LayoutFile layout = new LayoutFile();
        Header header = layout.getHeader();
        List<Field> fields = header.getFields();
        String name = fields.get(0).getName();

        FileHandlerSingleton csvFileHandler = FileHandlerSingleton.CSV_INSTANCE;

        List<String> linhas = csvFileHandler.read(new File(""));

        String separator = layout.getSeparator();

        //LayoutValidator.validate(linhas);
    }
}
