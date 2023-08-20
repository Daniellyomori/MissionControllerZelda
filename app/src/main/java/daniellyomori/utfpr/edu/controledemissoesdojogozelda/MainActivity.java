package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewMissoes;
    private ArrayList<Missao> listaMissoes;

    MissaoAdapter missaoAdapter;
    private int  posicaoSelecionada = -1;
    private View viewSelecionada;

    private androidx.appcompat.view.ActionMode actionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.main_item_selecionado_opcoes, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if(item.getItemId() == R.id.menuItemSelecionadoEditar){
                alterarMissao();
                mode.finish();
                return true;
            }
            else{
                if(item.getItemId() == R.id.menuItemSelecionadoExcluir){
                    excluirMissao();
                    mode.finish();
                    return true;
                }
                else{
                    return false;
                }
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode = null;
            viewSelecionada = null;
            listViewMissoes.setEnabled(true);
        }
    };

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
        listViewMissoes.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewMissoes.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        if(actionMode != null){
                            return false;
                        }
                        posicaoSelecionada = position;
                        view.setBackgroundColor(Color.LTGRAY);
                        viewSelecionada = view;
                        listViewMissoes.setEnabled(false);
                        actionMode = startSupportActionMode(mActionModeCallback);
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

    private void excluirMissao(){
        listaMissoes.remove(posicaoSelecionada);
        missaoAdapter.notifyDataSetChanged();
    }

    public void adicionarMissao(){
        MissaoActivity.novaMissao(this);
    }

    public void abrirSobre(){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuItemAdicionar){
            adicionarMissao();
            return true;
        }
        else{
            if(item.getItemId() == R.id.menuItemSobre){
                abrirSobre();
                return true;
            }
            else{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}