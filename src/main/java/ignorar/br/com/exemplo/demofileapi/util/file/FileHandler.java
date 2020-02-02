package ignorar.br.com.exemplo.demofileapi.util.file;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileHandler {

    List<String> read(final File file);

    void split(final File file, final int kbPerSplit) throws IOException;

    void write();
}
