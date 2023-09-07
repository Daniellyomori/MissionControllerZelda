package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Regiao;

public class RegiaoActivity extends AppCompatActivity {
    private ListView listViewRegioes;
    private ArrayList<Regiao> regioes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiao);

        listViewRegioes = findViewById(R.id.listViewRegiao);
        listViewRegioes.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Regiao regiao = (Regiao) listViewRegioes.getItemAtPosition(position);
                        Toast.makeText(getApplicationContext(), getString(R.string.regiao_espaco) + regiao.getNome() + getString(R.string.foi_clicada), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        popularLista();
    }

    public void popularLista(){
        String [] nomesRegioes = getResources().getStringArray(R.array.regioes);
        String[] nomesTorres = getResources().getStringArray(R.array.nome_torres);
        int[]   tipoRegioes_posicoes = getResources().getIntArray(R.array.tipo_regiao);
        int[] qtdeShrines = getResources().getIntArray(R.array.qtde_shrines);

        regioes = new ArrayList<>();

        Regiao regiao;

        Tipo [] tipos = Tipo.values();

        for(int count = 0; count < nomesRegioes.length; count++){
            regiao = new Regiao(nomesRegioes[count]);
            regiao.setNomeTorre(nomesTorres[count]);
            regiao.setTipo(tipos[tipoRegioes_posicoes[count]]);
            regiao.setQtdeShrines(qtdeShrines[count]);

            regioes.add(regiao);
        }

        RegiaoAdapter regiaoAdapter = new RegiaoAdapter(this, regioes);
        listViewRegioes.setAdapter(regiaoAdapter);
    }
}