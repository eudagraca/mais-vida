package mz.co.vida;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import DAO.ConfiguracaoFirebase;
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

                    Picasso.get().load(foto).placeholder(R.drawable.ic_user).into(ImFoto);
                    Tnome.setText(usuario.getNome());
                    Tcontacto.setText(usuario.getTelefone());
                    if ((usuario.getEstado().equals("DOADOR") || usuario.getEstado().equals("Doador"))) {
                        Tdisponibilidade.setVisibility(View.VISIBLE);
                        if (usuario.getDisponibilidade().equals("Sim")){
                            Tdisponibilidade.setToggleOn();
                            relativeLayout.setBackgroundResource(R.color.md_red_50);
                        }else if (usuario.getDisponibilidade().equals("Não")){
                            Tdisponibilidade.setToggleOff();
                        }
                    }else if (usuario.getEstado().equals("Requisitante")|| usuario.getEstado().equals("REQUISITANTE")) {
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

        return view;


    }

}