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
import android.widget.SearchView;
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

import mehdi.sakout.fancybuttons.FancyButton;
import mz.co.vida.CreateActivity;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.MyUtils;
import mz.co.vida.PostAdapter;
import mz.co.vida.PostDetailsActivity;
import mz.co.vida.R;
import mz.co.vida.entidades.Post;

import static mz.co.vida.MyUtils.SP_NAME;

public class HomeFragment extends Fragment {
    // Components
    RecyclerView rv_posts;
    // Vars
    List<Post> postsList;
    // Utils
    PostAdapter postAdapter;
    private SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    int fl_framelayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fl_framelayout = R.id.fl_framelayout;
        fragmentManager = getFragmentManager();

    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Init Main
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Context ctx = getContext();
        // Setup Utils
        sharedPreferences = getActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        rv_posts = view.findViewById(R.id.rv_posts);
        rv_posts.setHasFixedSize(true);
        // Init Components
        SearchView searchView = view.findViewById(R.id.pesquisar);
        FancyButton mBtAnunciar = view.findViewById(R.id.btn_anunciar);
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

        // Firebase Utils
        DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase();
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
                String status = getSelectedStatus(position, R.id.tv_estado);
                Intent intent = new Intent(getActivity(), PostDetailsActivity.class);

                intent.putExtra("id", id);
                intent.putExtra("telefone", postsList.get(position).getTelefone());
                intent.putExtra("nome", postsList.get(position).getNome());
                if (status.equals("REQUISITANTE")){
                    startActivity(intent);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    SecondProFrag fragment = new SecondProFrag();
                    fragment.setArguments(bundle);
                    MyUtils.changeFragment(fl_framelayout, fragment, fragmentManager);
                }
            }
        });
        return view;
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