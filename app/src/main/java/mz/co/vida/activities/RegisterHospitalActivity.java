package mz.co.vida.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import mz.co.vida.DAO.LogicaLocaisDoacao;
import mz.co.vida.R;
import mz.co.vida.entities.Endereco;
import mz.co.vida.entities.LocaisDoacao;
import mz.co.vida.utils.MyUtils;

public class RegisterHospitalActivity extends AppCompatActivity {

    private EditText et_nomeHospital, et_nomeLocal,et_telefone;
    private TextInputEditText et_endereco;
    private TextView tv_dataColeta, tv_horas, tv_dataFim, tv_horasFim;
    private Spinner sp_provinciasHospital;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private Context context;

    private int year;
    private int month;
    private int dayOfMonth;
    private int mHour, mMinute;

    private LocaisDoacao locais = new LocaisDoacao();
    private Endereco endereco = new Endereco();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mz.co.vida.R.layout.activity_register_hospital);

        tv_dataColeta                 = (TextView) findViewById(R.id.et_coleta_data);
        et_nomeHospital               = (EditText) findViewById(R.id.et_hospital_nome_promove);
        et_nomeLocal                  = (EditText) findViewById(R.id.et_nome_local_coleta);
        et_endereco                   = (TextInputEditText) findViewById(R.id.et_endereco_local);
        et_telefone                   = (EditText) findViewById(R.id.et_hospital_Phone);
        tv_horas                      = (TextView) findViewById(R.id.et_hospital_horas);
        tv_dataFim                    = (TextView) findViewById(R.id.et_coleta_data_fim);
        tv_horasFim                   = (TextView) findViewById(R.id.et_hospital_horas_fim);
        sp_provinciasHospital         = (Spinner) findViewById(R.id.sp_provincias_hospital);
        Button btn_resgistar_hospital = (Button) findViewById(mz.co.vida.R.id.btn_resgistar_hospital) ;
        context = this;

        // Launch Date Picker Dialog
        tv_dataColeta.setOnClickListener(view1 -> {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(context,
                    (datePicker, year, month, day) -> tv_dataColeta.setText(String.format("%d/%d/%d", day, month + 1, year)), year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
        tv_dataFim.setOnClickListener(view1 -> {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(context,
                    (datePicker, year, month, day) -> tv_dataFim.setText(String.format("%d/%d/%d", day, month + 1, year)), year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
        // Launch Time Picker Dialog
        tv_horas.setOnClickListener(v -> {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> tv_horas.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
            timePickerDialog.show();
        });

        tv_horasFim.setOnClickListener(v -> {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> tv_horasFim.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
            timePickerDialog.show();
        });

        btn_resgistar_hospital.setOnClickListener(v -> {
            if (isValid()) {
                locais.setNome(et_nomeHospital.getText().toString());
                locais.setTelefone("+258" + et_telefone.getText().toString());
                locais.setDataColeta(tv_dataColeta.getText().toString());
                locais.setHoras(tv_horas.getText().toString());
                locais.setDataFim(tv_dataFim.getText().toString());
                locais.setHorasFim(tv_horasFim.getText().toString());
                endereco.setNome(et_nomeLocal.getText().toString());
                endereco.setLocal(et_endereco.getText().toString());
                endereco.setProvincia(sp_provinciasHospital.getSelectedItem().toString());
                locais.setEndereco(endereco);
                LogicaLocaisDoacao locaisDoacao = new LogicaLocaisDoacao(context, locais, endereco);
                locaisDoacao.gravar();
            }
        });
    }

    public boolean isValid(){
        boolean valid = false;

        if(et_nomeHospital.getText().toString().isEmpty()){
            et_nomeHospital.setError("Nome do hospital vazio");
            et_nomeHospital.requestFocus();

        }else if(!et_nomeHospital.getText().toString().matches("[a-zA-Z àèìòùáéíóúâêîôûãõç]+$")){
            et_nomeHospital.setError("Nome do hospital inválido");
            et_nomeHospital.requestFocus();

        }else if (et_nomeLocal.getText().toString().isEmpty()){
            et_nomeLocal.setError("Local da colecta vazio");
            et_nomeLocal.requestFocus();

        }
        else if (!et_nomeLocal.getText().toString().matches("[a-zA-Z-0-9 àèìòùáéíóúâêîôûãõç]+$")){
            et_nomeLocal.setError("Informe o local da colecta");
            et_nomeLocal.requestFocus();

        }else if (et_endereco.getText().toString().isEmpty()){
            et_endereco.setError("Informe o endereço");
            et_endereco.requestFocus();

        }else if (et_endereco.getText().toString().length()<10){
            et_endereco.setError("Forneça mais detalhes sobre o endereço");
            et_endereco.requestFocus();

        }else if(tv_dataColeta.getText().toString().isEmpty()){
            MyUtils.alertaNegativa(context, "Informe a data inicial");
            tv_dataColeta.requestFocus();

        }else if(tv_dataFim.getText().toString().isEmpty()){
            MyUtils.alertaNegativa(context, "Informe a data limite");
            tv_dataFim.requestFocus();
        }else if (et_telefone.getText().toString().isEmpty()){
            et_telefone.setError("Informe o telefone");
            et_telefone.requestFocus();

        }else if (!et_telefone.getText().toString().matches("((86)|(84)|(87)|(85)|(82))[0-9]{7}")){
            et_telefone.setError("Telefone inválido");
            et_telefone.requestFocus();

        }else if(tv_horas.getText().toString().isEmpty()){
            MyUtils.alertaNegativa(context, "Informe a hora inicial");
            tv_horas.requestFocus();

        }else if(tv_horasFim.getText().toString().isEmpty()){
            MyUtils.alertaNegativa(context, "Informe a hora de enceramento");
            tv_horasFim.requestFocus();

        }else if(sp_provinciasHospital.getSelectedItem().toString().equals("Província")){
            MyUtils.alertaNegativa(context, "Selecione a província");
            sp_provinciasHospital.requestFocus();

        } else if(tv_horas.getText().toString().compareTo(tv_horasFim.getText().toString()) > 0) {

            MyUtils.alertaNegativa(context, "A hora início deve ser menor que a de enceramento ");

        }else if (tv_horas.getText().toString().compareTo(tv_horasFim.getText().toString()) == 0){

            MyUtils.alertaNegativa(context, "As horas devem ser diferentes");

        }else {
            valid= true;
        }

    return valid;
    }
}