package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.view.ActionMode;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Missao;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.persistencia.MissoesDatabase;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.utils.UtilsGUI;

public class MainActivity extends AppCompatActivity {

    private ListView listViewMissoes;
    private List<Missao> listaMissoes;

    MissaoAdapter missaoAdapter;
    //private int  posicaoSelecionada = -1;
    private View viewSelecionada;

    private androidx.appcompat.view.ActionMode actionMode;

    private static final String ARQUIVO =
            "daniellyomori.utfpr.edu.controledemissoesdojogozelda.sharedpreferences.PREFERENCIAS_ORDENACAO";

    private static final String ORDENACAO = "ORDENCAO";

    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    private String modoOrdenacao = "ASC";

    private static final int REQUEST_NOVA_MISSAO   = 1;
    private static final int REQUEST_ALTERAR_MISSAO = 2;

    private int posicao = -1;

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

            Missao misssao = (Missao) listViewMissoes.getItemAtPosition(posicao);

            if(item.getItemId() == R.id.menuItemSelecionadoEditar) {
                MissaoActivity.alterarMissao(MainActivity.this, REQUEST_ALTERAR_MISSAO, misssao);
                mode.finish();
                return true;
            }
            else{
                if(item.getItemId() == R.id.menuItemSelecionadoExcluir){
                    excluirMissao(misssao);
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
                        posicao = position;

                        Missao missao = (Missao) parent.getItemAtPosition(position);
                        MissaoActivity.alterarMissao(MainActivity.this,
                                REQUEST_ALTERAR_MISSAO,
                                missao);
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
                        posicao = position;
                        view.setBackgroundColor(Color.LTGRAY);
                        viewSelecionada = view;
                        listViewMissoes.setEnabled(false);
                        actionMode = startSupportActionMode(mActionModeCallback);
                        return true;
                    }
                });
        popularLista();
        registerForContextMenu(listViewMissoes);
    }
    private void lerPreferenciaOrdenacao(){
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        modoOrdenacao = shared.getString(ORDENACAO, modoOrdenacao);
        alteraOrdenacao();
    }

    private void salvarPreferenciaOrdenacao(String novoModo){
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        editor.putString(ORDENACAO, novoModo);
        editor.commit();

        modoOrdenacao = novoModo;
        alteraOrdenacao();
    }
    private void alteraOrdenacao(){
        if(modoOrdenacao.equals(ASC)){
            Collections.sort(listaMissoes, new Comparator<Missao>() {
                @Override
                public int compare(final Missao object1, final Missao object2) {
                    return object1.getNomeMissao().compareTo(object2.getNomeMissao());
                }
            });
        }
        else{
            Collections.sort(listaMissoes, new Comparator<Missao>() {
                @Override
                public int compare(final Missao object1, final Missao object2) {
                    return object2.getNomeMissao().compareTo(object1.getNomeMissao());
                }
            });
        }
        missaoAdapter.notifyDataSetChanged();
    }
    private void popularLista(){
        MissoesDatabase database = MissoesDatabase.getDatabase(this);
        listaMissoes = database.missaoDAO().queryAll();
        missaoAdapter = new MissaoAdapter(this, listaMissoes);
        listViewMissoes.setAdapter(missaoAdapter);
        lerPreferenciaOrdenacao();
    }

    private void excluirMissao(final Missao missao){

        String mensagem = getString(R.string.deseja_realmente_apagar) + "\n" + missao.getNomeMissao();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                MissoesDatabase database =
                                        MissoesDatabase.getDatabase(MainActivity.this);

                                database.missaoDAO().delete(missao);

                                missaoAdapter.remove(missao);
                                missaoAdapter.notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilsGUI.confirmar(this, mensagem, listener);
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
        if ((requestCode == REQUEST_NOVA_MISSAO || requestCode == REQUEST_ALTERAR_MISSAO) &&
                resultCode == Activity.RESULT_OK) {

            popularLista();
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
                if(item.getItemId() == R.id.menuOrdenaASC){
                    item.setChecked(true);
                    salvarPreferenciaOrdenacao(ASC);
                    return true;
                }
                else{
                    if(item.getItemId() == R.id.menuOrdenaDESC){
                        item.setChecked(true);
                        salvarPreferenciaOrdenacao(DESC);
                        return true;
                    }
                    else{
                        return super.onOptionsItemSelected(item);
                    }
                }
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item;
        if(modoOrdenacao.equals(ASC)){
            item = menu.findItem(R.id.menuOrdenaASC);
        }
        else{
            if(modoOrdenacao.equals(DESC)){
                item = menu.findItem(R.id.menuOrdenaDESC);
            }
            else{
                return false;
            }
        }
        item.setChecked(true);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.main_opcoes, menu);
    }

}