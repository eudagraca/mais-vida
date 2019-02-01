package mz.co.vida.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.robertlevonyan.views.chip.Chip;
import java.util.List;
import mz.co.vida.R;
import mz.co.vida.entities.Doador;
import mz.co.vida.entities.Requisitante;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    private List<Doador> mDoadorList;
    private List<Requisitante> mRequisitanteList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public PostAdapter(Context context, @Nullable List<Doador> doadorList, @Nullable List<Requisitante> requisitanteList) {
        mContext = context;
        mDoadorList = doadorList;
        mRequisitanteList = requisitanteList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.anuncios_list, viewGroup, false);
        return new PostViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {

        if (mDoadorList != null && mDoadorList.size() > 0) {
            Doador doador = mDoadorList.get(position);

            holder.tv_id.setText(doador.getUser_id());
            holder.tv_nome.setText(doador.getNome());
            holder.tv_provincia.setText(doador.getProvincia());
            holder.mli_tipoSangue.setLetter(doador.getTipo_sangue());
            holder.tv_estado.setChipText(MyUtils.DOADOR);

        } else if (mRequisitanteList != null && mRequisitanteList.size() > 0){
            Requisitante requisitante = mRequisitanteList.get(position);

            holder.tv_id.setText(requisitante.getId());
            holder.tv_nome.setText(requisitante.getNome());
            holder.tv_provincia.setText(requisitante.getProvincia());
            holder.mli_tipoSangue.setLetter(requisitante.getTipo_sangue());
            holder.tv_data.setText(requisitante.getData());
            holder.tv_estado.setChipText(MyUtils.REQUISITANTE);
        }
    }

    @Override
    public int getItemCount() {
        if (mDoadorList != null){
            return mDoadorList.size();
        } else {
            return mRequisitanteList.size();
        }
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        MaterialLetterIcon mli_tipoSangue;
        TextView tv_nome, tv_data, tv_provincia, tv_id;
        Chip tv_estado;

   PostViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            tv_id = itemView.findViewById(R.id.tv_id);
            mli_tipoSangue = itemView.findViewById(R.id.mli_tipoSangue);
            tv_nome = itemView.findViewById(R.id.tv_nome);
            tv_data = itemView.findViewById(R.id.tv_data);
            tv_estado = itemView.findViewById(R.id.tv_estado);
            tv_provincia = itemView.findViewById(R.id.tv_provincia);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (listener != null){
                       int position = getAdapterPosition();
                       if (position != RecyclerView.NO_POSITION) { // this check, is to prevent errors when clicking a card that has no position bound to it (for example, a card that we deleted seconds ago)
                           listener.onItemClick(position);
                       }
                   }
               }
           });
        }
    }
}