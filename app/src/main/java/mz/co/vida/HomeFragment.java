package mz.co.vida;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import mz.co.vida.entidades.Post;


public class HomeFragment extends Fragment {


    RecyclerView rv_posts;
    PostAdapter postAdapter;
    List<Post> postsList;

    private android.widget.SearchView searchView;
    private FancyButton mBtAnunciar;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);

         searchView = view.findViewById(R.id.pesquisar);
         mBtAnunciar = view.findViewById(R.id.btn_anunciar);

         mBtAnunciar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //Intent intent = new Intent(HomeFragment.this, AnuncioFragment.class);
                 //startActivity(intent);
             }
         });


        if (getActivity() != null){
            rv_posts = view.findViewById(R.id.rv_posts);
            rv_posts.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false)); // set the list layout style to vertical.

            postsList = new ArrayList<>();

            Post post1 = new Post("Camila Joana", "+O", "Maputo", "10/09/2018", "Imediata", "doadora", "");
            Post post2 = new Post("Débora William", "+AB", "Cabo Delegado", "10/09/2018", "Imediata", "Requisitante", "");
            Post post3 = new Post("Jessica Abreu", "-B", "Inhambane", "09/09/2018", "Não Imediata", "REQUISITANTE", "");
            Post post4 = new Post("Deny OG", "+A", "Maputo", "06/09/2018", "Imediata", "DOADOR", "");
            Post post5 = new Post("Ruben Maria", "+B", "Inhambane", "06/09/2018", "Imediata", "requisitante", "");


            for (int i = 0; i < 9; i++) {
                postsList.add(post1);
                postsList.add(post2);
                postsList.add(post3);
                postsList.add(post4);
                postsList.add(post5);
            }


            postAdapter = new PostAdapter(getActivity(), postsList);
            rv_posts.setAdapter(postAdapter);
        }



        return view;
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }


}
