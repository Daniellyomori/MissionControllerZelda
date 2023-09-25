package daniellyomori.utfpr.edu.controledemissoesdojogozelda.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Missao;

@Dao
public interface MissaoDAO {
    @Insert
    long insert(Missao missao);

    @Delete
    void delete(Missao missao);

    @Update
    void update(Missao missao);

    @Query("SELECT * FROM missao WHERE id = :id")
    Missao queryById(int id);

    @Query("SELECT * FROM missao ORDER BY nomeMissao ASC")
    List<Missao> queryAll();

    @Query("SELECT COUNT(*) FROM missao WHERE regiaoId = :id LIMIT 1")
    int queryForRegiaoId(int id);
}
