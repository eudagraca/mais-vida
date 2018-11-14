package mz.co.vida;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import java.util.Calendar;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.entidades.Anuncio;

public class AnuncioFragment extends Fragment {

    private TextView mQuant;
    private TextView tv_data;
    DatePickerDialog datePickerDialog;
    int quantidade;
    private int year;
    private int month;
    private int dayOfMonth;
    private Calendar calendar;
    private EditText mComentario;
    private FirebaseAuth mAuth;
    private SeekBar seekBar;
    //Anuncio anuncio;


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
        mQuant = view.findViewById(R.id.tv_quantidade);
        seekBar = view.findViewById(R.id.quantidade);
        Button mData = view.findViewById(R.id.btnDate);
        Button mBtanunciar = view.findViewById(R.id.btn_anunciar);
        mComentario = view.findViewById(R.id.tid_comentario);

        tv_data = view.findViewById(R.id.tvSelectedDate);
        mQuant.setText("Quantidade sanguínea: "+ seekBar.getProgress());
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
            int progresso = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresso = progress;
                //Toast.makeText(getContext(), "Definindo a quantidade sanguínea", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getContext(), "Progresso Iniciado", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                mQuant.setText("Quantidade sanguínea: "+ progresso);
                quantidade = progresso;
               // Toast.makeText(getContext(), "Progresso Parou", Toast.LENGTH_SHORT).show();
            }
        });

        mBtanunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress()<1 && TextUtils.isEmpty(tv_data.getText().toString())){
                    //Toast.makeText(getContext(), "Defina a quantidade sanguínea ", Toast.LENGTH_SHORT).show();
                    seekBar.requestFocus();
                }
                else{
                Anuncio anuncio =new Anuncio();
                anuncio.setComentario(mComentario.getText().toString());
                anuncio.setDataDoacao(tv_data.getText().toString());
                anuncio.setQuantSanguinea(quantidade);
                anuncio.setId(mAuth.getUid());
                if (anuncio != null){
                anuncio.gravar();
                Toast.makeText(getContext(), "Sucess", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(), "Ocorreu alguma falha", Toast.LENGTH_LONG).show();
                }
                seekBar.setProgress(0);
                tv_data.setText("");
                mComentario.setText("");
            }
            }
        });
     return view;
    }

}
