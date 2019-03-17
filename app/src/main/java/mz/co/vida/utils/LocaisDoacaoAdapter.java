package mz.co.vida.utils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mz.co.vida.R;
import mz.co.vida.entities.LocaisDoacao;

public class LocaisDoacaoAdapter extends RecyclerView.Adapter<LocaisDoacaoAdapter.LocaisViewHolder> {

    private Context mContext;
    private List<LocaisDoacao> locaisDoacaoList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public LocaisDoacaoAdapter(Context mContext, List<LocaisDoacao> locaisDoacaoList) {
        this.mContext = mContext;
        this.locaisDoacaoList = locaisDoacaoList;
    }

    @Override
    public LocaisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.locais_list, parent, false);
        return new LocaisViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(LocaisViewHolder holder, int position) {

        holder.tv_nome.setText(locaisDoacaoList.get(position).getNome());
        holder.tv_localicacao.setText(locaisDoacaoList.get(position).endereco.getLocal());
        holder.tv_id.setText(locaisDoacaoList.get(position).getId());


    }

    @Override
    public int getItemCount() {
        return locaisDoacaoList.size();
    }

    public static class LocaisViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nome, tv_localicacao, tv_id;
        private CardView cardView;


        public LocaisViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            tv_localicacao = itemView.findViewById(R.id.tv_hospital_local);
            tv_nome = itemView.findViewById(R.id.tv_hospital_name);
            cardView = itemView.findViewById(R.id.cardview_id);
            tv_id = itemView.findViewById(R.id.tv_hp_id);

            itemView.setOnClickListener(v -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // this check, is to prevent errors when clicking a card that has no position bound to it (for example, a card that we deleted seconds ago)
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
