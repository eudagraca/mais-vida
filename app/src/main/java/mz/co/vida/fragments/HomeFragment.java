package mz.co.vida.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.isapanah.awesomespinner.AwesomeSpinner;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.activities.DoadorDetailsActivity;
import mz.co.vida.activities.RequisitanteDetailsActivity;
import mz.co.vida.entities.Doador;
import mz.co.vida.entities.Requisitante;
import mz.co.vida.utils.MyUtils;
import mz.co.vida.utils.PostAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final int TAB_DOADOR = 0;
    private static final int TAB_REQUISITANTE = 1;
    // Utils
    private Context ctx;
    private PostAdapter postAdapter;
    // Vars
    private List<Doador> doadorList;
    private List<Requisitante> requisitanteList;

    //Compontents
    private RecyclerView rv_posts;
    private LinearLayout ll_emptyView;
    private TextView tv_emptyInfo;
    private ImageView imageView;
    private TabLayout.Tab doador_tab;
    private SweetAlertDialog progressDialog;
    AwesomeSpinner my_spinner, my_spinner_blood;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Init Utils
        final DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase();
        ctx = getContext();
        progressDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(R.color.transparent);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#ff1c1c"));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // Init Vars
        doadorList = new ArrayList<>();
        requisitanteList = new ArrayList<>();

        // Init Component
        TabLayout tl_menus              = view.findViewById(R.id.tl_donnor_reqq);
        ll_emptyView                    = view.findViewById(R.id.ll_emptyView);
        tv_emptyInfo                    = view.findViewById(R.id.tv_emptyInfo);
        imageView                       = view.findViewById(R.id.img_empty_home);
        my_spinner                      = view.findViewById(R.id.my_spinner);
        my_spinner_blood                = view.findViewById(R.id.my_spinner_blood);
        my_spinner.setHintTextColor(Color.GRAY);
        my_spinner_blood.setHintTextColor(Color.GRAY);
        my_spinner.setSelectedItemHintColor(Color.RED);
        my_spinner_blood.setSelectedItemHintColor(Color.RED);

        // Pre-init: auto select the first tab
        postAdapter = new PostAdapter(ctx, doadorList, null);
        listarPosts(postAdapter, mdataRef, true, doadorList, null);

        ArrayAdapter<CharSequence> bloodAdapter = ArrayAdapter.createFromResource(getContext(), R.array.switch_sangue, R.layout.spinner_text_drop);
        my_spinner_blood.setAdapter(bloodAdapter, 0);

        ArrayAdapter<CharSequence> provincesAdapter = ArrayAdapter.createFromResource(getContext(), R.array.provi_local, R.layout.spinner_text_drop);
        my_spinner.setAdapter(provincesAdapter, 0);

        my_spinner_blood.setOnSpinnerItemClickListener((position, itemAtPosition) -> {
            //TODO YOUR ACTIONS
        });
        my_spinner.setOnSpinnerItemClickListener((position, itemAtPosition) -> {
            //TODO YOUR ACTIONS
        });

        doador_tab = tl_menus.getTabAt(TAB_DOADOR);
        Objects.requireNonNull(tl_menus.getTabAt(0)).select();

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
                        tv_emptyInfo.setText(getString(R.string.nenhum_doador));
                        listarPosts(postAdapter, mdataRef, true, doadorList, null);
                        break;

                    case TAB_REQUISITANTE:
                        postAdapter = new PostAdapter(ctx, null, requisitanteList);
                        listarPosts(postAdapter, mdataRef, false, null, requisitanteList);
                        tv_emptyInfo.setText(getString(R.string.sem_requisicoes));
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

    private void listarPosts(final PostAdapter postAdapter, DatabaseReference db, final boolean isDoador, @Nullable final List<Doador> doadorList, @Nullable final List<Requisitante> requisitanteList) {
        String path;
        String estado;

        if (isDoador) {
            path = "Usuario";
            estado = MyUtils.DOADOR;
        } else {
            path = "anuncios";
            estado = MyUtils.REQUISITANTE;
        }

        if (MyUtils.isConnected(getContext())) {

            db.child(path).orderByChild("estado").equalTo(estado).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (isDoador) {
                        Doador doador;
                        assert doadorList != null;
                        doadorList.clear();
                        if (dataSnapshot.exists()) {
                            ll_emptyView.setVisibility(View.GONE);

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                doador = d.getValue(Doador.class);
                                if (doador != null && doador.getDisponibilidade().equals("Sim")) {
                                    progressDialog.dismiss();
                                    doadorList.add(doador);
                                }
                            }
                            postAdapter.setOnClickListener(position -> {
                                String id = getSelectedUserId(position);
                                String blood = getSelectedUserBlood(position);
                                Intent i = new Intent(getActivity(), DoadorDetailsActivity.class);
                                i.putExtra("id", id);
                                i.putExtra("blood", blood);
                                startActivity(i);
                            });
                        } else {
                            rv_posts.setVisibility(View.GONE);
                            ll_emptyView.setVisibility(View.VISIBLE);
                            imageView.setBackgroundResource(R.drawable.ic_empty_box);
                            tv_emptyInfo.setText(getString(R.string.nenhum_doador));
                        }
                    } else {
                        Requisitante requisitante;
                        assert requisitanteList != null;
                        requisitanteList.clear();

                        if (dataSnapshot.exists()) {

                            ll_emptyView.setVisibility(View.GONE);

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                requisitante = d.getValue(Requisitante.class);
                                progressDialog.dismiss();
                                requisitanteList.add(requisitante);
                            }
                            postAdapter.setOnClickListener(position -> {
                                Intent i = new Intent(getActivity(), RequisitanteDetailsActivity.class);
                                i.putExtra("nome", requisitanteList.get(position).getNome());
                                i.putExtra("data", requisitanteList.get(position).getData());
                                i.putExtra("contacto", requisitanteList.get(position).getContacto());
                                i.putExtra("quantSanguinea", requisitanteList.get(position).getQuantSanguinea());
                                i.putExtra("descricao", requisitanteList.get(position).getDescricao());
                                i.putExtra("provincia", requisitanteList.get(position).getProvincia());
                                i.putExtra("tipo_sangue", requisitanteList.get(position).getTipo_sangue());
                                i.putExtra("unidadeProxima", requisitanteList.get(position).getUnidadeProxima());
                                startActivity(i);
                            });
                        } else {

                            if (MyUtils.isConnected(ctx)) {
                                rv_posts.setVisibility(View.GONE);
                                ll_emptyView.setVisibility(View.VISIBLE);
                                imageView.setBackgroundResource(R.drawable.ic_empty_box);
                                tv_emptyInfo.setText(getString(R.string.sem_requisicoes));
                            } else {
                                ll_emptyView.setVisibility(View.VISIBLE);
                                imageView.setBackgroundResource(R.drawable.ic_wifi);
                                tv_emptyInfo.setText(getString(R.string.connection_problem));
                            }
                        }
                    }
                    if (postAdapter.getItemCount() > 0) {
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
                }
            });
        } else {
            ll_emptyView.setVisibility(View.VISIBLE);
            imageView.setBackgroundResource(R.drawable.ic_wifi);
            tv_emptyInfo.setText(getString(R.string.connection_problem));
        }
    }

    private String getSelectedUserId(int position) {
        TextView tv = Objects.requireNonNull(rv_posts.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.tv_id);
        return tv.getText().toString();
    }

    private String getSelectedUserBlood(int position) {
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
        if (!MyUtils.isConnected(ctx)) {
            tv_emptyInfo.setText(R.string.connection_problem);
            rv_posts.setVisibility(View.GONE);
            ll_emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (doador_tab != null) {
            doador_tab.select();
            doador_tab.getPosition();
        }
    }
}