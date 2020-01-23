package br.com.exemplo.demofileapi.util;

import br.com.exemplo.demofileapi.model.PessoaFisica;
import br.com.exemplo.demofileapi.model.PessoaJuridica;
import org.apache.commons.lang3.StringUtils;

public class FileHelper {

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
}
