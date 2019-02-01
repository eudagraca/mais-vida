package mz.co.vida.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

    // Firebase
    private FirebaseAuth mAuth;

    private int progresso = 0;
    private int quantidade=0;
    private String id;
    private String nome;
    private String tipo;
    private String provincia;
    private String estado;
    private String contacto;

    private Anuncio anuncio =new Anuncio();

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

        //Setup firebase
        mAuth                               = ConfiguracaoFirebase.getFirebaseAuth();
        id                                  = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference mDatabase_usuario = ConfiguracaoFirebase.getFirebase().child("Usuario").child(id);
        mAuth                               = ConfiguracaoFirebase.getFirebaseAuth();
        final SweetAlertDialog dialog       = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);


        //Init Components

        final Button bt_data     = view.findViewById(R.id.btnDate);
        final Button bt_anunciar = view.findViewById(R.id.btn_anunciar);
        tv_quantidade            = view.findViewById(R.id.tv_quantidade);
        seekBar                  = view.findViewById(R.id.quantidade);
        et_descricao             = view.findViewById(R.id.et_descricao);
        tv_data                  = view.findViewById(R.id.tvSelectedDate);
        bt_delete                = view.findViewById(R.id.bt_delete);
        bt_update                = view.findViewById(R.id.bt_update);



        tv_quantidade.setText(R.string.quant+ seekBar.getProgress());

            DatabaseReference mDatabase_anuncios = ConfiguracaoFirebase.getFirebase();
            mDatabase_anuncios.child("anuncios").child(String.valueOf(id))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){
                                anuncio.setDataDoacao(dataSnapshot.child("data").getValue(String.class));
                                anuncio.setQuantSanguinea(dataSnapshot.child("quantSanguinea").getValue(Integer.class));
                                anuncio.setDescricao(dataSnapshot.child("descricao").getValue(String.class));

                                tv_quantidade.setText("Quantidade saguínea: "+ String.valueOf(anuncio.getQuantSanguinea()));
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

                            }else {

                                seekBar.setEnabled(true);
                                tv_data.setEnabled(true);
                                et_descricao.setEnabled(true);
                                bt_data.setEnabled(true);
                                bt_anunciar.setVisibility(View.VISIBLE);
                                bt_delete.setVisibility(View.GONE);
                                bt_update.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            //Update
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
                }).setCancelButton(MyUtils.NAO, sDialog -> sDialog.dismiss())
                        .show();
            });

                mDatabase_usuario.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        nome = dataSnapshot.child("nome").getValue(String.class);
                        provincia = dataSnapshot.child("provincia").getValue(String.class);
                        estado = dataSnapshot.child("estado").getValue(String.class);
                        tipo = dataSnapshot.child("tipo_sangue").getValue(String.class);
                        contacto = dataSnapshot.child("contacto").getValue(String.class);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        //Setup Date
        bt_data.setOnClickListener(view1 -> {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getContext(),
                    (datePicker, year, month, day) -> tv_data.setText(day + "/" + (month + 1) + "/" + year), year, month, dayOfMonth);
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
                tv_quantidade.setText("Quantidade sanguínea: "+ progresso);
                quantidade = progresso;
               }
        });

        bt_anunciar.setOnClickListener(v -> {
            if ((seekBar.getProgress()==0)){
                Toast.makeText(getContext(), "Defina a quantidade sanguínea ", Toast.LENGTH_SHORT).show();
                seekBar.requestFocus();
            }
            else if (tv_data.getText().toString().isEmpty()){
                tv_data.setError("Defina a data para doacao");
                Toast.makeText(getContext(), "Defina a data para doacao", Toast.LENGTH_SHORT).show();
                tv_data.requestFocus();
            }
            else{
                anuncio.setId(mAuth.getUid());
                anuncio.setDescricao(et_descricao.getText().toString());
                anuncio.setDataDoacao(tv_data.getText().toString());
                anuncio.setQuantSanguinea(quantidade);
                anuncio.setTipo_sangue(tipo);
                anuncio.setNome(nome);
                anuncio.setProvincia(provincia);
                anuncio.setEstado(estado);
                anuncio.setContacto(contacto);
                if (anuncio.gravar()) {
                    MyUtils.alertaPosetiva(context, "Requisição anunciada");
                    clearCampos();
                }else {
                    MyUtils.alertaPosetiva(context, "Falha ao anunciar a requisição");
                }
        }
        });

        bt_delete.setOnClickListener(v -> {
            dialog.setTitleText("Alerta!");
            dialog.setContentText("Apagar o anuncio?");
            dialog.setConfirmText("Sim");
            dialog.setConfirmClickListener(sDialog -> {
                DatabaseReference mData =  ConfiguracaoFirebase.getFirebase().child("anuncios").child(id);
                if(mData.removeValue().isSuccessful()){
                clearCampos();
                sDialog.dismiss();

                MyUtils.alertaPosetiva(context, "Requisição apagada");
                }else {
                    MyUtils.alertaNegativa(context,"Falha ao apagar a requisicao!!");
                }
            })
                    .setCancelButton(MyUtils.NAO, sDialog -> sDialog.dismiss())
                    .show();
        });

     return view;
    }

    private void clearCampos() {
        seekBar.setProgress(0);
        tv_quantidade.setText(R.string.quant+ seekBar.getProgress());
        tv_data.setText("");
        et_descricao.setText("");
    }
}
