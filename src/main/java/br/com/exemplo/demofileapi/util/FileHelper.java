package br.com.exemplo.demofileapi.util;

import br.com.exemplo.demofileapi.model.PessoaFisica;
import br.com.exemplo.demofileapi.model.PessoaJuridica;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

    public static void main(String args[]) throws FileNotFoundException {
        /*Pattern pattern = Pattern.compile("[,\\.\\-;]");

        final CharSequence splitIt =
                new FileAsCharSequence(new File("\\Users\\loce\\Postman\\files\\processed\\test-file.txt"));
        pattern.splitAsStream(splitIt).forEach(System.out::println);

        Stream<String> stream = pattern.splitAsStream(splitIt);

        stream.forEach(System.out::println);*/

        System.out.println(System.getProperty("user.home"));
    }
}
