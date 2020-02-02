package ignorar.br.com.exemplo.demofileapi.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileSplitterV3 {
    private static final String dir = Paths.get(System.getProperty("user.home") + "/arquivosteste/").toAbsolutePath().toString();
    private static final String extension = ".txt";

    public static Stream<String> convertFileToStream(String location) throws IOException {
        return Files.lines(Paths.get(location));
    }

    public static void convertStreamToFile(Stream<String> data, Path path) throws IOException {
        Files.write(path, (Iterable<String>) data::iterator);
    }

    /**
     * Split a file into multiples files.
     *
     * @param fileName   Name of file to be split.
     * @param mBperSplit maximum number of MB per file.
     * @throws IOException
     */
    public static List<Path> splitFile(final String fileName, final int mBperSplit) throws IOException {

        if (mBperSplit <= 0) {
            throw new IllegalArgumentException("mBperSplit must be more than zero");
        }

        List<Path> partFiles = new ArrayList<>();
        final long sourceSize = Files.size(Paths.get(fileName));
        final long bytesPerSplit = 1024L * 1024L * mBperSplit;
        final long numSplits = sourceSize / bytesPerSplit;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;

        try (RandomAccessFile sourceFile = new RandomAccessFile(fileName, "r");
             FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++) {
                //write multipart files.
                writePartToFile(bytesPerSplit, fileName, position, position * bytesPerSplit, sourceChannel, partFiles);
            }

            if (remainingBytes > 0) {
                writePartToFile(remainingBytes, fileName, position+1,position * bytesPerSplit, sourceChannel, partFiles);
            }
        }
        return partFiles;
    }

    public static List<Path> splitFile(final File file, final int kbPerSplit) throws IOException {

        if (kbPerSplit <= 0) {
            throw new IllegalArgumentException("kbPerSplit must be more than zero");
        }

        List<Path> partFiles = new ArrayList<>();
        String originalName = file.getName();
        final long sourceSize = Files.size(file.toPath());
        //final long bytesPerSplit = 1024L * 1024L * mBperSplit;
        final long bytesPerSplit = 1024L * kbPerSplit;
        final long numSplits = sourceSize / bytesPerSplit;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;

        try (RandomAccessFile sourceFile = new RandomAccessFile(file, "r");
             FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++) {
                //write multipart files.
                writePartToFile(bytesPerSplit, originalName, position, position * bytesPerSplit, sourceChannel, partFiles);
            }

            if (remainingBytes > 0) {
                writePartToFile(remainingBytes, originalName, position, position * bytesPerSplit, sourceChannel, partFiles);
            }
        }
        return partFiles;
    }

    private static void writePartToFile(long byteSize, String originalFileName, long partNumber,
                                        long position, FileChannel sourceChannel, List<Path> partFiles)
            throws IOException {
        Path fileName =
            Paths.get(dir + "/" + originalFileName.substring(0, originalFileName.length()-4) + "_parte" + String.valueOf(partNumber) + extension);
        try (RandomAccessFile toFile = new RandomAccessFile(fileName.toFile(), "rw");
                FileChannel toChannel = toFile.getChannel()) {
            sourceChannel.position(position);
            toChannel.transferFrom(sourceChannel, 0, byteSize);
        }
        partFiles.add(fileName);
    }
}
