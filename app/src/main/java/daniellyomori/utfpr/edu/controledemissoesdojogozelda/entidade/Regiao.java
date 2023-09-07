package daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.Tipo;

public class Regiao {
    private String nome;
    private String nomeTorre;
    private Tipo tipo;

    int qtdeShrines;

    public Regiao(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeTorre() {
        return nomeTorre;
    }

    public void setNomeTorre(String nomeTorre) {
        this.nomeTorre = nomeTorre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getQtdeShrines() {
        return qtdeShrines;
    }

    public void setQtdeShrines(int qtdeShrines) {
        this.qtdeShrines = qtdeShrines;
    }
}
