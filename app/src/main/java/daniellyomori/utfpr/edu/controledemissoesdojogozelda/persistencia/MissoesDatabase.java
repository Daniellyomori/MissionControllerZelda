package daniellyomori.utfpr.edu.controledemissoesdojogozelda.persistencia;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.R;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.Tipo;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Missao;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Regiao;

@Database(entities = {Missao.class, Regiao.class}, version = 1)
public abstract class MissoesDatabase extends RoomDatabase {
    public abstract MissaoDAO missaoDAO();
    public abstract RegiaoDAO regiaoDAO();
    private static MissoesDatabase instance;

    public static MissoesDatabase getDatabase(final Context context){
        if(instance == null){
            synchronized (MissoesDatabase.class){
                if(instance == null){
                    RoomDatabase.Builder builder = Room.databaseBuilder(context, MissoesDatabase.class,
                                                                        "missoes.db");
                    builder.addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    carregaRegioesIniciais(context);
                                }
                            });
                        }
                    });
                    instance = (MissoesDatabase) builder.build();
                }
            }
        }
        return instance;
    }
    private static void carregaRegioesIniciais(final Context context) {
        String[] nomesRegioes = context.getResources().getStringArray(R.array.regioes);
        String[] nomesTorres = context.getResources().getStringArray(R.array.nome_torres);
        int[] tipoRegioes_posicoes = context.getResources().getIntArray(R.array.tipo_regiao);
        int[] qtdeShrines = context.getResources().getIntArray(R.array.qtde_shrines);

        Tipo[] tipos = Tipo.values();

        for (int count = 0; count < nomesRegioes.length; count++) {
            Regiao regiao = new Regiao(nomesRegioes[count]);
            regiao.setNomeTorre(nomesTorres[count]);
            regiao.setTipo(tipos[tipoRegioes_posicoes[count]]);
            regiao.setQtdeShrines(qtdeShrines[count]);

            instance.regiaoDAO().insert(regiao);
        }
    }
}
