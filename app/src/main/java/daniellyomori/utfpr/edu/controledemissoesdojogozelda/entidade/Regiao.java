package daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.Tipo;
@Entity(tableName = "regiao", indices = @Index(value = {"nome"}, unique = true))
public class Regiao {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NotNull
    private String nome;
    private String nomeTorre;
    private Tipo tipo;
    int qtdeShrines;

    public Regiao(String nome){
        setNome(nome);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    @NonNull
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
    @Override
    public String toString(){
        return getNome();
    }
}
