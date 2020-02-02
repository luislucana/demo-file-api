package ignorar.br.com.exemplo.demofileapi.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PessoaJuridica {
    @NotNull
    @Min(value = 1, message = "O codigo do registro deve ter apenas 1 digito.")
    @Max(value = 9, message = "O codigo do registro deve ter apenas 1 digito.")
    private Integer codigoRegistro;

    @NotNull
    @Length(min = 14, max = 14, message = "CNPJ invalido")
    private String cnpj;

    @NotNull
    @Size(min = 3, max = 25, message = "Nome invalido de pessoa juridica")
    private String nome;

    public Integer getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(Integer codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
