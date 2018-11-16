package mz.co.vida.fragments;

import android.app.DatePickerDialog;
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

import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.entities.Anuncio;

public class AnuncioFragment extends Fragment {

    // Components
    private TextView mQuant;
    private TextView tv_data;
    private DatePickerDialog datePickerDialog;
    int quantidade;
    private int year;
    private int month;
    private int dayOfMonth;
    private Calendar calendar;
    private EditText et_descricao;
    private Button btUpdate;
    private Button btDelete;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private SeekBar seekBar;
    int progresso = 0;

    String id;
    String nome;
    String tipo;
    String provincia;
    String estado;

    Anuncio anuncio =new Anuncio();

    public AnuncioFragment() {
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

        final View view = inflater.inflate(R.layout.fragment_anuncio, container, false);

        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        id = mAuth.getCurrentUser().getUid();
        mRef = ConfiguracaoFirebase.getFirebase().child("Usuario").child(id);

        //Init Components

        final Button mData       = view.findViewById(R.id.btnDate);
        final Button mBtanunciar = view.findViewById(R.id.btn_anunciar);
        mAuth                    = ConfiguracaoFirebase.getFirebaseAuth();
        mQuant                   = view.findViewById(R.id.tv_quantidade);
        seekBar                  = view.findViewById(R.id.quantidade);
        et_descricao             = view.findViewById(R.id.et_descricao);
        tv_data                  = view.findViewById(R.id.tvSelectedDate);
        btDelete                 = view.findViewById(R.id.bt_delete);
        btUpdate                 = view.findViewById(R.id.bt_update);
        mQuant.setText(R.string.quant+ seekBar.getProgress());





            DatabaseReference mDataRef = ConfiguracaoFirebase.getFirebase();
            mDataRef.child("anuncios").child(String.valueOf(id))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){
                                anuncio.setDescricao(dataSnapshot.child("descricao").getValue(String.class));
                                anuncio.setDataDoacao(dataSnapshot.child("dataDoacao").getValue(String.class));
                                anuncio.setQuantSanguinea(dataSnapshot.child("quantSanguinea").getValue(Integer.class));


                                mQuant.setText("Quantidade saguínea: "+ String.valueOf(anuncio.getQuantSanguinea()));
                                tv_data.setText(anuncio.getDataDoacao());
                                et_descricao.setText(anuncio.getDescricao());
                                seekBar.setProgress(anuncio.getQuantSanguinea());

                                seekBar.setEnabled(false);
                                tv_data.setEnabled(false);
                                et_descricao.setEnabled(false);
                                mData.setEnabled(false);
                                mBtanunciar.setVisibility(View.GONE);

                            }else {

                                seekBar.setEnabled(true);
                                tv_data.setEnabled(true);
                                et_descricao.setEnabled(true);
                                mData.setEnabled(true);
                                mBtanunciar.setVisibility(View.VISIBLE);
                                btDelete.setVisibility(View.GONE);
                                btUpdate.setVisibility(View.GONE);

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                nome   = dataSnapshot.child("nome").getValue(String.class);
                provincia = dataSnapshot.child("provincia").getValue(String.class);
                estado = dataSnapshot.child("estado").getValue(String.class);
                tipo   = dataSnapshot.child("tipoSanguineo").getValue(String.class);

                anuncio.setNome(nome);
                anuncio.setProvincia(provincia);
                anuncio.setTipo_sangue(tipo);
                anuncio.setEstado(estado);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Setup Date
        mData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_data.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
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
                mQuant.setText("Quantidade sanguínea: "+ progresso);
                quantidade = progresso;
               }
        });



        mBtanunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    anuncio.setDescricao(et_descricao.getText().toString());
                    anuncio.setDataDoacao(tv_data.getText().toString());
                    anuncio.setQuantSanguinea(quantidade);
                    anuncio.setId(mAuth.getUid());
                    anuncio.gravar();
                    Toast.makeText(getContext(), "Requisição anunciada", Toast.LENGTH_LONG).show();
                    seekBar.setProgress(0);
                    mQuant.setText(R.string.quant+ seekBar.getProgress());
                    tv_data.setText("");
                    et_descricao.setText("");

            }
            }
        });
     return view;
    }





}
