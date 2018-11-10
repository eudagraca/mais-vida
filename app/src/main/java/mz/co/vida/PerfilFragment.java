package mz.co.vida;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zcw.togglebutton.ToggleButton;

import mz.co.vida.DAO.ConfiguracaoFirebase;
import de.hdodenhof.circleimageview.CircleImageView;
import mz.co.vida.entidades.Usuario;


public class PerfilFragment extends Fragment {

    public static final String SP_NAME = "maisVidaSP";

    private TextView tNome, tSexo, tContacto,
            tSangue, tProvincia, tUnidadeSanitaria, tEstado;
    private ToggleButton tDisponibilidade;
    private CircleImageView imFoto;
    FrameLayout relativeLayout;

    SharedPreferences mySharedPref;

    FirebaseAuth mAuth;
    DatabaseReference profileUser;
    String usuarioActual;


    public PerfilFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        usuarioActual = mAuth.getCurrentUser().getUid();
        profileUser = ConfiguracaoFirebase.getFirebase().child("Usuario").child(usuarioActual);

        mySharedPref = getActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        tNome = view.findViewById(R.id.tv_nome);
        tContacto = view.findViewById(R.id.tv_contacto);
        tSexo = view.findViewById(R.id.tv_Sexo);
        tProvincia = view.findViewById(R.id.tv_provincia);
        tUnidadeSanitaria = view.findViewById(R.id.tv_unidade_sanitaria);
        tEstado = view.findViewById(R.id.tv_estado);
        tDisponibilidade = view.findViewById(R.id.tv_disponibilidade);
        tSangue = view.findViewById(R.id.tv_tiposanguineo);
        imFoto = view.findViewById(R.id.iv_foto);
        relativeLayout = view.findViewById(R.id.rl);




       //e mais uma coisa.. tas a ver o valor de SharedPref.. ele fica guardado aqui
        boolean estaToggled = mySharedPref.getBoolean("situacaoDoToggle", false);
        Log.v("ssss", String.valueOf(estaToggled));

        tDisponibilidade.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean isChecked) {
                SharedPreferences.Editor editor = mySharedPref.edit();
                if (isChecked) {
                    editor.putBoolean("situacaoDoToggle", false);
                } else {
                    editor.putBoolean("situacaoDoToggle", true);
                }
                editor.apply();
            }
        });


        profileUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(dataSnapshot.child("nome").getValue().toString());
                    String foto = dataSnapshot.child("foto").getValue().toString();
                    usuario.setEmail(dataSnapshot.child("email").getValue().toString());
                    usuario.setFoto(dataSnapshot.child("foto").getValue().toString());
                    usuario.setProvincia(dataSnapshot.child("provincia").getValue().toString());
                    usuario.setUnidadeProxima(dataSnapshot.child("unidadeProxima").getValue().toString());
                    usuario.setTipoSanguineo(dataSnapshot.child("tipoSanguineo").getValue().toString());
                    usuario.setSexo(dataSnapshot.child("sexo").getValue().toString());
                    usuario.setDisponibilidade(dataSnapshot.child("disponibilidade").getValue().toString());
                    usuario.setEstado(dataSnapshot.child("estado").getValue().toString());
                    usuario.setTelefone(dataSnapshot.child("telefone").getValue().toString());

                    Picasso.get().load(foto).placeholder(R.drawable.ic_user).into(imFoto);
                    tNome.setText(usuario.getNome());
                    tContacto.setText(usuario.getTelefone());


                    if ((usuario.getEstado().equals("DOADOR") || usuario.getEstado().equals("Doador"))) {
                        tDisponibilidade.setVisibility(View.VISIBLE);
                        if (usuario.getDisponibilidade().equals("Sim")){
                            tDisponibilidade.setToggleOn(true);
                            relativeLayout.setBackgroundResource(R.color.md_red_50);
                        }else if (usuario.getDisponibilidade().equals("Não")){
                            tDisponibilidade.setToggleOff(true);
                        }
                    }else if (usuario.getEstado().equals("Requisitante")|| usuario.getEstado().equals("REQUISITANTE")) {
                        tDisponibilidade.setVisibility(View.INVISIBLE);

                    }  // estava a fazer toggle porcausa disto.. enão porque pegou o valor errado de sharedpref.
                    tEstado.setText(usuario.getEstado());
                    tSangue.setText(usuario.getSexo());
                    tProvincia.setText(usuario.getProvincia());
                    tUnidadeSanitaria.setText(usuario.getUnidadeProxima());
                    tSangue.setText(usuario.getTipoSanguineo());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;


    }

}