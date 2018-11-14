package mz.co.vida;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import java.util.List;
import mz.co.vida.entidades.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> mNotificationList;
    private Context mContext;
    //private AdapterView.OnItemClickListener onItemClickListener;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        mContext = context;
        mNotificationList = notificationList;
    }




    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.notification_list, viewGroup, false);
//        status = ProfileActivity.status;
        return new NotificationViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position) {

        if ((mNotificationList != null) && (mNotificationList.size() >0)) {
            Notification notification = mNotificationList.get(position);

            holder.tv_id.setText(notification.getUid());
            holder.tv_nome.setText(notification.getNome());
            holder.tv_descricao.setText(notification.getEstadoDescrito());
            // holder.tv_data.setText(post.getData());
            holder.ml_tipoSangue.setLetter(notification.getTipoSangue());
        }
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

//    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }

    class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MaterialLetterIcon ml_tipoSangue;
        TextView tv_nome, tv_data, tv_id, tv_descricao;


        NotificationViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

       tv_id = itemView.findViewById(R.id.tv_id);
            ml_tipoSangue = itemView.findViewById(R.id.mL_tipoS);
            tv_nome = itemView.findViewById(R.id.tv_nomeN);
            tv_descricao = itemView.findViewById(R.id.actv_status_desc);


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

        @Override
        public void onClick(View v) {
//            mSelectedItem = getAdapterPosition();
//            notifyItemRangeChanged(0, mSingleCheckList.size());
//            mAdapter.onItemHolderClick(NotificationViewHolder.this);
        }
    }

}
