package mz.co.vida.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

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
import mz.co.vida.activities.DonnateDetailsActivity;
import mz.co.vida.entities.Endereco;
import mz.co.vida.entities.LocaisDoacao;
import mz.co.vida.utils.LocaisDoacaoAdapter;

public class LocaisFragment extends Fragment {

    //Vars
    protected List<LocaisDoacao> locaisDoacaoList;
    protected String value;
    private LocaisDoacaoAdapter locaisDoacaoAdapter;
    //Components
    private RecyclerView recyclerView;
    private LinearLayout ll_empty;
    private TextView tv_emptyLocals;
    private ImageView img_empty_locals;

    //Utils
    Context context;
    private AwesomeSpinner spinner_provincias;
    private ArrayAdapter<CharSequence> provincesAdapter;

    public LocaisFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_locais, container, false);

        context = getActivity();
        //init Components
        recyclerView                = view.findViewById(R.id.rv_locals);
        spinner_provincias          = view.findViewById(R.id.sp_coleta_prov);
        ll_empty                    = view.findViewById(R.id.ll_emptyLocals);
        tv_emptyLocals              = view.findViewById(R.id.tv_emptyLocals);
        img_empty_locals            = view.findViewById(R.id.img_empty_locals);

        spinner_provincias.setSelectedItemHintColor(Color.RED);
        //Setup vars
        locaisDoacaoList = new ArrayList<>();
        DatabaseReference database = ConfiguracaoFirebase.getFirebase();
        //Setup Components
        provincesAdapter = ArrayAdapter.createFromResource(getContext(), R.array.provi_local, R.layout.spinner_text_drop);
        spinner_provincias.setAdapter(provincesAdapter, 0);

        SweetAlertDialog progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#ff1c1c"));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Order list by province
        spinner_provincias.setOnSpinnerItemClickListener((position, itemAtPosition) ->  {
            value = spinner_provincias.getSelectedItem();
            if (!value.equals("Todas")) {
                database.child("Colecta_Sanguinea").orderByChild("endereco_provincia").equalTo(value).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        locaisDoacaoList.clear();
                        if (dataSnapshot.exists()) {
                            recyclerView.setVisibility(View.VISIBLE);
                            ll_empty.setVisibility(View.GONE);
                            img_empty_locals.setVisibility(View.GONE);
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                LocaisDoacao locais = new LocaisDoacao();
                                Endereco endereco = new Endereco();
                                locais.setNome(d.child("nome").getValue(String.class));
                                endereco.setLocal(d.child("endereco_provincia").getValue(String.class));
                                locais.setId(d.getKey());
                                locais.setEndereco(endereco);
                                locaisDoacaoList.add(locais);
                            }
                        } else {
                            progressDialog.dismiss();

                            recyclerView.setVisibility(View.GONE);
                            ll_empty.setVisibility(View.VISIBLE);
                            img_empty_locals.setVisibility(View.VISIBLE);
                            img_empty_locals.setBackgroundResource(R.drawable.ic_empty_box);
                            tv_emptyLocals.setText("Locais não encontrados em " + value);

                        }
                        progressDialog.dismiss();
                        locaisDoacaoAdapter.setOnClickListener(position -> {
                            Intent intent = new Intent(context, DonnateDetailsActivity.class);
                            String id = getSelectedUserId(position);
                            intent.putExtra("hospitalId", id);
                            startActivity(intent);
                        });
                        if (locaisDoacaoAdapter.getItemCount() > 0) {
                            recyclerView.setAdapter(locaisDoacaoAdapter);
                            locaisDoacaoAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // todo: remove on production...
                    }
                });
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(context);
        locaisDoacaoAdapter = new LocaisDoacaoAdapter(getContext(), locaisDoacaoList);
        recyclerView.setLayoutManager(manager);

        if (!spinner_provincias.isSelected() || value.equals("Todas") ) {

            database.child("Colecta_Sanguinea").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    locaisDoacaoList.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            LocaisDoacao locais = new LocaisDoacao();
                            Endereco endereco = new Endereco();
                            locais.setNome(d.child("nome").getValue(String.class));
                            endereco.setLocal(d.child("endereco_provincia").getValue(String.class));
                            locais.setId(d.getKey());
                            locais.setEndereco(endereco);
                            locaisDoacaoList.add(locais);
                        }
                    }
                    progressDialog.dismiss();
                    locaisDoacaoAdapter.setOnClickListener(position -> {
                        Intent intent = new Intent(context, DonnateDetailsActivity.class);
                        String id = getSelectedUserId(position);
                        intent.putExtra("hospitalId", id);
                        startActivity(intent);
                    });
                    if (locaisDoacaoAdapter.getItemCount() > 0) {
                        recyclerView.setAdapter(locaisDoacaoAdapter);
                        locaisDoacaoAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // todo: remove on production...
                }
            });
        }
        return view;
    }

    private String getSelectedUserId(int position) {
        TextView tv = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.tv_hp_id);
        return tv.getText().toString();
    }


    @Override
    public void onStart() {
        super.onStart();

        spinner_provincias.getSelectedItem();
        spinner_provincias.setAdapter(provincesAdapter, -1);
    }

    @Override
    public void onResume() {
        super.onResume();
        spinner_provincias.getSelectedItem();
        spinner_provincias.setAdapter(provincesAdapter, -1);
    }
}