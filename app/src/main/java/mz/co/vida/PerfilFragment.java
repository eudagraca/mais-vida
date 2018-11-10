package mz.co.vida;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

    private TextView Tnome, Tsexo, Tcontacto,
            Tsangue, Tprovincia, TunidadeSanitaria, Testado;
    private com.zcw.togglebutton.ToggleButton Tdisponibilidade;
    private CircleImageView ImFoto;
    FrameLayout relativeLayout;

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

        Tnome = view.findViewById(R.id.tv_nome);
        Tcontacto = view.findViewById(R.id.tv_contacto);
        Tsexo = view.findViewById(R.id.tv_Sexo);
        Tprovincia = view.findViewById(R.id.tv_provincia);
        TunidadeSanitaria = view.findViewById(R.id.tv_unidade_sanitaria);
        Testado = view.findViewById(R.id.tv_estado);
        Tdisponibilidade = view.findViewById(R.id.tv_disponibilidade);
        Tsangue = view.findViewById(R.id.tv_tiposanguineo);
        ImFoto = view.findViewById(R.id.iv_foto);
        relativeLayout = view.findViewById(R.id.rl);

        profileUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(dataSnapshot.child("nome").getValue(String.class));

                    usuario.setEmail(dataSnapshot.child("email").getValue(String.class));

                    if (dataSnapshot.child("foto").exists()){
                        String foto = dataSnapshot.child("foto").getValue(String.class);
                        usuario.setFoto(foto);
                        Picasso.get().load(foto).placeholder(R.drawable.ic_user).into(ImFoto);
                    }

                    usuario.setProvincia(dataSnapshot.child("provincia").getValue(String.class));
                    usuario.setUnidadeProxima(dataSnapshot.child("unidadeProxima").getValue(String.class));
                    usuario.setTipoSanguineo(dataSnapshot.child("tipoSanguineo").getValue(String.class));
                    usuario.setSexo(dataSnapshot.child("sexo").getValue(String.class));
                    usuario.setDisponibilidade(dataSnapshot.child("disponibilidade").getValue(String.class));
                    usuario.setEstado(dataSnapshot.child("estado").getValue(String.class));
                    usuario.setTelefone(dataSnapshot.child("telefone").getValue(String.class));


                    Tnome.setText(usuario.getNome());
                    Tcontacto.setText(usuario.getTelefone());
                    if (usuario.getEstado().equals("Doador")) {
                        Tdisponibilidade.setVisibility(View.VISIBLE);
                        if (usuario.getDisponibilidade().equals("Sim")){
                            Tdisponibilidade.setToggleOn();
                            relativeLayout.setBackgroundResource(R.color.md_red_50);
                        }else if (usuario.getDisponibilidade().equals("Não")){
                            Tdisponibilidade.setToggleOff();
                        }
                    }else if (usuario.getEstado().equals("Requisitante")) {
                        Tdisponibilidade.setVisibility(View.INVISIBLE);

                    }
                    Testado.setText(usuario.getEstado());
                    Tsexo.setText(usuario.getSexo());
                    Tprovincia.setText(usuario.getProvincia());
                    TunidadeSanitaria.setText(usuario.getUnidadeProxima());
                    Tsangue.setText(usuario.getTipoSanguineo());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Tdisponibilidade.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean isOn) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyUtils.SP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isRequisitante", !isOn);
                editor.apply();

                profileUser.child("estado").setValue(isOn ? "Doador" : "Requisitante");
            }
        });



        return view;


    }

}