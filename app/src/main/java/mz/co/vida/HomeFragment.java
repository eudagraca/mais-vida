package mz.co.vida;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import DAO.ConfiguracaoFirebase;
import mehdi.sakout.fancybuttons.FancyButton;
import mz.co.vida.entidades.Post;

public class HomeFragment extends Fragment {

    RecyclerView rv_posts;
    PostAdapter postAdapter;
    List<Post> postsList;
    private DatabaseReference mdataRef;
    private android.widget.SearchView searchView;
    private FancyButton mBtAnunciar;
    ValueEventListener valueEventListener;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);

         searchView = view.findViewById(R.id.pesquisar);
         mBtAnunciar = view.findViewById(R.id.btn_anunciar);
         rv_posts = view.findViewById(R.id.rv_posts);
         rv_posts.setHasFixedSize(true);
         mBtAnunciar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //Instancia o fragmentManager que é o responsável pela troca de Fragment
                 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                 //Instancia o fragment que você vai colocar na tela
                 AnuncioFragment anuncioFragment = new AnuncioFragment();
                 //Faz a transação, substituindo no frame da sua MainActivity que contem os fragmentos, o antigo pelo novo fragmento que você instanciou.
                 fragmentManager.beginTransaction().replace(R.id.fl_framelayout, anuncioFragment).commit();
             }
         });




        postsList = new ArrayList<Post>();
         mdataRef = ConfiguracaoFirebase.getFirebase();
         mdataRef.child("Usuario").orderByChild("disponibilidade").equalTo("Sim");

         mdataRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot info: dataSnapshot.getChildren()){
                     Post post = info.getValue(Post.class);

                     Post data = new Post();
                     data.setName(post.getName());
                     data.setTipodesangue(post.getTipodesangue());
                     data.setEstado(post.getEstado());
                     data.setProvincia(post.getProvincia());

                     System.out.println("Aqui "+info);
                     postsList.add(data);
                     Toast.makeText(getContext(), "Post", Toast.LENGTH_SHORT).show();
                 }
                 if (getActivity() != null){
                     rv_posts.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false)); // set the list layout style to vertical.
                     postAdapter = new PostAdapter(getActivity(), postsList);
                     rv_posts.setAdapter(postAdapter);
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 Toast.makeText(getContext(), "Bugeed", Toast.LENGTH_SHORT).show();
             }
         });

        return view;
    }
    public class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //mdataRef.addValueEventListener(valueEventListener);
    }
}
