package mz.co.vida.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.activities.DoadorDetailsActivity;
import mz.co.vida.activities.RequisitanteDetailsActivity;
import mz.co.vida.entities.Doador;
import mz.co.vida.entities.Requisitante;
import mz.co.vida.utils.PostAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final int TAB_DOADOR = 0;
    public static final int TAB_REQUISITANTE = 1;

    // Utils
    Context ctx;
    FragmentManager fragmentManager;
    PostAdapter postAdapter;

    // Vars
    List<Doador> doadorList;
    List<Requisitante> requisitanteList;

    // Components
    TabLayout tl_menus;
    RecyclerView rv_posts;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Init Utils
        final DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase();
        fragmentManager = getActivity().getSupportFragmentManager();
        ctx = getContext();

        // Init Vars
        doadorList = new ArrayList<>();
        requisitanteList = new ArrayList<>();


        // Init Components
        tl_menus = view.findViewById(R.id.tl_donnor_reqq);

        // Pre-init: auto select the first tab
        TabLayout.Tab doador_tab = tl_menus.getTabAt(TAB_DOADOR);
        if (doador_tab != null){
            doador_tab.select();
        }


        // Setup
        rv_posts = view.findViewById(R.id.rv_posts);
        rv_posts.setHasFixedSize(true);
        rv_posts.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false)); // set the list layout style to vertical.


        tl_menus.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){
                    case TAB_DOADOR:
                        postAdapter = new PostAdapter(ctx, doadorList, null);
                        listarPosts(postAdapter, mdataRef, true, doadorList, null);
                        break;

                    case TAB_REQUISITANTE:
                        postAdapter = new PostAdapter(ctx, null, requisitanteList);
                        listarPosts(postAdapter, mdataRef, false, null, requisitanteList);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;

    }


    private void listarPosts(final PostAdapter postAdapter, DatabaseReference db, final boolean isDoador, @Nullable final List<Doador> doadorList, @Nullable final List<Requisitante> requisitanteList){
        String path;
        String estado;

        if (isDoador){
            path = "Usuario";
            estado = "Doador";

        } else {
            path = "anuncios";
            estado = "Requisitante";
        }

        db.child(path).orderByChild("estado").equalTo(estado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isDoador){
                    Doador doador;
                    doadorList.clear();

                    for (DataSnapshot d: dataSnapshot.getChildren()){
                        doador = d.getValue(Doador.class);
                        doadorList.add(doador);
                    }

                    postAdapter.setOnClickListener(new PostAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            String id = getSelectedUserId(position, R.id.tv_id);
                            Intent i = new Intent(getActivity(), DoadorDetailsActivity.class);
                            i.putExtra("id", id);
                            startActivity(i);
                        }
                    });

                } else {
                    Requisitante requisitante;
                    requisitanteList.clear();

                    for (DataSnapshot d: dataSnapshot.getChildren()){
                        requisitante= d.getValue(Requisitante.class);
                        requisitanteList.add(requisitante);
                    }

                    postAdapter.setOnClickListener(new PostAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent i = new Intent(getActivity(), RequisitanteDetailsActivity.class);
                            i.putExtra("nome",           requisitanteList.get(position).getNome());
                            i.putExtra("data",           requisitanteList.get(position).getDataDoacao());
                            i.putExtra("contacto",       requisitanteList.get(position).getContacto());
                            i.putExtra("quantSanguinea", requisitanteList.get(position).getQuantSanguinea());
                            i.putExtra("descricao",      requisitanteList.get(position).getDescricao());
                            i.putExtra("provincia",      requisitanteList.get(position).getProvincia());
                            i.putExtra("tipo_sangue",    requisitanteList.get(position).getTipo_sangue());
                            startActivity(i);
                        }
                    });
                }


                if (postAdapter.getItemCount() > 0){
                    rv_posts.setAdapter(postAdapter);
                    postAdapter.notifyDataSetChanged();

                } else {
                    // TODO: 11/16/18 Hide recycler
                    // TODO: 11/16/18 Show Layout EmptyView
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // todo: remove on production...
                Toast.makeText(getContext(), "Bugged", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String getSelectedUserId(int position, int viewRes){
        TextView tv = Objects.requireNonNull(rv_posts.findViewHolderForAdapterPosition(position)).itemView.findViewById(viewRes);
        return tv.getText().toString();
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }

}
