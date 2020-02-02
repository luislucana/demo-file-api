package ignorar.br.com.exemplo.demofileapi.util;

public enum HeaderFields {

    CODIGO_REGISTRO(0, 1), CNPJ(1, 14), NOME( 35, 82);

    HeaderFields(Integer posicao, Integer tamanho) {
        this.posicao = posicao;
        this.tamanho = tamanho;
    }

    private Integer posicao;

    private Integer tamanho;

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }
}
