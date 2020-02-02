package ignorar.br.com.exemplo.demofileapi.util;

import ignorar.br.com.exemplo.demofileapi.model.PessoaFisica;
import ignorar.br.com.exemplo.demofileapi.model.PessoaJuridica;
import ignorar.br.com.exemplo.demofileapi.util.HeaderFields;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.UUID;

public class FileHelper {

    private static String HOME = System.getProperty("user.home");

    public PessoaJuridica pessoaJuridica;

    public PessoaFisica pessoaFisica;

    public static PessoaJuridica extractPessoaJuridica(String line) {
        if (StringUtils.isBlank(line)) {
            return null;
        }

        String codigoRegistro = line.substring(HeaderFields.CODIGO_REGISTRO.getPosicao(),
                (HeaderFields.CODIGO_REGISTRO.getPosicao() + HeaderFields.CODIGO_REGISTRO.getTamanho()));
        String cnpj = line.substring(HeaderFields.CNPJ.getPosicao(),
                (HeaderFields.CNPJ.getPosicao() + HeaderFields.CNPJ.getTamanho()));
        String nome = line.substring(HeaderFields.NOME.getPosicao(),
                (HeaderFields.NOME.getPosicao() + HeaderFields.NOME.getTamanho()));

        PessoaJuridica pj = new PessoaJuridica();
        pj.setCodigoRegistro(Integer.valueOf(codigoRegistro));
        pj.setCnpj(cnpj);
        pj.setNome(nome);

        return pj;
    }

    public static void teste() throws IOException {
        Path dir1 = Paths.get(
                HOME + "/firstdir_" + UUID.randomUUID().toString());
        Path dir2 = Paths.get(
                HOME + "/otherdir_" + UUID.randomUUID().toString());

        Files.createDirectory(dir1);
        Files.createDirectory(dir2);

        Path file1 = dir1.resolve("filetocopy.txt");
        Path file2 = dir2.resolve("filetocopy.txt");
        Files.createFile(file1);

        Files.move(file1, file2);
    }

    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {
        /*Pattern pattern = Pattern.compile("[,\\.\\-;]");

        final CharSequence splitIt =
                new FileAsCharSequence(new File("\\Users\\loce\\Postman\\files\\processed\\test-file.txt"));
        pattern.splitAsStream(splitIt).forEach(System.out::println);

        Stream<String> stream = pattern.splitAsStream(splitIt);

        stream.forEach(System.out::println);*/

        /*String generator = "src/main/resources/origem.txt";

        Path path = Paths.get(generator);

        File file = path.toFile();

        if (file.exists()) {
            System.out.println("existe");
        } else {
            System.out.println("nao existe");
        }

        System.out.println(System.getProperty("user.home"));*/

        // The input string for this test
        /*final String string = "Hello World";

        // Check length, in characters
        System.out.println(string.length()); // prints "11"

        // Check encoded sizes
        final byte[] utf8Bytes = string.getBytes(StandardCharsets.UTF_8);
        System.out.println(utf8Bytes.length); // prints "11"

        final byte[] utf16Bytes= string.getBytes(StandardCharsets.UTF_16);
        System.out.println(utf16Bytes.length); // prints "24"

        final byte[] utf32Bytes = string.getBytes("UTF-32");
        System.out.println(utf32Bytes.length); // prints "44"

        final byte[] isoBytes = string.getBytes(StandardCharsets.ISO_8859_1);
        System.out.println(isoBytes.length); // prints "11"

        final byte[] winBytes = string.getBytes("CP1252");
        System.out.println(winBytes.length); // prints "11"*/

        // ==================================================================
        final String interesting = "\uF93D\uF936\uF949\uF942"; // Chinese ideograms

        // Check length, in characters
        System.out.println(interesting.length()); // prints "4"

        // Check encoded sizes
        final byte[] utf8Bytes = interesting.getBytes("UTF-8");
        System.out.println(utf8Bytes.length); // prints "12"

        final byte[] utf16Bytes= interesting.getBytes("UTF-16");
        System.out.println(utf16Bytes.length); // prints "10"

        final byte[] utf32Bytes = interesting.getBytes("UTF-32");
        System.out.println(utf32Bytes.length); // prints "16"

        final byte[] isoBytes = interesting.getBytes("ISO-8859-1");
        System.out.println(isoBytes.length); // prints "4" (probably encoded "????")

        final byte[] winBytes = interesting.getBytes("CP1252");
        System.out.println(winBytes.length); // prints "4" (probably encoded "????")
    }
}
