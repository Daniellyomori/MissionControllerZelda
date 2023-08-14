package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewMissoes;
    private ArrayList<Missao> listaMissoes;

    MissaoAdapter missaoAdapter;
    private int  posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewMissoes = findViewById(R.id.listViewMissao);

        listViewMissoes.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        posicaoSelecionada = position;
                        alterarMissao();
                    }
                });

        listViewMissoes.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        posicaoSelecionada = position;
                        alterarMissao();
                        return true;
                    }
                });
        popularLista();
    }
    private void popularLista(){
        listaMissoes = new ArrayList<>();
        missaoAdapter = new MissaoAdapter(this, listaMissoes);
        listViewMissoes.setAdapter(missaoAdapter);
    }

    private void alterarMissao(){
        Missao missao = listaMissoes.get(posicaoSelecionada);
        MissaoActivity.alterarMissao(this, missao);
    }

    public void adicionarMissao(View view){
        MissaoActivity.novaMissao(this);
    }

    public void abrirSobre(View view){
        SobreActivity.sobre(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String nomeMissao = bundle.getString(MissaoActivity.NOME);
            String nomeNPC = bundle.getString(MissaoActivity.NOME_NPC);
            String regiao = bundle.getString(MissaoActivity.REGIAO);
            int precisaCompletarMissao = bundle.getInt(MissaoActivity.PRECISA_COMPLETAR_MISSAO);
            String qualMissao = bundle.getString(MissaoActivity.QUAL_MISSAO);
            String anotacoes = bundle.getString(MissaoActivity.ANOTACOES);
            boolean missaoCompleta = bundle.getBoolean(MissaoActivity.MISSAO_COMPLETA);

            if (requestCode == MissaoActivity.ALTERAR) {
                Missao missao = listaMissoes.get(posicaoSelecionada);
                missao.setNomeMissao(nomeMissao);
                missao.setNomeNPCMissao(nomeNPC);
                missao.setRegiao(new Regiao(regiao));
                missao.setPrecisaCompletarMissao(precisaCompletarMissao);
                missao.setQualMissao(qualMissao);
                missao.setAnotacoes(anotacoes);
                missao.setMissaoCompleta(missaoCompleta);
                posicaoSelecionada = -1;

            } else {
                listaMissoes.add(new Missao(nomeMissao, nomeNPC, new Regiao(regiao), precisaCompletarMissao,
                        qualMissao, anotacoes, missaoCompleta));
            }

            missaoAdapter.notifyDataSetChanged();
        }
    }
}