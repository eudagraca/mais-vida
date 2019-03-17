package mz.co.vida.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.github.angads25.toggle.widget.LabeledSwitch;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import lib.kingja.switchbutton.SwitchMultiButton;
import mz.co.vida.DAO.LogicaUsuario;
import mz.co.vida.R;
import mz.co.vida.entities.Usuario;
import mz.co.vida.utils.MyUtils;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;


public class RegistoActivity extends AppCompatActivity {

    //Utils
    private final static int PICK_PHOTO_CODE = 1046;
    private Context ctx;
    private AwesomeValidation mAwesomeValidation;
    private Usuario usuario;
    private LogicaUsuario logicaUsuario;

    //Components
    private EditText et_nome;
    private EditText et_email;
    private EditText et_senha, et_confirmar;
    private EditText et_telefone;
    private EditText et_unidade_proxima;
    private LabeledSwitch lbl_disponibilidade;
    private CircleImageView civ_foto;
    private Spinner sp_provincias;
    private RadioButton radio_masculino, radio_femenino, radio_outro;
    private Button btn_finalizar_registo;
    private SwitchMultiButton smb_tipo_sangue;

    public RegistoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        mAwesomeValidation = new AwesomeValidation(UNDERLABEL);
        usuario = new Usuario();
        //init components
        Button bt_back                     = (Button) findViewById(R.id.voltar_login);
        et_nome                            = (EditText) findViewById(R.id.et_nome);
        et_email                           = (EditText) findViewById(R.id.et_mail);
        et_senha                           = (EditText) findViewById(R.id.et_password);
        et_telefone                        = (EditText) findViewById(R.id.et_Phone);
        et_unidade_proxima                 = (EditText) findViewById(R.id.et_UnidadeProx);
        et_confirmar                       = (EditText) findViewById(R.id.et_passwordConfirm);
        lbl_disponibilidade                = (LabeledSwitch) findViewById(R.id.lbl_disponibilidade);
        civ_foto                           = (CircleImageView) findViewById(R.id.profile_image);
        sp_provincias                      = (Spinner) findViewById(R.id.sp_provincias);
        radio_femenino                     = (RadioButton) findViewById(R.id.rb_femenino);
        radio_masculino                    = (RadioButton) findViewById(R.id.rb_masculino);
        radio_outro                        = (RadioButton) findViewById(R.id.rb_outro);
        SwitchMultiButton smb_estado       = (SwitchMultiButton) findViewById(R.id.switch_mult_estado);
        smb_tipo_sangue                    = (SwitchMultiButton) findViewById(R.id.bt_MultiSangue);
        btn_finalizar_registo              = (Button) findViewById(R.id.btn_resgistar);
        //init utils
        ctx = this;
        logicaUsuario   =  new LogicaUsuario(usuario, ctx);
        mAwesomeValidation.setContext(ctx);
        addValidationToViews();


        bt_back.setOnClickListener(v -> onBackPressed());

