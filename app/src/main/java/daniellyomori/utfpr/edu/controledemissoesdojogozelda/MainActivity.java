package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNomeMissao, editTextNpc, editTextQualMissao, editTextAnotacoes;
    private CheckBox cbMissaoCompleta;
    private RadioGroup radioGroupMissaCompleta;
    private Spinner spinnerRegioes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextNomeMissao = findViewById(R.id.editTextNomeMissao);
        editTextNpc = findViewById(R.id.editTextNpcMissao);
        editTextAnotacoes = findViewById(R.id.editTextTextMultiLineAnotacoes);
        editTextQualMissao = findViewById(R.id.editTextQualMissao);

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
        popularRegioes();
    }

    public void limparCampos(View view) {
        editTextNomeMissao.setText(null);
        editTextNpc.setText(null);
        editTextAnotacoes.setText(null);
        editTextQualMissao.setText(null);
        cbMissaoCompleta.setChecked(false);
        radioGroupMissaCompleta.clearCheck();

        editTextNomeMissao.requestFocus();

        Toast.makeText(this, getText(R.string.apagados_valores), Toast.LENGTH_SHORT).show();
    }

    public void popularRegioes(){
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.regioes));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegioes.setAdapter(adapter);
    }

    public void salvar (View view) {
        String mensagemEditText = "";
        String mensagemRadioButton = "";

        String nomeMissao = editTextNomeMissao.getText().toString();
        String anotacoes = editTextAnotacoes.getText().toString();
        String nomeNpc = editTextNpc.getText().toString();

        int radioButtonId = radioGroupMissaCompleta.getCheckedRadioButtonId();
        
        //1. Não utilizados pois não se encaixam nas validacoes solicitadas no enunciado.
        //2. O campo qualRegiao nao foi validado mesmo sendo editText pois o mesmo só é visivel
        //caso opção sim seja marcada, o que causaria muito problema
        //String missaoCompleta = cbMissaoCompleta.getText().toString();
        //String regiao = spinnerRegioes.getSelectedItem().toString();
        //String qualRegiao = editTextQualMissao.getText().toString();

        if(nomeMissao == null|| nomeMissao.trim().isEmpty())
        {
            mensagemEditText += getString(R.string.valida_nome_missao);
            editTextNomeMissao.requestFocus();
        }
        else{
            if(nomeNpc == null|| nomeNpc.trim().isEmpty()){
                mensagemEditText += getString(R.string.valida_nome_npc);
                editTextNpc.requestFocus();
            }
            else{
                if(anotacoes == null|| anotacoes.trim().isEmpty()){
                    mensagemEditText += getString(R.string.valida_anotacoes);
                    editTextAnotacoes.requestFocus();
                }
            }
        }


        if(!(radioButtonId == R.id.radioButtonSim) && !(radioButtonId == R.id.radioButtonNao)){
            mensagemRadioButton +=getString(R.string.valida_radio_button_missao_dependente);
        }

        if(!mensagemEditText.equals("")){
            Toast.makeText(this, mensagemEditText, Toast.LENGTH_SHORT).show();
        }
        if(!mensagemRadioButton.equals("")){
            Toast.makeText(this, mensagemRadioButton, Toast.LENGTH_SHORT).show();
        }

    }


}