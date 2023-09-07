package daniellyomori.utfpr.edu.controledemissoesdojogozelda.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import daniellyomori.utfpr.edu.controledemissoesdojogozelda.R;

public class UtilsGUI {
    public static void erro(Context contexto, int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle("Erro");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(id);

        builder.setNeutralButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void confirmar(Context contexto, String mensagem, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle("Confirmação");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(mensagem);

        builder.setPositiveButton(R.string.sim, listener);
        builder.setNegativeButton(R.string.nao, listener);

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String validaCampoTexto(Context contexto, EditText editText, int idMensagemErro){
        String texto = editText.getText().toString();
        if (UtilsString.verificaStringVazia(texto)){
            UtilsGUI.erro(contexto, idMensagemErro);
            editText.setText(null);
            editText.requestFocus();
            return null;
        }else{
            return texto.trim();
        }
    }

}