        //Setup provincias adapter
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.provincias,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_text_drop);
        sp_provincias.setAdapter(adapter);

        smb_estado.setOnSwitchListener((position, tabText) -> Toast.makeText(RegistoActivity.this, tabText, Toast.LENGTH_SHORT).show());

        smb_tipo_sangue.setOnSwitchListener((position, tabText) -> Toast.makeText(RegistoActivity.this, tabText, Toast.LENGTH_SHORT).show());

        civ_foto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, PICK_PHOTO_CODE);
        });
    }

    public boolean isValid(){
        boolean valid = false;

        if (!et_nome.getText().toString().matches("[a-zA-Z àèìòùáéíóúâêîôûãõç]+$")){
            et_nome.setError("Nome inválido");
            et_nome.requestFocus();
        }else if (!et_telefone.getText().toString().matches("((86)|(84)|(87)|(85)|(82))[0-9]{7}")){
            et_telefone.setError("Telefone inválido");
            et_telefone.requestFocus();
        }else if(!MyUtils.isValidEmailAddressRegex(et_email.getText().toString())){
            et_email.setError("Email inválido");
            et_email.requestFocus();
        }else if(et_senha.getText().toString().isEmpty()){
                et_senha.setError(" Defina uma senha");
                et_senha.requestFocus();
        } else if (!et_senha.getText().toString().matches("[a-zA-Z0-9_-]+$")){
            et_senha.setError("Senha inválida");
            et_senha.requestFocus();
        }else if (et_senha.getText().toString().length() < 6) {
            et_senha.setError("A senha muito curta");
            et_senha.requestFocus();
        }else if (!et_senha.getText().toString().equals(et_confirmar.getText().toString())){
            et_confirmar.setError("Senhas incopatíveis");
            et_confirmar.requestFocus();
        }else if(sp_provincias.getSelectedItem().toString().equals("Província")){
            MyUtils.alertaNegativa(this, "Seleccione a sua província");
        }else if(!sexoIsValid()){
            MyUtils.alertaNegativa(ctx, "Seleccione o sexo");
        }else if(!sangue()){
            MyUtils.alertaNegativa(ctx, "Seleccione o seu tipo  sanguíneo");
        }else if (et_unidade_proxima.getText().toString().isEmpty()){
            et_unidade_proxima.setError("Sua unidade mais próxima");
            et_unidade_proxima.requestFocus();
        }
        else {
            valid = true;
        }
        return valid;
    }

    private boolean sangue(){
        switch (smb_tipo_sangue.getSelectedTab()) {
            case 0:
                usuario.setTipo_sangue("A+");
                return true;
            case 1:
                usuario.setTipo_sangue("B+");
                return true;
            case 2:
                usuario.setTipo_sangue("AB+");
                return true;
            case 3:
                usuario.setTipo_sangue("O+");
                return true;
            case 4:
                usuario.setTipo_sangue("A-");
                return true;
            case 5:
                usuario.setTipo_sangue("B-");
                return true;
            case 6:
                usuario.setTipo_sangue("AB-");
                return true;
            case 7:
                usuario.setTipo_sangue("O-");
                return true;
            default:
                return false;
        }
    }

    private boolean sexoIsValid(){

        if (radio_femenino.isChecked()){
            usuario.setSexo("Femenino");
            return true;
        }else if (radio_masculino.isChecked()){
            usuario.setSexo("Masculino");
            return true;
        }else if (radio_outro.isChecked()){
            usuario.setSexo("Outro");
            return true;
        }else {
            return false;
        }
    }
    private void addValidationToViews() {

        mAwesomeValidation.addValidation(this, R.id.switch_mult_estado, validationHolder -> {
            if (lbl_disponibilidade.isOn() && ((SwitchMultiButton) validationHolder.getView()).getSelectedTab() == 0) {
                usuario.setDisponibilidade(MyUtils.SIM);
                usuario.setEstado(MyUtils.DOADOR);
                return true;
            } else if (!lbl_disponibilidade.isOn() && ((SwitchMultiButton) validationHolder.getView()).getSelectedTab() == 0) {
                usuario.setDisponibilidade(MyUtils.NAO);
                usuario.setEstado(MyUtils.DOADOR);
                return true;
            } else if (((SwitchMultiButton) validationHolder.getView()).getSelectedTab() == 1) {
                usuario.setDisponibilidade(null);
                usuario.setEstado(MyUtils.REQUISITANTE);
                return true;
            } else {
                MyUtils.alertaNegativa(ctx, "Seleccione tipo de usuário DOADOR / REQUISITANTE");
                return false;
            }
        }, validationHolder -> {

        }, validationHolder -> {

        }, R.string.wrong_disp);


        //Register
        btn_finalizar_registo.setOnClickListener(v -> {


            if (isValid()){

                if (mAwesomeValidation.validate()) {
                    usuario.setNome(et_nome.getText().toString());
                    usuario.setEmail(et_email.getText().toString().trim().toLowerCase());
                    usuario.setContacto("+258" + et_telefone.getText().toString().trim());
                    usuario.setSenha(et_senha.getText().toString());
                    usuario.setUnidadeProxima(et_unidade_proxima.getText().toString());
                    usuario.setProvincia(sp_provincias.getSelectedItem().toString().trim());
                    logicaUsuario.create_user(ctx);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            usuario.setFoto(Objects.requireNonNull(imageUri).toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                civ_foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}