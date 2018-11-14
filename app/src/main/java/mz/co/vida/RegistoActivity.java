package mz.co.vida;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.Helper.Preferencias;
import de.hdodenhof.circleimageview.CircleImageView;
import lib.kingja.switchbutton.SwitchMultiButton;
import mz.co.vida.entidades.Usuario;

public class RegistoActivity extends AppCompatActivity {
    private EditText mNome;
    private EditText mEmail;
    private SwitchMultiButton bt_estado;
    private EditText mSenha;
    private EditText mSenhaConf;
    private RadioButton rbFemenino;
    private RadioButton rbMasculino;
    private RadioButton rbOutro;
    private EditText mTelefone;
    private RadioGroup mSexo;
    private Switch mDisponibilidade;
    private EditText mUnidade_Proxima;
    private SwitchMultiButton bt_TipoSangue;
    private CircleImageView imageProfile;
    private Usuario usuario;
    public final static int PICK_PHOTO_CODE = 1046;
    private static final String TAG = "RegisterActivity";
    private Uri imageUri;
    //Firebase
    private FirebaseAuth mAuth;

    private FirebaseUser fireUser;
    private Uri mainImageURI;
    private StorageReference storageref;
    private StorageReference imageRef;

    // 1. Instantiate an AlertDialog.Builder with its constructor
    //AlertDialog.Builder builder = new AlertDialog.Builder(RegistoActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        Button back = (Button) findViewById(R.id.voltar_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLogin();
            }
        });
        storageref = FirebaseStorage.getInstance().getReference();
        final Spinner provincias = (Spinner) findViewById(R.id.sp_provincias);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.provincias,
                android.R.layout.simple_spinner_item);

        provincias.setAdapter(adapter);

        bt_estado = (SwitchMultiButton) findViewById(R.id.switch_mult_estado);

        bt_estado.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {

                Toast.makeText(RegistoActivity.this, tabText, Toast.LENGTH_SHORT).show();
            }
        });

        bt_TipoSangue = (SwitchMultiButton) findViewById(R.id.bt_MultiSangue);
        bt_TipoSangue.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                Toast.makeText(RegistoActivity.this, tabText, Toast.LENGTH_SHORT).show();
            }
        });
        //Init Components
        mNome                   = (EditText) findViewById(R.id.et_nome);
        mEmail                  = (EditText) findViewById(R.id.et_mail);
        mSenha                  = (EditText) findViewById(R.id.et_password);
        mSenhaConf              = (EditText) findViewById(R.id.et_passwordConfirm);
        mTelefone               = (EditText) findViewById(R.id.et_Phone);
        mUnidade_Proxima        = (EditText) findViewById(R.id.et_UnidadeProx);
        mDisponibilidade        = (Switch) findViewById(R.id.stc_disponibilidade);
        rbFemenino              = (RadioButton) findViewById(R.id.rbFemenino);
        rbMasculino             = (RadioButton) findViewById(R.id.rbMasculino);
        rbOutro                 = (RadioButton) findViewById(R.id.rbOutro);
        mSexo                   = (RadioGroup) findViewById(R.id.rg);
        Button btn_finalizarRegisto = (Button) findViewById(R.id.btn_resgistar);
        imageProfile = (CircleImageView) findViewById(R.id.profile_image);

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, PICK_PHOTO_CODE);
            }
        });

        btn_finalizarRegisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSenha.getText().toString().equals(mSenhaConf.getText().toString()) && !TextUtils.isEmpty(mNome.getText().toString())
                        && !TextUtils.isEmpty(mEmail.getText().toString()) && !TextUtils.isEmpty(mTelefone.getText().toString())
                        && !TextUtils.isEmpty(mUnidade_Proxima.getText())) {

                    usuario = new Usuario();
                    usuario.setNome(mNome.getText().toString());
                    usuario.setEmail(mEmail.getText().toString().trim().toLowerCase());
                    usuario.setTelefone(mTelefone.getText().toString());
                    usuario.setSenha(mSenha.getText().toString());
                    usuario.setUnidadeProxima(mUnidade_Proxima.getText().toString());

                    if (provincias.getSelectedItem().equals("Província")){
                        Toast.makeText(RegistoActivity.this, "Seleccione uma província válida", Toast.LENGTH_SHORT).show();
                    }else {
                        usuario.setProvincia(provincias.getSelectedItem().toString());
                    }
                    Log.i(TAG, "INFO " +usuario.getProvincia());
                    //Estado

                    if (String.valueOf(bt_estado.getSelectedTab()).equals("0")) {
                        usuario.setEstado("Doador");
                        if (mDisponibilidade.isChecked()) {
                            mDisponibilidade.setVisibility(View.VISIBLE);
                            usuario.setDisponibilidade("Sim");
                        } else{
                            usuario.setDisponibilidade("Não");
                        }
                    } else if (String.valueOf(bt_estado.getSelectedTab()).equals("1")) {
                        mDisponibilidade.setVisibility(View.GONE);
                        usuario.setEstado("Requistante");
                        mDisponibilidade.setEnabled(false);

                        usuario.setDisponibilidade(null);
                    }
                    if (!(mSenha.getText().toString().equals(mSenhaConf.getText().toString()))) {
                        Toast.makeText(RegistoActivity.this, "Senhas incopativeis", Toast.LENGTH_SHORT).show();
                    } else if (mNome.getText().toString().isEmpty()) {
                        mNome.setError("Nome Inválido");
                        mNome.requestFocus();
                    } else if (mTelefone.getText().length() < 9) {
                        mTelefone.setError("Telefone inválido");
                        mTelefone.requestFocus();
                    } else if (mEmail.getText().toString().isEmpty()) {
                        mEmail.setError("Digite o email");
                        mEmail.requestFocus();
                    } else if (mSenha.getText().toString().isEmpty()) {
                        mSenha.setError("Digite o email");
                        mSenha.requestFocus();
                    } else if (mSenhaConf.getText().length() < 1) {
                        mSenhaConf.setError("Repita a senha");
                        mSenhaConf.requestFocus();
                    }
                    //Sexo
                    if (rbMasculino.isChecked()) {
                        usuario.setSexo("Femenino");
                    } else if (rbFemenino.isChecked()) {
                        usuario.setSexo("Masculino");
                    } else if (rbOutro.isChecked()) {
                        usuario.setSexo("Outro");
                    } else {
                        mSexo.requestFocus();
                        Toast.makeText(RegistoActivity.this, "Seleccione o sexo", Toast.LENGTH_LONG).show();
                    }
                    //Tipo sanguíneo
                    switch (String.valueOf(bt_TipoSangue.getSelectedTab())) {
                        case "0":
                            usuario.setTipoSanguineo("A+");
                            break;
                        case "1":
                            usuario.setTipoSanguineo("B+");
                            break;
                        case "2":
                            usuario.setTipoSanguineo("AB+");
                            break;
                        case "3":
                            usuario.setTipoSanguineo("O+");
                            break;
                        case "4":
                            usuario.setTipoSanguineo("A-");
                            break;
                        case "5":
                            usuario.setTipoSanguineo("B-");
                            break;
                        case "6":
                            usuario.setTipoSanguineo("AB-");
                            break;
                        default:
                            usuario.setTipoSanguineo("O-");
                            break;
                    }

                    uploadProfileImage();
                    registarUser();

                } else {
                    Toast.makeText(RegistoActivity.this, "Preencha os espaços", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_CODE && resultCode == RESULT_OK) {
            mainImageURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mainImageURI);
                imageProfile.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfileImage() {
        if (mainImageURI != null) {
            imageRef = storageref.child("imagens/").child("perfil" + UUID.randomUUID().toString());
            //final ProgressDialog progressDialog = new ProgressDialog(this);
            UploadTask uploadTask = imageRef.putFile(mainImageURI);
            //Log.i(TAG, "Referencia" + mainImageURI);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        imageUri = task.getResult();
                        Log.i(TAG, "MSG  "+ imageUri);
                        usuario.setFoto(imageUri.toString());

                    }else {
                        Log.i(TAG, "EROOOOOO!!!!!!!");
                    }
                }
            });

        }
    }

    private void registarUser() {
            mAuth = ConfiguracaoFirebase.getFirebaseAuth();
            final ProgressDialog proDialog = new ProgressDialog(RegistoActivity.this);
            proDialog.setTitle("Criando usuario aguarde...");
            proDialog.show();

            mAuth.createUserWithEmailAndPassword(
                    usuario.getEmail(),
                    usuario.getSenha()
            ).addOnCompleteListener(RegistoActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(RegistoActivity.this, R.string.email_verification, Toast.LENGTH_LONG).show();
                        //String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                        fireUser = mAuth.getCurrentUser();
                        if (fireUser != null) {
                            usuario.setUidUser(fireUser.getUid());
                            fireUser.sendEmailVerification();
                            usuario.gravar();
                        }

                        proDialog.dismiss();
                        Preferencias preferencias = new Preferencias(RegistoActivity.this);
                        preferencias.gravarUsuario(fireUser.getUid(), usuario.getNome());
                        abrirLogin();
                    } else {
                        proDialog.dismiss();
                        String erro;
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException ex) {

                            erro = "Digite uma Senha mais forte, contendo no mínimo 8 caracteres de letras e números";
                        } catch (FirebaseAuthInvalidCredentialsException ex) {

                            erro = "O e-mail digitado não é válido";
                        } catch (FirebaseAuthUserCollisionException ex) {

                            erro = "Este e-mail ja possui um registo";
                        }catch (FirebaseNetworkException ex){

                            erro = "Problemas de conexao";
                        } catch (Exception ex) {
                            erro = "Erro desconhecido";
                            ex.printStackTrace();

                        }

                        Toast.makeText(RegistoActivity.this, erro, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    public void abrirLogin() {
        Intent intent = new Intent(RegistoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }




    // Função para verificar existência de conexão com a internet

    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm!=null){
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni !=null && ni.isConnected();
        }
        return false;
    }
}