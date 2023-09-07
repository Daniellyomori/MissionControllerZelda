package daniellyomori.utfpr.edu.controledemissoesdojogozelda.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Missao;

@Database(entities = {Missao.class}, version = 1, exportSchema = false)
public abstract class MissoesDatabase extends RoomDatabase {

    public abstract MissaoDAO missaoDAO();

    private static MissoesDatabase instance;

    public static MissoesDatabase getDatabase(final Context context){
        if(instance == null){
            synchronized (MissoesDatabase.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context, MissoesDatabase.class, "missoes.db")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}
