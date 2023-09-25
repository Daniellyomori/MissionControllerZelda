package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Regiao;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.persistencia.MissoesDatabase;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.utils.UtilsGUI;

public class RegiaoActivity extends AppCompatActivity {
    public static final String MODO = "MODO";
    public static final String ID = "ID";
    public static final int NOVO  = 1;
    public static final int ALTERAR = 2;
    private int modo;
    private Regiao regiao;

    private EditText editTextNomeRegiao, editTextNomeTorre, editTextQtdeShrines;
    private RadioGroup radioGroupTipo;

    public static void novo(Activity activity, int requestCode){
        Intent intent = new Intent(activity, RegiaoActivity.class);
        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Regiao regiao){
        Intent intent = new Intent(activity, RegiaoActivity.class);
        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, regiao.getId());

        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_regiao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextNomeRegiao = findViewById(R.id.editTextNomeRegiao);
        editTextNomeTorre = findViewById(R.id.editTextNomeTorre);
        editTextQtdeShrines = findViewById(R.id.editTextQtdeShrines);
        radioGroupTipo = findViewById(R.id.radioGroupTipo);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        if (bundle != null){
            modo = bundle.getInt(MODO, NOVO);
        }
        else{
            modo = NOVO;
        }

        if (modo == ALTERAR){
            setTitle(R.string.alterar_regiao);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    int id = bundle.getInt(ID);
                    MissoesDatabase database = MissoesDatabase.getDatabase(RegiaoActivity.this);

                    regiao = database.regiaoDAO().queryForId(id);

                    RegiaoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            editTextNomeRegiao.setText(regiao.getNome());
                            editTextNomeTorre.setText(regiao.getNomeTorre());

                            Tipo tipo = regiao.getTipo();
                            RadioButton button;

                            if(tipo == Tipo.CEU){
                                button = findViewById(R.id.radioButtonCeu);
                                button.setChecked(true);
                            }
                            else if(tipo == Tipo.SUPERFICIE){
                                button = findViewById(R.id.radioButtonSuperficie);
                                button.setChecked(true);
                            }

                            editTextQtdeShrines.setText(String.valueOf(regiao.getQtdeShrines()));
                        }
                    });
                }
            });

        }
        else{
            setTitle(R.string.nova_regiao);
            regiao = new Regiao("");
        }
    }

    private void salvar(){

        final String nomeRegiao  = UtilsGUI.validaCampoTexto(this, editTextNomeRegiao, R.string.valida_nome_regiao);
        if (nomeRegiao == null){
            return;
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MissoesDatabase database = MissoesDatabase.getDatabase(RegiaoActivity.this);
                List<Regiao> lista = database.regiaoDAO().queryForNome(nomeRegiao);

                if (modo == NOVO) {
                    if (lista.size() > 0){
                        RegiaoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UtilsGUI.erro(RegiaoActivity.this, R.string.regiao_existente);
                            }
                        });
                        return;
                    }

                    regiao.setNome(nomeRegiao);
                    regiao.setNomeTorre(editTextNomeTorre.getText().toString());

                    int tipo = radioGroupTipo.getCheckedRadioButtonId();
                    if(tipo == R.id.radioButtonCeu){
                        regiao.setTipo(Tipo.CEU);
                    }
                    else if(tipo == R.id.radioButtonSuperficie){
                        regiao.setTipo(Tipo.SUPERFICIE);
                    }

                    regiao.setQtdeShrines(Integer.parseInt(editTextQtdeShrines.getText().toString()));

                    database.regiaoDAO().insert(regiao);

                } else {

                    if (!nomeRegiao.equals(regiao.getNome())){
                        if (lista.size() >= 1){
                            RegiaoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsGUI.erro(RegiaoActivity.this, R.string.regiao_existente);
                                }
                            });

                            return;
                        }
                        regiao.setNome(nomeRegiao);
                        regiao.setNomeTorre(editTextNomeTorre.getText().toString());

                        int tipo = radioGroupTipo.getCheckedRadioButtonId();
                        if(tipo == R.id.radioButtonCeu){
                            regiao.setTipo(Tipo.CEU);
                        }
                        else if(tipo == R.id.radioButtonSuperficie){
                            regiao.setTipo(Tipo.SUPERFICIE);
                        }

                        regiao.setQtdeShrines(Integer.parseInt(editTextQtdeShrines.getText().toString()));

                        database.regiaoDAO().update(regiao);
                    }
                }
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.missoes_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuItemSalvar){
            salvar();
            return true;
        }
        else{
            if(item.getItemId() == R.id.menuItemLimpar){
                cancelar();
                return true;
            }
            else{
                return super.onOptionsItemSelected(item);
            }
        }
    }

}
