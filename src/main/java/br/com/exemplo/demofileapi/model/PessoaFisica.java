package br.com.exemplo.demofileapi.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PessoaFisica {
    @NotNull
    @Min(value = 1, message = "O codigo do registro deve ter apenas 1 digito.")
    @Max(value = 9, message = "O codigo do registro deve ter apenas 1 digito.")
    private Integer codigoRegistro;

    @NotNull
    @Length(min = 14, max = 14, message = "CPF invalido")
    private String cpf;

    @NotNull
    @Size(min = 25, max = 25, message = "Nome invalido de pessoa juridica")
    private String nome;

    @NotNull
    @Size(min = 14, max = 14, message = "Nome invalido de cidade")
    private String cidade;

    @NotNull
    @Size(min = 11, max = 11, message = "Telefone invalido")
    private String telefone;

    @NotNull
    @Size(min = 31, max = 31, message = "Email invalido")
    private String email;

    public Integer getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(Integer codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
