package daniellyomori.utfpr.edu.controledemissoesdojogozelda.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Regiao;

@Dao
public interface RegiaoDAO {
    @Insert
    long insert(Regiao regiao);

    @Update
    void update(Regiao regiao);

    @Delete
    void delete(Regiao regiao);

    @Query("SELECT * FROM regiao WHERE id = :id")
    Regiao queryForId(int id);

    @Query("SELECT * FROM regiao ORDER BY nome ASC")
    List<Regiao> queryAll();

    @Query("SELECT * FROM regiao WHERE nome = :nome ORDER BY nome ASC")
    List<Regiao> queryForNome(String nome);

    @Query("SELECT COUNT(*) FROM regiao")
    int count();
}
