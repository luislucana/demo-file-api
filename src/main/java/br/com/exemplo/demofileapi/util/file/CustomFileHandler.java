package br.com.exemplo.demofileapi.util.file;

import java.io.File;
import java.util.List;

public interface CustomFileHandler {

    List<String> read(final File file);

    void write();

    void split(final File file, final int kbPerSplit);
}
