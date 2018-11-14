package mz.co.vida.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.robertlevonyan.views.chip.Chip;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.MyUtils;
import mz.co.vida.NotificationAdapter;
import mz.co.vida.PostAdapter;
import mz.co.vida.PostDetailsActivity;
import mz.co.vida.R;
import mz.co.vida.entidades.Notification;


public class NotificationFragment extends Fragment {

    MaterialLetterIcon ml_tipoSangue;
    TextView tv_nome;
    TextView tv_data;
    TextView tv_descricao;

    // Components
    RecyclerView rv_notif;
    // Vars
    List<Notification> notificationList;
    // Utils
    NotificationAdapter notificationAdapter;
    private SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    int fl_framelayout;


    public NotificationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fl_framelayout = R.id.fl_framelayout;
        fragmentManager = getFragmentManager();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        ml_tipoSangue = view.findViewById(R.id.mL_tipoS);
        tv_descricao     = view.findViewById(R.id.actv_status_desc);
        tv_nome       = view.findViewById(R.id.tv_nomeN);
        rv_notif      = view.findViewById(R.id.rv_announce);
        rv_notif.setHasFixedSize(true);

        notificationList = new ArrayList<>();

        // Setup RecyclerView
        if (getActivity() != null){
            rv_notif.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false)); // set the list layout style to vertical.
            notificationAdapter = new NotificationAdapter(getActivity(), notificationList);
            rv_notif.setAdapter(notificationAdapter);
        }

        DatabaseReference mdataRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference userAnuncio = mdataRef.child("anuncios").child("");
        userAnuncio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        notificationAdapter.setOnClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


            }
        });
        return view;
    }

    public String getSelectedUserId(int position, int viewRes){
        TextView tv = Objects.requireNonNull(rv_notif.findViewHolderForAdapterPosition(position)).itemView.findViewById(viewRes);
        return tv.getText().toString();
    }

    public String getSelectedStatus(int position, int viewRes){
        Chip chip = Objects.requireNonNull(rv_notif.findViewHolderForAdapterPosition(position)).itemView.findViewById(viewRes);
        return chip.getChipText();
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }

}