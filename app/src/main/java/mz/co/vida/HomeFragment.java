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
import android.widget.SearchView;

public class HomeFragment extends Fragment {

    // Components
    RecyclerView rv_posts;
    private FancyButton mBtAnunciar;

    // Vars
    List<Post> postsList;

    // Utils
    PostAdapter postAdapter;
    ValueEventListener valueEventListener;
    // Firebase Utils
    private DatabaseReference mdataRef;
    private SearchView searchView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Setup Utils
        rv_posts = view.findViewById(R.id.rv_posts);
        rv_posts.setHasFixedSize(true);

        // Init Main
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Init Components
        searchView  = view.findViewById(R.id.pesquisar);
        mBtAnunciar = view.findViewById(R.id.btn_anunciar);
        postsList = new ArrayList<Post>();

        // Setup Components
        mBtAnunciar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // Instancia o fragmentManager que é o responsável pela troca de Fragment
                 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                 // Instancia o fragment que você vai colocar na tela
                 AnuncioFragment anuncioFragment = new AnuncioFragment();
                 // Faz a transação, substituindo no frame da sua MainActivity que contem os fragmentos, o antigo pelo novo fragmento que você instanciou.
                 fragmentManager.beginTransaction().replace(R.id.fl_framelayout, anuncioFragment).commit();
             }
        });


        // Setup RecyclerView
        if (getActivity() != null){
            rv_posts.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false)); // set the list layout style to vertical.
            postAdapter = new PostAdapter(getActivity(), postsList);
            rv_posts.setAdapter(postAdapter);
        }

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

                     // Nunca utilize System.out.println em código Java para Android. Instead, utilize `Log.`
                     // @deprected System.out.println("Aqui "+info);
                     Log.v("Log", "Aqui" + info);
                     postsList.add(data);

                     /**
                        Deixo isto para que vcs façam. É simples:
                        Recomendo que alem de Adapter.notifyDataSetChanged(), utilizem o .notifyItemInserted(position)

                        Arrangem uma forma de saber a posição do último item adicionado e acrescem 1, o que dará o position.
                        Deste modo, vão ter uma forma mais fluida de adiconar elementos nesse recyclerview.

                        Uma dica: já que tem o for() loop, há uma maneira de vocês incrementarem um número, ao se adicionar
                        um elemento no recyclerView e assim terão a posição...


                        // ou se desconseguirem podem deixar o notifyDataSetChanged() sem problemas.
                      */
                     postAdapter.notifyDataSetChanged();
                     // Toast.makeText(getContext(), "Post", Toast.LENGTH_SHORT).show();
                 }

             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 // todo: remove on production...
                 Toast.makeText(getContext(), "Bugged", Toast.LENGTH_SHORT).show();
             }
         });

        return view;
    }

//     Post post = info.getValue(Post.class); // desnecessário



    public class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }

}
