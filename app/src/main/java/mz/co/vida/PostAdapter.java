package mz.co.vida;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.robertlevonyan.views.chip.Chip;

import java.util.List;

import mz.co.vida.entidades.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> mPostsList;
    private Context mContext;
    private AdapterView.OnItemClickListener onItemClickListener;


    public PostAdapter(Context context, List<Post> postsList) {
        mContext = context;
        mPostsList = postsList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.posts_list, viewGroup, false);
//        status = ProfileActivity.status;
        return new PostViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {
        Post post = mPostsList.get(position);

        holder.tv_nome.setText(post.getName());
        holder.tv_provincia.setText(post.getProvincia());
        setChipTextAndBackground(holder.tv_estado, post.getEstado());
        if (holder.tv_estado.equals("doador"))
        holder.tv_data.setText(post.getData());
        holder.mli_tipoSangue.setLetter(post.getTipodesangue());
    }

    @Override
    public int getItemCount() {
        return mPostsList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void onItemHolderClick(PostViewHolder holder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
        }
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PostAdapter mAdapter;
        MaterialLetterIcon mli_tipoSangue;
        TextView tv_nome, tv_data, tv_provincia;
        Chip tv_estado;




        public PostViewHolder(View itemView, final PostAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;

            mli_tipoSangue = itemView.findViewById(R.id.mli_tipoSangue);
            tv_nome = itemView.findViewById(R.id.tv_nome);
            tv_data = itemView.findViewById(R.id.tv_data);
            tv_estado = itemView.findViewById(R.id.tv_estado);
            tv_provincia = itemView.findViewById(R.id.tv_provincia);

//            itemView.setOnClickListener(this);
//            mRadio.setOnClickListener(this);
        }

        public void setStudentToList(Post item, int position) {

        }

        @Override
        public void onClick(View v) {
//            mSelectedItem = getAdapterPosition();
//            notifyItemRangeChanged(0, mSingleCheckList.size());
//            mAdapter.onItemHolderClick(PostViewHolder.this);
        }
    }

    // Meus métodos:
    private void setChipTextAndBackground(Chip v, String estado){
        String est = estado.toLowerCase();
        v.setChipText(estado.toUpperCase());


        switch (est){
            case "doador":
                v.setBackgroundResource(R.color.md_grey_800);
                break;
            case "requisitante":
                v.setBackgroundResource(R.color.md_grey_600);
                break;
            default:
                v.setBackgroundResource(R.color.md_blue_900);
                break;

        }

    }


}
