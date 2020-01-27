package br.com.exemplo.demofileapi.util.file;

import java.io.File;
import java.util.List;

public interface CustomFileReader {

    List<String> read(final File file);
}
