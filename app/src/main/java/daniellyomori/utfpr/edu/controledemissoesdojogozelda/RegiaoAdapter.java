package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RegiaoAdapter extends BaseAdapter {
    private Context context;
    private List<Regiao> regioes;

    private static class RegiaoHolder{
        public TextView textViewNomeRegiaoValor;
        public TextView textViewNomeTorreValor;
        public TextView textViewTipoRegiaoValor;
        public TextView textViewQtdeShrinesValor;

    }

    public RegiaoAdapter(Context context,  List<Regiao> regioes){
        this.context = context;
        this.regioes = regioes;
    }

    @Override
    public int getCount() {
        return regioes.size();
    }

    @Override
    public Object getItem(int position) {
        return regioes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RegiaoHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linha_lista_regiao, parent, false);

            holder = new RegiaoHolder();

            holder.textViewNomeRegiaoValor = convertView.findViewById(R.id.textViewNomeRegiaoValor);
            holder.textViewNomeTorreValor = convertView.findViewById(R.id.textViewNomeTorreValor);
            holder.textViewTipoRegiaoValor = convertView.findViewById(R.id.textViewTipoRegiaoValor);
            holder.textViewQtdeShrinesValor = convertView.findViewById(R.id.textViewQtdeShrinesValor);

            convertView.setTag(holder);
        }
        else{
            holder = (RegiaoHolder) convertView.getTag();
        }

        holder.textViewNomeRegiaoValor.setText(regioes.get(position).getNome());
        holder.textViewNomeTorreValor.setText(regioes.get(position).getNomeTorre());

        switch (regioes.get(position).getTipo()){
            case CEU:
                holder.textViewTipoRegiaoValor.setText(R.string.ceu);
                break;
            case SUPERFICIE:
                holder.textViewTipoRegiaoValor.setText(R.string.superficie);
                break;
        }

        holder.textViewQtdeShrinesValor.setText(Integer.toString(regioes.get(position).getQtdeShrines()));
        return convertView;
    }


}
