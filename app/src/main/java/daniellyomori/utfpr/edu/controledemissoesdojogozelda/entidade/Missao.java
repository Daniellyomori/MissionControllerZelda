package daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Missao {

    @PrimaryKey(autoGenerate = true)
    private int id;
    public static final int SIM = 1;
    public static final int NAO = 2;
    @NonNull
    private String nomeMissao;
    @NonNull
    private String nomeNPCMissao;
    @NonNull
    private String regiao;
    @NonNull
    private int precisaCompletarMissao;
    private String qualMissao;
    @NonNull
    private String anotacoes;
    @NonNull
    private boolean missaoCompleta;

    public Missao(String nomeMissao,
                  String nomeNPCMissao,
                  String regiao,
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

    public Missao(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeMissao() {
        return nomeMissao;
    }

    public void setNomeMissao(@NonNull String nomeMissao) {
        this.nomeMissao = nomeMissao;
    }

    public String getNomeNPCMissao() {
        return nomeNPCMissao;
    }

    public void setNomeNPCMissao(@NonNull String nomeNPCMissao) {
        this.nomeNPCMissao = nomeNPCMissao;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(@NonNull String regiao) {
        this.regiao = regiao;
    }

    public int getPrecisaCompletarMissao() {
        return precisaCompletarMissao;
    }

    public void setPrecisaCompletarMissao(@NonNull int precisaCompletarMissao) {
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

    public void setAnotacoes(@NonNull String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public boolean getMissaoCompleta() {
        return missaoCompleta;
    }

    public void setMissaoCompleta(@NonNull boolean missaoCompleta) {
        this.missaoCompleta = missaoCompleta;
    }
}
