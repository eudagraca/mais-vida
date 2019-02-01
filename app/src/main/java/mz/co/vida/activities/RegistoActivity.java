package mz.co.vida.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import lib.kingja.switchbutton.SwitchMultiButton;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.entities.Usuario;
import mz.co.vida.helpers.Preferencias;
import mz.co.vida.utils.MyUtils;
import mz.co.vida.utils.VerificaConexao;


public class RegistoActivity extends AppCompatActivity {


    //Utils
    private final static int PICK_PHOTO_CODE = 1046;
    private Uri image_uri;
    private String img;
    private Context ctx;

    //Components
    private EditText et_nome, et_email, et_senha,et_senha_conf, et_telefone, et_unidade_proxima;
    private RadioButton rb_femenino, rb_masculino, rb_outro;
    private Switch sw_disponibilidade;
    private SwitchMultiButton smb_estado;
    private CircleImageView civ_foto;
    private Spinner sp_provincias;
    private Button btn_finalizar_registo;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser fire_user;
    private Uri imageUri;
    private StorageReference storageReference;
    private StorageReference image_reference;
    private Usuario usuario = new Usuario();

    public RegistoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        //init components
        Button bt_back                    = (Button) findViewById(R.id.voltar_login);
        et_nome                           = (EditText) findViewById(R.id.et_nome);
        et_email                          = (EditText) findViewById(R.id.et_mail);
        et_senha                          = (EditText) findViewById(R.id.et_password);
        et_senha_conf                     = (EditText) findViewById(R.id.et_passwordConfirm);
        et_telefone                       = (EditText) findViewById(R.id.et_Phone);
        et_unidade_proxima                = (EditText) findViewById(R.id.et_UnidadeProx);
        sw_disponibilidade                = (Switch) findViewById(R.id.stc_disponibilidade);
        rb_femenino                       = (RadioButton) findViewById(R.id.rb_femenino);
        rb_masculino                      = (RadioButton) findViewById(R.id.rb_masculino);
        rb_outro                          = (RadioButton) findViewById(R.id.rb_outro);
        civ_foto                          = (CircleImageView) findViewById(R.id.profile_image);
        sp_provincias                     = (Spinner) findViewById(R.id.sp_provincias);
        smb_estado                        = (SwitchMultiButton) findViewById(R.id.switch_mult_estado);
        SwitchMultiButton smb_tipo_sangue = (SwitchMultiButton) findViewById(R.id.bt_MultiSangue);
        btn_finalizar_registo             = (Button) findViewById(R.id.btn_resgistar);

        //setup utils
        ctx = this;
        storageReference = FirebaseStorage.getInstance().getReference();

        bt_back.setOnClickListener(v -> onBackPressed());

