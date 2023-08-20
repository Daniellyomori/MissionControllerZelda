package daniellyomori.utfpr.edu.controledemissoesdojogozelda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MissaoAdapter extends BaseAdapter {
    private Context context;
    private List<Missao> missoes;

    private static class MissaoHolder{
        public TextView textViewNomeMissaoValor;
        public TextView textViewNomeNPCMissaoValor;
        public TextView textViewRegiaoValor;
        public TextView textViewCompletarMissaoValor;
        public TextView textViewQualMissaoValor;
        public TextView textViewAnotacoesValor;
        public TextView textViewMissaoCompletaValor;

    }

    public MissaoAdapter(Context context, List<Missao> missoes){
        this.context = context;
        this.missoes = missoes;
    }

    @Override
    public int getCount() {
        return missoes.size();
    }

    @Override
    public Object getItem(int position) {
        return missoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         MissaoHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linha_lista_missao, parent, false);

            holder = new MissaoHolder();

            holder.textViewNomeMissaoValor = convertView.findViewById(R.id.textViewNomeMissaoValor);
            holder.textViewNomeNPCMissaoValor = convertView.findViewById(R.id.textViewNomeNPCMissaoValor);
            holder.textViewRegiaoValor = convertView.findViewById(R.id.textViewRegiaoValor);
            holder.textViewCompletarMissaoValor = convertView.findViewById(R.id.textViewCompletarMissaoValor);
            holder.textViewQualMissaoValor = convertView.findViewById(R.id.textViewQualMissaoValor);
            holder.textViewAnotacoesValor = convertView.findViewById(R.id.textViewAnotacoesValor);
            holder.textViewMissaoCompletaValor = convertView.findViewById(R.id.textViewMissaoCompletaValor);

            convertView.setTag(holder);
        }
        else{
            holder = (MissaoHolder) convertView.getTag();
        }

        holder.textViewNomeMissaoValor.setText(missoes.get(position).getNomeMissao());
        holder.textViewNomeNPCMissaoValor.setText(missoes.get(position).getNomeNPCMissao());
        holder.textViewRegiaoValor.setText(missoes.get(position).getRegiao().getNome());


        switch (missoes.get(position).getPrecisaCompletarMissao()){
            case Missao.SIM:
                holder.textViewCompletarMissaoValor.setText(R.string.sim);
                break;
            case Missao.NAO:
                holder.textViewCompletarMissaoValor.setText(R.string.nao);
                break;
        }

        holder.textViewQualMissaoValor.setText(missoes.get(position).getQualMissao());
        holder.textViewAnotacoesValor.setText(missoes.get(position).getAnotacoes());

        if(missoes.get(position).isMissaoCompleta()){
            holder.textViewMissaoCompletaValor.setText(R.string.sim);
        }
        else{
            holder.textViewMissaoCompletaValor.setText(R.string.nao);
        }

        return convertView;
    }


}
