package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

public class Missao {
    public static final int SIM = 1;
    public static final int NAO = 2;
    private String nomeMissao;
    private String nomeNPCMissao;
    private Regiao regiao;
    private int precisaCompletarMissao;
    private String qualMissao;
    private String anotacoes;
    private boolean missaoCompleta;

    public Missao(String nomeMissao,
                  String nomeNPCMissao,
                  Regiao regiao,
                  int precisaCompletarMissao,
                  String qualMissao,
                  String anotacoes,
                  boolean missaoCompleta) {
        this.nomeMissao = nomeMissao;
        this.nomeNPCMissao = nomeNPCMissao;
        this.regiao = regiao;
        this.precisaCompletarMissao = precisaCompletarMissao;
        this.qualMissao = qualMissao;
        this.anotacoes = anotacoes;
        this.missaoCompleta = missaoCompleta;
    }

    public String getNomeMissao() {
        return nomeMissao;
    }

    public void setNomeMissao(String nomeMissao) {
        this.nomeMissao = nomeMissao;
    }

    public String getNomeNPCMissao() {
        return nomeNPCMissao;
    }

    public void setNomeNPCMissao(String nomeNPCMissao) {
        this.nomeNPCMissao = nomeNPCMissao;
    }

    public Regiao getRegiao() {
        return regiao;
    }

    public void setRegiao(Regiao regiao) {
        this.regiao = regiao;
    }

    public int getPrecisaCompletarMissao() {
        return precisaCompletarMissao;
    }

    public void setPrecisaCompletarMissao(int precisaCompletarMissao) {
        this.precisaCompletarMissao = precisaCompletarMissao;
    }

    public String getQualMissao() {
        return qualMissao;
    }

    public void setQualMissao(String qualMissao) {
        this.qualMissao = qualMissao;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public boolean isMissaoCompleta() {
        return missaoCompleta;
    }

    public void setMissaoCompleta(boolean missaoCompleta) {
        this.missaoCompleta = missaoCompleta;
    }
}
