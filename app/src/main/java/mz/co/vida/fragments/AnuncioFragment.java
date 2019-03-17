package mz.co.vida.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.DAO.LogicaAnuncios;
import mz.co.vida.R;
import mz.co.vida.entities.Anuncio;
import mz.co.vida.utils.MyUtils;

public class AnuncioFragment extends Fragment {

    // Components
    private TextView tv_quantidade;
    private TextView tv_data;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private EditText et_descricao;
    private Button bt_update;
    private Button bt_delete;
    private SeekBar seekBar;

    //Utils
    private Context context;
    private int year;
    private int month;
    private int dayOfMonth;
    private int progresso = 0;
    private int quantidade = 0;
    private String mAuth;

    //Init user
    private final Anuncio anuncio = new Anuncio();
    private SweetAlertDialog progressDialog;

    public AnuncioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_anuncio, container, false);

        //Init Utils
        context = getContext();
        LogicaAnuncios logicaAnuncios = new LogicaAnuncios(anuncio, context);

        //Setup firebase
        FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        mAuth = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        //Init Components
        final Button bt_data        = view.findViewById(R.id.btnDate);
        final Button bt_anunciar    = view.findViewById(R.id.btn_anunciar);
        tv_quantidade               = view.findViewById(R.id.tv_quantidade);
        seekBar                     = view.findViewById(R.id.quantidade);
        et_descricao                = view.findViewById(R.id.et_descricao);
        tv_data                     = view.findViewById(R.id.tvSelectedDate);
        bt_delete                   = view.findViewById(R.id.bt_delete);
        bt_update                   = view.findViewById(R.id.bt_update);
        LinearLayout ll_anuncio     = view.findViewById(R.id.ll_anuncio);
        LinearLayout ll_empty       = view.findViewById(R.id.ll_empty_anuncico);
        TextView tv_emptyInfo       = view.findViewById(R.id.tv_emptyInfo_anuncio);
        ImageView image_anuncio     = view.findViewById(R.id.image_anuncio);


        bt_update.setVisibility(View.GONE);
        bt_delete.setVisibility(View.GONE);
        progressDialog              = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#ff1c1c"));
        DatabaseReference database  = ConfiguracaoFirebase.getFirebase();
        tv_quantidade.setText(String.format("Quantidade sanguínea: %d", seekBar.getProgress()));

        if (MyUtils.isConnected(context)) {
            ll_anuncio.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //Retrieving data from firebase
            database.child("anuncios").child(String.valueOf(mAuth))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                anuncio.setDataDoacao(dataSnapshot.child("data").getValue(String.class));
                                anuncio.setQuantSanguinea(dataSnapshot.child("quantSanguinea").getValue(Integer.class));
                                anuncio.setDescricao(dataSnapshot.child("descricao").getValue(String.class));
                                tv_quantidade.setText(String.format("Quantidade sanguínea: %s", String.valueOf(anuncio.getQuantSanguinea())));
                                tv_data.setText(anuncio.getDataDoacao());
                                seekBar.setProgress(anuncio.getQuantSanguinea());
                                et_descricao.setText(anuncio.getDescricao());
                                seekBar.setEnabled(false);
                                tv_data.setEnabled(false);
                                et_descricao.setEnabled(false);
                                bt_data.setEnabled(false);
                                bt_anunciar.setVisibility(View.GONE);
                                bt_update.setVisibility(View.VISIBLE);
                                bt_delete.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            } else {
                                seekBar.setEnabled(true);
                                tv_data.setEnabled(true);
                                et_descricao.setEnabled(true);
                                bt_data.setEnabled(true);
                                bt_anunciar.setVisibility(View.VISIBLE);
                                bt_delete.setVisibility(View.GONE);
                                bt_update.setVisibility(View.GONE);
                                progressDialog.dismiss();
                            }

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        } else {
            ll_anuncio.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
            tv_emptyInfo.setText(R.string.connection_problem);
            image_anuncio.setBackgroundResource(R.drawable.ic_wifi);
            progressDialog.dismiss();
        }

        //update
        bt_update.setOnClickListener(v -> {
            dialog.setTitleText("Alerta!");
            dialog.setContentText("Actualizar a requisição?");
            dialog.setConfirmText(MyUtils.SIM);
            dialog.setConfirmClickListener(sDialog -> {
                //Enable and Disable buttons
                seekBar.setEnabled(true);
                tv_data.setEnabled(true);
                et_descricao.setEnabled(true);
                bt_data.setEnabled(true);
                bt_anunciar.setVisibility(View.VISIBLE);
                bt_delete.setVisibility(View.GONE);
                bt_update.setVisibility(View.GONE);
                sDialog.dismiss();
            }).setCancelButton(MyUtils.NAO, Dialog::dismiss)
                    .show();
        });

        //Setup Date
        bt_data.setOnClickListener(view1 -> {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getContext(),
                    (datePicker, year, month, day) -> tv_data.setText(String.format("%d/%d/%d", day, month + 1, year)), year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresso = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_quantidade.setText(String.format("Quantidade sanguínea: %d", progresso));
                quantidade = progresso;
            }
        });

        bt_anunciar.setOnClickListener(v -> {
            if ((seekBar.getProgress() < 1)) {
                Toast.makeText(getContext(), "Defina a quantidade sanguínea ", Toast.LENGTH_SHORT).show();
                seekBar.requestFocus();
            } else if (tv_data.getText().toString().isEmpty()) {
                MyUtils.alertaNegativa(context, "Defina a data para doação");
                tv_data.requestFocus();
            } else {

                database.child("Usuario").child(mAuth).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dialog.dismiss();
                        if (dataSnapshot.exists()) {
                            anuncio.setNome(dataSnapshot.child("nome").getValue(String.class));
                            anuncio.setProvincia(dataSnapshot.child("provincia").getValue(String.class));
                            anuncio.setEstado(dataSnapshot.child("estado").getValue(String.class));
                            anuncio.setTipo_sangue(dataSnapshot.child("tipo_sangue").getValue(String.class));
                            anuncio.setContacto(dataSnapshot.child("contacto").getValue(String.class));
                            anuncio.setUnidadeProxima(dataSnapshot.child("unidadeProxima").getValue(String.class));
                        }
                        anuncio.setId(mAuth);
                        anuncio.setDescricao(et_descricao.getText().toString());
                        anuncio.setDataDoacao(tv_data.getText().toString());
                        anuncio.setQuantSanguinea(quantidade);
                        logicaAnuncios.gravarAnuncio(context);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });

            }
        });

        bt_delete.setOnClickListener(v -> {
            dialog.setTitleText("Alerta!");
            dialog.setContentText("Apagar o anuncio?");
            dialog.setConfirmText("Sim");
            dialog.setConfirmClickListener(sDialog -> {
                logicaAnuncios.removerAnuncio(mAuth, context);
                clearCampos();
                dialog.dismiss();

            }).setCancelButton(MyUtils.NAO, Dialog::dismiss)
                    .show();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_quantidade.setText(String.format("Quantidade sanguínea: %d", progresso));
    }

    private void clearCampos() {
        seekBar.setProgress(0);
        tv_quantidade.setText(String.format("Quantidade sanguínea: %d", seekBar.getProgress()));
        tv_data.setText("");
        et_descricao.setText("");
    }
}