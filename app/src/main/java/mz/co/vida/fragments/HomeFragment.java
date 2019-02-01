package mz.co.vida.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
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
import mz.co.vida.utils.MyUtils;
import mz.co.vida.utils.PostAdapter;

import static android.content.Context.MODE_PRIVATE;
import static mz.co.vida.utils.MyUtils.SP_IS_DOADOR;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final int TAB_DOADOR = 0;
    private static final int TAB_REQUISITANTE = 1;
    private SharedPreferences sharedPreferences;

    // Utils
    private Context ctx;
    private FragmentManager fragmentManager;
    private PostAdapter postAdapter;
    // Vars
    private List<Doador> doadorList;
    private List<Requisitante> requisitanteList;

    //Compontents
    private RecyclerView rv_posts;
    private LinearLayout ll_emptyView;
    private TextView tv_emptyInfo;
    int i=1;

    public HomeFragment() {

    }

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            // Init Utils
            final DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase();
            fragmentManager = getActivity().getSupportFragmentManager();
            ctx = getContext();

            // Init Vars
            doadorList = new ArrayList<>();
            requisitanteList = new ArrayList<>();

            // Init Component
            TabLayout tl_menus = view.findViewById(R.id.tl_donnor_reqq);
            ll_emptyView = view.findViewById(R.id.ll_emptyView);
            tv_emptyInfo = view.findViewById(R.id.tv_emptyInfo);

            // Pre-init: auto select the first tab
            postAdapter = new PostAdapter(ctx, doadorList, null);
            listarPosts(postAdapter, mdataRef, true, doadorList, null);

           // final TabLayout.Tab doador_tab = tl_menus.getTabAt(TAB_DOADOR);
            Objects.requireNonNull(tl_menus.getTabAt(0)).select();

//        if (doador_tab != null){
//            doador_tab.select();
//            doador_tab.getPosition();
//        }

            // Setup
            rv_posts = view.findViewById(R.id.rv_posts);
            rv_posts.setHasFixedSize(true);
            rv_posts.setLayoutManager(new CustomLinearLayoutManager(getContext(), false)); // set the list layout style to vertical.

            tl_menus.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    switch (tab.getPosition()) {
                        case TAB_DOADOR:
                            postAdapter = new PostAdapter(ctx, doadorList, null);
                            tv_emptyInfo.setText("Nenhum doador disponível");
                            listarPosts(postAdapter, mdataRef, true, doadorList, null);
                            break;

                        case TAB_REQUISITANTE:
                            postAdapter = new PostAdapter(ctx, null, requisitanteList);
                            listarPosts(postAdapter, mdataRef, false, null, requisitanteList);
                            tv_emptyInfo.setText("Não há requisições");
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
            estado = MyUtils.DOADOR;
        } else {
            path = "anuncios";
            estado = MyUtils.REQUISITANTE;
        }
        db.child(path).orderByChild("estado").equalTo(estado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isDoador){
                    Doador doador;
                    assert doadorList != null;
                    doadorList.clear();

                    if (dataSnapshot.exists()){
                        ll_emptyView.setVisibility(View.GONE);

                    for (DataSnapshot d: dataSnapshot.getChildren()){
                        doador = d.getValue(Doador.class);
                        if (doador != null && doador.getDisponibilidade().equals("Sim")) {
                            doadorList.add(doador);
                        }
                    }

                    postAdapter.setOnClickListener(new PostAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            String id    = getSelectedUserId(position);
                            String blood = getSelectedUserBlood(position);
                            Intent i = new Intent(getActivity(), DoadorDetailsActivity.class);
                            i.putExtra("id", id);
                            i.putExtra("blood", blood);
                            startActivity(i);
                        }
                    });
                    }else {
                        rv_posts.setVisibility(View.GONE);
                        ll_emptyView.setVisibility(View.VISIBLE);
                        tv_emptyInfo.setText("Não há doadores disponíveis");
                    }

                } else {
                    Requisitante requisitante;
                    assert requisitanteList != null;
                    requisitanteList.clear();

                    if (dataSnapshot.exists()) {

                        ll_emptyView.setVisibility(View.GONE);

                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            requisitante = d.getValue(Requisitante.class);
                            requisitanteList.add(requisitante);
                        }
                        postAdapter.setOnClickListener(new PostAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent i = new Intent(getActivity(), RequisitanteDetailsActivity.class);
                                i.putExtra("nome", requisitanteList.get(position).getNome());
                                i.putExtra("data", requisitanteList.get(position).getData());
                                i.putExtra("contacto", requisitanteList.get(position).getContacto());
                                i.putExtra("quantSanguinea", requisitanteList.get(position).getQuantSanguinea());
                                i.putExtra("descricao", requisitanteList.get(position).getDescricao());
                                i.putExtra("provincia", requisitanteList.get(position).getProvincia());
                                i.putExtra("tipo_sangue", requisitanteList.get(position).getTipo_sangue());
                                startActivity(i);
                            }
                        });
                    }else {
                        rv_posts.setVisibility(View.GONE);
                        ll_emptyView.setVisibility(View.VISIBLE);
                        tv_emptyInfo.setText("Não há anúncios de requisitantes");
                    }
                }
                if (postAdapter.getItemCount() > 0){
                    rv_posts.setAdapter(postAdapter);
                    postAdapter.notifyDataSetChanged();

                } else {
                    // TODO: 11/16/18 Hide recycler
                    // TODO: 11/16/18 Show Layout EmptyView
                    rv_posts.setVisibility(View.GONE);
                    ll_emptyView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // todo: remove on production...
               // Toast.makeText(getContext(), "Bugged", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSelectedUserId(int position){
        TextView tv = Objects.requireNonNull(rv_posts.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.tv_id);
        return tv.getText().toString();
    }

    private String getSelectedUserBlood(int position){
        MaterialLetterIcon tv = Objects.requireNonNull(rv_posts.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.mli_tipoSangue);
        return tv.getLetter();
    }

    class CustomLinearLayoutManager extends LinearLayoutManager {
        CustomLinearLayoutManager(Context context, boolean reverseLayout) {
            super(context, LinearLayoutManager.VERTICAL, reverseLayout);
        }
    }

    @Override
    public void onStart() {

               super.onStart();

              // welcome();
    }
    public void welcome(){


         String i = String.valueOf(sharedPreferences.getInt(MyUtils.CONTADOR, 1));

        Log.i("ALOO", String.valueOf(i));
        if (Integer.parseInt(i)==1) {

            MyUtils.alertaPosetiva(ctx,"Seja bem vindo ao Mais Vida");

            sharedPreferences = ctx.getSharedPreferences(MyUtils.CONTADOR, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            i=String.valueOf(Integer.parseInt(i)+1);
            editor.putInt("Contador", Integer.parseInt(String.valueOf(i)));
            editor.apply();

            Log.i("ALO", String.valueOf(i));
        }


    }

}