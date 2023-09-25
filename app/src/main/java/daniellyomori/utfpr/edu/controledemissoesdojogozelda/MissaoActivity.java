package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Missao;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Regiao;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.persistencia.MissoesDatabase;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.utils.UtilsGUI;

public class MissaoActivity extends AppCompatActivity {
    public static final String MODO = "MODO";
    public static final String ID = "ID";
    public static final int NOVO  = 1;
    public static final int ALTERAR = 2;

    private int modo;

    private Missao missao;

    private EditText editTextNomeMissao, editTextNpc, editTextQualMissao, editTextAnotacoes;
    private CheckBox cbMissaoCompleta;
    private RadioGroup radioGroupMissaCompleta;
    private Spinner spinnerRegioes;

    private List<Regiao> listaRegioes;


    public static void novaMissao(AppCompatActivity activity){
        Intent intent = new Intent(activity, MissaoActivity.class);
        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent, NOVO);
    }

    public static void alterarMissao(AppCompatActivity activity, int requestCode, Missao missao){
        Intent intent = new Intent(activity, MissaoActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, missao.getId());

        activity.startActivityForResult(intent, requestCode);
    }

    public void limparCampos() {
        editTextNomeMissao.setText(null);
        editTextNpc.setText(null);
        editTextAnotacoes.setText(null);
        editTextQualMissao.setText(null);
        cbMissaoCompleta.setChecked(false);
        radioGroupMissaCompleta.clearCheck();
        spinnerRegioes.setSelection(0);
        editTextNomeMissao.requestFocus();

        Toast.makeText(this, getText(R.string.apagados_valores), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_missao);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextNomeMissao = findViewById(R.id.editTextNomeMissao);
        editTextNpc = findViewById(R.id.editTextNpcMissao);
        editTextAnotacoes = findViewById(R.id.editTextTextMultiLineAnotacoes);
        cbMissaoCompleta = findViewById(R.id.checkBoxMissaoCompleta);
        radioGroupMissaCompleta = findViewById(R.id.radioGroupCompletarMissao);
        spinnerRegioes = findViewById(R.id.spinnerRegiao);

        radioGroupMissaCompleta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButtonSim){
                    editTextQualMissao.setVisibility(View.VISIBLE);
                }
                else{
                    editTextQualMissao.setVisibility(View.GONE);
                }
            }
        });
        editTextQualMissao = findViewById(R.id.editTextQualMissao);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        carregarRegioes();

        if(bundle != null){
            modo = bundle.getInt(MODO, NOVO);

            if(modo == NOVO){
                setTitle(getString(R.string.nova_missao));
                missao = new Missao();
            }
            else{
                setTitle(getString(R.string.alterar_missao));
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        int id = bundle.getInt(ID);

                        MissoesDatabase database = MissoesDatabase
                                .getDatabase(MissaoActivity.this);
                        missao = database.missaoDAO().queryById(id);

                        MissaoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editTextNomeMissao.setText(missao.getNomeMissao());
                                editTextNpc.setText(missao.getNomeNPCMissao());

                                int posicao = posicaoRegiao(missao.getRegiaoId());
                                spinnerRegioes.setSelection(posicao);

                                int tipo = missao.getPrecisaCompletarMissao();
                                RadioButton button;

                                if(tipo == Missao.SIM){
                                    button = findViewById(R.id.radioButtonSim);
                                    button.setChecked(true);
                                }
                                else if(tipo == Missao.NAO){
                                    button = findViewById(R.id.radioButtonNao);
                                    button.setChecked(true);
                                }

                                editTextQualMissao.setText(missao.getQualMissao());

                                editTextAnotacoes.setText(missao.getAnotacoes());

                                boolean missaoCompleta = missao.getMissaoCompleta();
                                cbMissaoCompleta.setChecked(missaoCompleta);
                            }
                        });
                    }
                });
            }
        }
    }

    private int posicaoRegiao(int regiaoId){

        for (int i=0; i<listaRegioes.size(); i++){
            Regiao regiao = listaRegioes.get(i);
            if (regiao.getId() == regiaoId){
                return i;
            }
        }
        return -1;
    }

    private void carregarRegioes(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MissoesDatabase database = MissoesDatabase.getDatabase(MissaoActivity.this);
                listaRegioes = database.regiaoDAO().queryAll();
                MissaoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<Regiao> spinnerAdapter = new ArrayAdapter<>(MissaoActivity.this,
                                        android.R.layout.simple_list_item_1, listaRegioes);

                        spinnerRegioes.setAdapter(spinnerAdapter);
                    }
                });
            }
        });
    }
    public void salvar() {

        String nomeMissao = UtilsGUI.validaCampoTexto(this, editTextNomeMissao,
                R.string.o_nome_da_missao_nao_pode_ser_vazio);
        if (nomeMissao == null ) {
            return;
        }

        String nomeNpc = UtilsGUI.validaCampoTexto(this, editTextNpc,
                R.string.o_nome_do_npc_nao_pode_ser_vazio);
        if (nomeNpc == null) {
            return;
        }

        int precisaMissaoCompleta;

        int radioButtonId = radioGroupMissaCompleta.getCheckedRadioButtonId();
        if(radioButtonId == R.id.radioButtonSim){
            precisaMissaoCompleta = Missao.SIM;
        }
        else{
            if(radioButtonId == R.id.radioButtonNao){
                precisaMissaoCompleta = Missao.NAO;
            }
            else{
                UtilsGUI.erro(this, R.string.erro_outra_missao_precisa_ser_completada);
                return;
            }
        }
        String qualMissao = "";
        if(precisaMissaoCompleta == Missao.SIM) {
            qualMissao = UtilsGUI.validaCampoTexto(this, editTextQualMissao,
                    R.string.erro_qual_missao);
            if (qualMissao == null) {
                return;
            }
        }

        String anotacoes = UtilsGUI.validaCampoTexto(this, editTextAnotacoes,
                R.string.as_anotacoes_nao_podem_estar_vazia);
        if (anotacoes == null) {
            return;
        }

        boolean missaoCompleta = cbMissaoCompleta.isChecked();

        missao.setNomeMissao(nomeMissao);
        missao.setNomeNPCMissao(nomeNpc);

        Regiao regiao = (Regiao) spinnerRegioes.getSelectedItem();
        if (regiao != null){
            missao.setRegiaoId(regiao.getId());
        }

        missao.setPrecisaCompletarMissao(precisaMissaoCompleta);
        if(precisaMissaoCompleta == Missao.SIM){
            missao.setQualMissao(qualMissao);
        }
        else{
            missao.setQualMissao("");
        }
        missao.setAnotacoes(anotacoes);
        missao.setMissaoCompleta(missaoCompleta);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MissoesDatabase database = MissoesDatabase.getDatabase(MissaoActivity.this);

                if (modo == NOVO) {
                    database.missaoDAO().insert(missao);
                }
                else {
                    database.missaoDAO().update(missao);
                }

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    public void cancelar(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.missoes_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuItemSalvar){
            salvar();
            return true;
        }
        else{
            if(item.getItemId() == R.id.menuItemLimpar){
                limparCampos();
                return true;
            }
            else{
                if(item.getItemId() == android.R.id.home){
                    cancelar();
                    return true;
                }
                else{
                    return super.onOptionsItemSelected(item);
                }
            }
        }
    }
}