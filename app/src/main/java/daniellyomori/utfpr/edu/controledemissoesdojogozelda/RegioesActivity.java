package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.entidade.Regiao;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.persistencia.MissoesDatabase;
import daniellyomori.utfpr.edu.controledemissoesdojogozelda.utils.UtilsGUI;

public class RegioesActivity extends AppCompatActivity {
    private static final int REQUEST_NOVA_REGIAO = 1;
    private static final int REQUEST_ALTERAR_REGIAO = 2;
    private ListView listViewRegioes;

    private ArrayAdapter<Regiao> regiaoAdapter;
    private List<Regiao> regioes;

    public static void iniciar(Activity activity){
        Intent intent = new Intent(activity, RegioesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiao);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listViewRegioes = findViewById(R.id.listViewRegiao);

        listViewRegioes.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Regiao regiao = (Regiao) listViewRegioes.getItemAtPosition(position);
                        RegiaoActivity.alterar(RegioesActivity.this,
                                REQUEST_ALTERAR_REGIAO,
                                regiao);
                    }
                }
        );
        setTitle(R.string.regiao_titulo);
        popularLista();
        registerForContextMenu(listViewRegioes);
    }

    public void popularLista() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MissoesDatabase database = MissoesDatabase.getDatabase(RegioesActivity.this);
                regioes = database.regiaoDAO().queryAll();

                RegioesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        regiaoAdapter = new ArrayAdapter<>(RegioesActivity.this,
                                android.R.layout.simple_list_item_1, regioes);

                        listViewRegioes.setAdapter(regiaoAdapter);

                    }
                });
            }
        });
    }

    private void verificarRegiao(final Regiao regiao){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MissoesDatabase database = MissoesDatabase.getDatabase(RegioesActivity.this);
                int count = database.missaoDAO().queryForRegiaoId(regiao.getId());

                if (count > 0){
                    RegioesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilsGUI.erro(RegioesActivity.this, R.string.regiao_sendo_utilizada);
                        }
                    });
                    return;
                }

                RegioesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        excluirRegiao(regiao);
                    }
                });
            }
        });
    }

    private void excluirRegiao(final Regiao regiao){
        String mensagem = getString(R.string.deseja_realmente_apagar) + "\n" + regiao.getNome();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        MissoesDatabase database = MissoesDatabase.getDatabase(RegioesActivity.this);
                                        database.regiaoDAO().delete(regiao);
                                        RegioesActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                regiaoAdapter.remove(regiao);
                                            }
                                        });
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
        UtilsGUI.confirmar(this, mensagem, listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_NOVA_REGIAO || requestCode == REQUEST_ALTERAR_REGIAO)
                && resultCode == Activity.RESULT_OK){

            popularLista();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.regioes_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuRegiaoAdicionar){
            RegiaoActivity.novo(this, REQUEST_NOVA_REGIAO);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.main_item_selecionado_opcoes, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Regiao regiao = (Regiao) listViewRegioes.getItemAtPosition(info.position);

        if(item.getItemId() == R.id.menuItemSelecionadoEditar){
            RegiaoActivity.alterar(this, REQUEST_ALTERAR_REGIAO, regiao);
            return true;
        }
        else{
            if(item.getItemId() == R.id.menuItemSelecionadoExcluir){
                verificarRegiao(regiao);
                return true;
            }
            else{
                return super.onContextItemSelected(item);
            }
        }
    }
}