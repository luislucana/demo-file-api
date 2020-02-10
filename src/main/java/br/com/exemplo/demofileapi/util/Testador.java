package br.com.exemplo.demofileapi.util;

import br.com.exemplo.demofileapi.util.file.FileHandlerSingleton;
import br.com.exemplo.demofileapi.util.file.layout.Field;
import br.com.exemplo.demofileapi.util.file.layout.Header;
import br.com.exemplo.demofileapi.util.file.layout.LayoutFile;
import br.com.exemplo.demofileapi.util.file.layout.LayoutValidator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Testador {
    public static void main(String[] args) throws IOException {
        // fazer algum teste aqui
        LayoutFile layout = new LayoutFile();
        Map<String, String> rowIdentifiers = layout.getRowIdentifiers();
        Map<String, List<Field>> rows = layout.getRowFields();

        layout.setSeparator(",");

        layout.setRowFields(null);
        Field field = new Field();
        field.setName("nome");
        field.setType("string");
        field.setSize(100);

        Field field2 = new Field();
        field2.setName("dataNascimento");
        field2.setSize(10);
        field2.setType("date");

        List<Field> campos = new ArrayList<>();
        campos.add(field);
        campos.add(field2);

        Map<String, List<Field>> mapa = new HashMap<>();
        mapa.put("header", campos);

        layout.setRowFields(mapa);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(layout);

        System.out.println(json);


    }
}
