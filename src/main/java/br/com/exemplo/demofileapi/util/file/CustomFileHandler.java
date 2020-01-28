package br.com.exemplo.demofileapi.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface CustomFileHandler {

    List<String> read(final File file);

    void write();

    void split(final File file, final int kbPerSplit) throws IOException;
}