        //Setup provincias adapter
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.provincias,
                android.R.layout.simple_spinner_item);
        sp_provincias.setAdapter(adapter);

        smb_estado.setOnSwitchListener((position, tabText) -> Toast.makeText(RegistoActivity.this, tabText, Toast.LENGTH_SHORT).show());

        smb_tipo_sangue.setOnSwitchListener((position, tabText) -> Toast.makeText(RegistoActivity.this, tabText, Toast.LENGTH_SHORT).show());


        civ_foto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, PICK_PHOTO_CODE);
        });

        //Tipo sanguíneo
        switch (String.valueOf(smb_tipo_sangue.getSelectedTab())) {
            case "0":
                usuario.setTipo_sangue("A+");
                break;
            case "1":
                usuario.setTipo_sangue("B+");
                break;
            case "2":
                usuario.setTipo_sangue("AB+");
                break;
            case "3":
                usuario.setTipo_sangue("O+");
                break;
            case "4":
                usuario.setTipo_sangue("A-");
                break;
            case "5":
                usuario.setTipo_sangue("B-");
                break;
            case "6":
                usuario.setTipo_sangue("AB-");
                break;
            case "7":
                usuario.setTipo_sangue("O-");
                break;
            default:
                smb_tipo_sangue.requestFocus();
                break;
        }
         validarRegisto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                civ_foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfileImage() {
        if (imageUri != null) {
            image_reference = storageReference.child("imagens/").child("perfil" + UUID.randomUUID().toString());
            UploadTask uploadTask = image_reference.putFile(imageUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return image_reference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    image_uri = task.getResult();
                    assert image_uri != null;
                    img = image_uri.toString();
                    DatabaseReference mDatabase= ConfiguracaoFirebase.getFirebase();
                    mDatabase.child("Usuario").child(fire_user.getUid())
                            .child("foto")
                            .setValue(img);
                }
            });

        }
    }

    private void create_user() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#ff1c1c"));
        pDialog.setTitleText("Aguarde...");
        pDialog.setCancelable(false);
        pDialog.show();

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(String.valueOf(MyUtils.CONTADOR), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Contador", 1);
        editor.apply();

        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        //Setup dialog progress
        mAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(RegistoActivity.this, task -> {

            if (task.isSuccessful()) {
                fire_user =  mAuth.getCurrentUser();
                if (fire_user != null) {
                    usuario.setUidUser(fire_user.getUid());
                    fire_user.sendEmailVerification();
                    usuario.gravar();
                    uploadProfileImage();

                }
                Preferencias preferencias = new Preferencias(RegistoActivity.this);
                preferencias.gravarUsuario(fire_user.getUid(), usuario.getNome());
                abrirLogin();


            } else {
                pDialog.dismiss();
                String erro;
                try {
                    throw Objects.requireNonNull(task.getException());
                //} catch (FirebaseAuthWeakPasswordException ex) {
                //    erro = "Digite uma Senha mais forte, contendo no mínimo 8 caracteres de letras e números";
                //} catch (FirebaseAuthInvalidCredentialsException ex) {
                //    erro = "O e-mail digitado não é válido";
                } catch (FirebaseAuthUserCollisionException ex) {
                    erro = "Este e-mail já possui um registo";
                //}catch (FirebaseNetworkException ex){
                //    erro = "Problemas de conexao";
                } catch (Exception ex) {
                    erro = "Erro desconhecido";
                    ex.printStackTrace();
                }

                MyUtils.alertaNegativa(ctx, erro);


            }
        });
    }

    private void validarRegisto() {

        btn_finalizar_registo.setOnClickListener(v -> {

            //check if there is an internet connection
            if (VerificaConexao.isConnected(ctx)){
                //Not null
                if (TextUtils.isEmpty(et_nome.getText().toString()) || TextUtils.isEmpty(et_telefone.getText().toString())
                        || TextUtils.isEmpty(et_senha.getText().toString()) || TextUtils.isEmpty(et_senha_conf.getText().toString())
                        || TextUtils.isEmpty(et_email.getText().toString()) || TextUtils.isEmpty(et_unidade_proxima.getText().toString())) {
                    et_nome.requestFocus();
                    Toast.makeText(ctx, "Preencha os espaços em branco", Toast.LENGTH_SHORT).show();
                }else {
                    //Estado
                    switch (String.valueOf(smb_estado.getSelectedTab())) {
                        case "0":
                            usuario.setEstado(MyUtils.DOADOR);
                            if (sw_disponibilidade.isChecked()) {
                                usuario.setDisponibilidade("Sim");
                            } else {
                                usuario.setDisponibilidade("Não");
                            }
                            break;
                        case "1":
                            usuario.setEstado(MyUtils.REQUISITANTE);
                            sw_disponibilidade.setVisibility(View.GONE);
                            usuario.setDisponibilidade(null);
                            break;
                        case "-1":
                            Toast.makeText(ctx, "Seleccione o teu estado", Toast.LENGTH_SHORT).show();
                            break;
                    }

                    if (et_unidade_proxima.getText().toString().isEmpty()){
                        et_unidade_proxima.setError("Informe a sua unidade mais próxima");
                        et_unidade_proxima.requestFocus();
                    }

                    if (et_nome.getText().toString().length() < 3) {
                        et_nome.setError("Nome Inválido");
                        et_nome.requestFocus();
                    }

                    if (et_telefone.getText().length() < 9) {
                        et_telefone.setError("Digite um número válido");
                        et_telefone.requestFocus();
                    }

                    if (!et_email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                        et_email.setError("Endereço de email inválido");
                        et_email.requestFocus();

                    }

                    if (et_senha.getText().toString().length() < 8) {
                        et_senha.setError("No mínimo 8 digitos");
                        et_senha.requestFocus();
                    }
                    else if (!TextUtils.equals(et_senha.getText().toString().trim(), et_senha_conf.getText().toString().trim())) {
                        et_senha_conf.setError("Senhas incompatíveis");
                        et_senha_conf.requestFocus();
                        et_senha.requestFocus();

                    }

                    if (sp_provincias.getSelectedItem().equals("Províncias")) {
                        TextView sp = (TextView) sp_provincias.getSelectedView();
                        sp.setError("Seleccione uma província");
                        sp.setTextColor(Color.RED);
                        sp.requestFocus();
                    }

                    //Sexo
                    if (rb_femenino.isChecked()) {
                        usuario.setSexo("Femenino");
                    } else if (rb_masculino.isChecked()) {
                        usuario.setSexo("Masculino");
                    } else if (rb_outro.isChecked()) {
                        usuario.setSexo("Outro");
                    } else {
                        Toast.makeText(ctx, "Seleccione o sexo", Toast.LENGTH_SHORT).show();

                    }

                    usuario.setNome(et_nome.getText().toString());
                    usuario.setEmail(et_email.getText().toString().trim().toLowerCase());
                    usuario.setContacto("+258"+et_telefone.getText().toString());
                    usuario.setSenha(et_senha.getText().toString());
                    usuario.setUnidadeProxima(et_unidade_proxima.getText().toString());
                    usuario.setProvincia(sp_provincias.getSelectedItem().toString());
                    create_user();
                }
            }else {
                MyUtils.alertaNegativa(ctx, "Liga-te à internet");
            }
        });
    }

    private void abrirLogin() {
        Intent intent = new Intent(ctx, LoginActivity.class);
        startActivity(intent);
        new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Conta criada")
                .setContentText("Consulte a sua caixa de email para confirmar o seu registo!")
                .show();



    }


}