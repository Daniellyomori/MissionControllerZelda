package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Missao;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.persistencia.MissoesDatabase;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.utils.UtilsGUI;

public class MissaoActivity extends AppCompatActivity {

    public static final String MODO = "MODO";

    public static final String ID = "ID";
    public static final String NOME = "NOME";
    public static String NOME_NPC = "NOME_NPC";
    public static final String REGIAO = "REGIAO";
    public static final String PRECISA_COMPLETAR_MISSAO = "PRECISA_COMPLETAR_MISSAO";
    public static final String QUAL_MISSAO = "QUAL_MISSAO";
    public static final String ANOTACOES = "ANOTACOES";
    public static final String MISSAO_COMPLETA = "MISSAO_COMPLETA";

    public static final int NOVO  = 1;
    public static final int ALTERAR = 2;

    private int modo;

    private Missao missao;

    private EditText editTextNomeMissao, editTextNpc, editTextQualMissao, editTextAnotacoes;
    private CheckBox cbMissaoCompleta;
    private RadioGroup radioGroupMissaCompleta;
    private Spinner spinnerRegioes;


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
        setContentView(R.layout.activity_missao);

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

        if(bundle != null){
            modo = bundle.getInt(MODO, NOVO);

            if(modo == NOVO){
                setTitle(getString(R.string.nova_missao));
                missao = new Missao();
            }
            else{

                int id = bundle.getInt(ID);
                MissoesDatabase db = MissoesDatabase.getDatabase(this);

                missao = db.missaoDAO().queryById(id);

                editTextNomeMissao.setText(missao.getNomeMissao());

                editTextNpc.setText(missao.getNomeNPCMissao());

                String regiao = missao.getRegiao();

                for (int count = 0; count < spinnerRegioes.getAdapter().getCount(); count++){
                    String valor = (String) spinnerRegioes.getItemAtPosition(count);
                    if (valor.equals(regiao)){
                        spinnerRegioes.setSelection(count);
                        break;
                    }
                }

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
                setTitle(getString(R.string.alterar_missao));
            }

        }
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

        String regiao = (String) spinnerRegioes.getSelectedItem();
        if (regiao == null) {
            UtilsGUI.erro(this, R.string.erro_regiao_vazia);
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
        missao.setRegiao(regiao);
        missao.setPrecisaCompletarMissao(precisaMissaoCompleta);
        if(precisaMissaoCompleta == Missao.SIM){
            missao.setQualMissao(qualMissao);
        }
        else{
            missao.setQualMissao("");
        }
        missao.setAnotacoes(anotacoes);
        missao.setMissaoCompleta(missaoCompleta);

       MissoesDatabase database = MissoesDatabase.getDatabase(this);

       if(modo == NOVO){
           database.missaoDAO().insert(missao);
       }
       else{
           database.missaoDAO().update(missao);
       }
        setResult(Activity.RESULT_OK);
        finish();
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