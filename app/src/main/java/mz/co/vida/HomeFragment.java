package mz.co.vida;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mz.co.vida.DAO.ConfiguracaoFirebase;
import mehdi.sakout.fancybuttons.FancyButton;
import mz.co.vida.entidades.Post;
import android.widget.SearchView;

import static mz.co.vida.MyUtils.SP_NAME;

public class HomeFragment extends Fragment {
    // Components
    RecyclerView rv_posts;
    private FancyButton mBtAnunciar;
    // Vars
    List<Post> postsList;
    // Utils
    PostAdapter postAdapter;
    SharedPreferences sharedPreferences;
    // Firebase Utils
    private DatabaseReference mdataRef;
    private SearchView searchView;

    private Context ctx;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Init Main
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ctx = getContext();
        // Setup Utils
        sharedPreferences = getActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        rv_posts = view.findViewById(R.id.rv_posts);
        rv_posts.setHasFixedSize(true);
        // Init Components
        searchView  = view.findViewById(R.id.pesquisar);
        mBtAnunciar = view.findViewById(R.id.btn_anunciar);
        postsList = new ArrayList<>();


        // Setup Components
        mBtAnunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                startActivity(intent);
            }
        });


        // Setup RecyclerView
        if (getActivity() != null){
            rv_posts.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false)); // set the list layout style to vertical.
            postAdapter = new PostAdapter(getActivity(), postsList);
            rv_posts.setAdapter(postAdapter);
        }

        // get the user's UID
        //FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
       // String uid = firebaseAuth.getUid();
        mdataRef = ConfiguracaoFirebase.getFirebase();
        mdataRef.child("Usuario").orderByChild("disponibilidade").equalTo("Sim").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post;
                postsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                   //if (d.exists())
                   post = d.getValue(Post.class);
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


        postAdapter.setOnClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = getSelectedUserId(position, R.id.tv_id);
                Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("telefone", postsList.get(position).getTelefone());
                intent.putExtra("nome", postsList.get(position).getNome());
                startActivity(intent);
            }
        });


        return view;
    }


    public String getSelectedUserId(int position, int viewRes){
        TextView tv = Objects.requireNonNull(rv_posts.findViewHolderForAdapterPosition(position)).itemView.findViewById(viewRes);
        return tv.getText().toString();
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }
}