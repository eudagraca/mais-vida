package mz.co.vida.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.robertlevonyan.views.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.MyUtils;
import mz.co.vida.PostAdapterDonnor;
import mz.co.vida.PostAdapterResquisitantes;
import mz.co.vida.PostDetailsActivity;
import mz.co.vida.R;
import mz.co.vida.entidades.Post;
import mz.co.vida.entidades.PostReq;

import static mz.co.vida.MyUtils.SP_NAME;


public class RequisitantesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    // Components
    RecyclerView rv_posts;
    // Vars
    List<PostReq> postsList;
    // Utils
    PostAdapterResquisitantes postAdapter;
    private SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    int fl_framelayout;

    public RequisitantesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fl_framelayout = R.id.fl_framelayout;
        fragmentManager = getFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requisitantes, container, false);



        //Context ctx = getContext();
        // Setup Utils
        sharedPreferences = getActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        rv_posts = view.findViewById(R.id.rv_req);
        rv_posts.setHasFixedSize(true);
        // Init Components
        //SearchView searchView = view.findViewById(R.id.pesquisar);
        //FancyButton mBtAnunciar = view.findViewById(R.id.btn_anunciar);
        postsList = new ArrayList<>();

        // Setup RecyclerView
        if (getActivity() != null){
            rv_posts.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false)); // set the list layout style to vertical.
            postAdapter = new PostAdapterResquisitantes(getActivity(), postsList);
            rv_posts.setAdapter(postAdapter);
        }

        // Firebase Utils
        DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase();
        mdataRef.child("anuncios").orderByChild("dataDoacao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PostReq post;
                postsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    //if (d.exists())
                    post = d.getValue(PostReq.class);
                    postsList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // todo: remove on production...
                Toast.makeText(getContext(), "Bugged", Toast.LENGTH_SHORT).show();
            }
        });



        postAdapter.setOnClickListener(new PostAdapterResquisitantes.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = getSelectedUserId(position, R.id.tv_id);

                Intent intent = new Intent(getActivity(), PostDetailsActivity.class);

                intent.putExtra("id", id);
                intent.putExtra("telefone", postsList.get(position).getTelefone());
                intent.putExtra("nome", postsList.get(position).getNome());
                intent.putExtra("quantidade", postsList.get(position).getQuantSanguinea());
                intent.putExtra("data", postsList.get(position).getDataDoacao());
                intent.putExtra("comentario", postsList.get(position).getComentario());
                intent.putExtra("localizacao", postsList.get(position).getLocalizacao());
                intent.putExtra("tipo_sangue", postsList.get(position).getTipo_sangue());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public String getSelectedUserId(int position, int viewRes){
        TextView tv = Objects.requireNonNull(rv_posts.findViewHolderForAdapterPosition(position)).itemView.findViewById(viewRes);
        return tv.getText().toString();
    }

    public String getSelectedStatus(int position, int viewRes){
        Chip chip = Objects.requireNonNull(rv_posts.findViewHolderForAdapterPosition(position)).itemView.findViewById(viewRes);
        return chip.getChipText();
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }

}