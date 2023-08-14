package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MissaoActivity extends AppCompatActivity {

    public static final String MODO = "MODO";
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

    private EditText editTextNomeMissao, editTextNpc, editTextQualMissao, editTextAnotacoes;
    private CheckBox cbMissaoCompleta;
    private RadioGroup radioGroupMissaCompleta;
    private Spinner spinnerRegioes;


    public static void novaMissao(AppCompatActivity activity){
        Intent intent = new Intent(activity, MissaoActivity.class);
        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent, NOVO);
    }

    public static void alterarMissao(AppCompatActivity activity, Missao missao){

        Intent intent = new Intent(activity, MissaoActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(NOME,  missao.getNomeMissao());
        intent.putExtra(NOME_NPC, missao.getNomeNPCMissao());
        intent.putExtra(REGIAO, missao.getRegiao().getNome());
        intent.putExtra(PRECISA_COMPLETAR_MISSAO, missao.getPrecisaCompletarMissao());
        if(missao.getPrecisaCompletarMissao() == Missao.SIM){
            intent.putExtra(QUAL_MISSAO, missao.getQualMissao());
        }
        else{
            intent.putExtra(QUAL_MISSAO, "");
        }
        intent.putExtra(ANOTACOES, missao.getAnotacoes());
        intent.putExtra(MISSAO_COMPLETA, missao.isMissaoCompleta());

        activity.startActivityForResult(intent, ALTERAR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missao);

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
            }
            else{
                String nomeMissao = bundle.getString(NOME);
                editTextNomeMissao.setText(nomeMissao);

                String nomeNPCMissao = bundle.getString(NOME_NPC);
                editTextNpc.setText(nomeNPCMissao);

                String regiao = bundle.getString(REGIAO);

                for (int count = 0; count < spinnerRegioes.getAdapter().getCount(); count++){
                    String valor = (String) spinnerRegioes.getItemAtPosition(count);
                    if (valor.equals(regiao)){
                        spinnerRegioes.setSelection(count);
                        break;
                    }
                }

                int tipo = bundle.getInt(PRECISA_COMPLETAR_MISSAO);
                RadioButton button;

                if(tipo == Missao.SIM){
                    button = findViewById(R.id.radioButtonSim);
                    button.setChecked(true);
                }
                else if(tipo == Missao.NAO){
                    button = findViewById(R.id.radioButtonNao);
                    button.setChecked(true);
                }

                String precisaCompletarMissao = bundle.getString(QUAL_MISSAO);
                editTextQualMissao.setText(precisaCompletarMissao);

                String anotacoes = bundle.getString(ANOTACOES);
                editTextAnotacoes.setText(anotacoes);

                boolean missaoCompleta = bundle.getBoolean(MISSAO_COMPLETA);
                cbMissaoCompleta.setChecked(missaoCompleta);
            }
            setTitle(getString(R.string.alterar_missao));
        }
    }
    public void salvar(View view) {

        String nomeMissao = editTextNomeMissao.getText().toString();
        if (nomeMissao == null || nomeMissao.trim().isEmpty()) {
            Toast.makeText(this, R.string.o_nome_da_missao_nao_pode_ser_vazio, Toast.LENGTH_SHORT).show();
            editTextNomeMissao.requestFocus();
            return;
        }

        String nomeNpc = editTextNpc.getText().toString();
        if (nomeNpc == null || nomeNpc.trim().isEmpty()) {
            Toast.makeText(this, R.string.o_nome_do_npc_nao_pode_ser_vazio, Toast.LENGTH_SHORT).show();
            editTextNpc.requestFocus();
            return;
        }

        String regiao = (String) spinnerRegioes.getSelectedItem();
        if (regiao == null) {
            Toast.makeText(this, getString(R.string.erro_regiao_vazia), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, getString(R.string.erro_outra_missao_precisa_ser_completada),
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String qualMissao = editTextQualMissao.getText().toString();

        String anotacoes = editTextAnotacoes.getText().toString();
        if (anotacoes == null || anotacoes.trim().isEmpty()) {
            Toast.makeText(this, R.string.as_anotacoes_nao_podem_estar_vazia, Toast.LENGTH_SHORT).show();
            editTextNpc.requestFocus();
            return;
        }

        boolean missaoCompleta = cbMissaoCompleta.isChecked();


        Intent intent = new Intent();
        intent.putExtra(NOME,  nomeMissao);
        intent.putExtra(NOME_NPC, nomeNpc);
        intent.putExtra(REGIAO,  regiao);
        intent.putExtra(PRECISA_COMPLETAR_MISSAO, precisaMissaoCompleta);
        if(precisaMissaoCompleta == Missao.SIM){
            intent.putExtra(QUAL_MISSAO, qualMissao);
        }
        else{
            intent.putExtra(QUAL_MISSAO, "");
        }
        intent.putExtra(ANOTACOES, anotacoes);
        intent.putExtra(MISSAO_COMPLETA, missaoCompleta);

        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    public void cancelar(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}