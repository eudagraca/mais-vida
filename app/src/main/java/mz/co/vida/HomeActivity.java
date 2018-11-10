package mz.co.vida;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {


    int fl_framelayout;
    BottomNavigationView main_nav;
    FragmentManager fragmentManager;
    sobreFragment sobre_fragment;
    PerfilFragment perfil_fragment;
    HomeFragment homeFragment;
    AnuncioFragment anuncioFragment;
    private BottomNavigationView estado;
    View barrinha;

    BottomNavigationView nav_anuncio;
    MeusAnunciosFragment meusAnunciosFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        fragmentManager = getSupportFragmentManager();

        // Fragment init
        perfil_fragment  = new PerfilFragment();
        sobre_fragment = new sobreFragment();
        anuncioFragment = new AnuncioFragment();
        homeFragment = new HomeFragment();

        meusAnunciosFragment = new MeusAnunciosFragment();
        nav_anuncio = (BottomNavigationView) findViewById(R.id.estado);
        anuncioFragment = new AnuncioFragment();

        barrinha = findViewById(R.id.barrinha);
        main_nav = (BottomNavigationView) findViewById(R.id.nav_main);
        estado = (BottomNavigationView) findViewById(R.id.estado);
        fl_framelayout = R.id.fl_framelayout;
        estado.setVisibility(View.INVISIBLE);
        barrinha.setVisibility(View.INVISIBLE);

        MyUtils.changeFragment(fl_framelayout, homeFragment, fragmentManager);

        main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())  {
                    case R.id.navigation_home:
                        MyUtils.changeFragment(fl_framelayout, homeFragment, fragmentManager);
                        estado.setVisibility(View.INVISIBLE);
                        barrinha.setVisibility(View.INVISIBLE);
                        return true;
                    case R.id.navigation_anunciar:
                        MyUtils.changeFragment(fl_framelayout, anuncioFragment, fragmentManager);
                        estado.setVisibility(View.VISIBLE);
                        barrinha.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.navigation_perfil:
                        MyUtils.changeFragment(fl_framelayout, perfil_fragment, fragmentManager);
                        estado.setVisibility(View.INVISIBLE);
                        barrinha.setVisibility(View.INVISIBLE);
                        return true;
                    case R.id.navigation_sobre:
                        MyUtils.changeFragment(fl_framelayout, sobre_fragment, fragmentManager);
                        estado.setVisibility(View.INVISIBLE);
                        barrinha.setVisibility(View.INVISIBLE);
                        return true;
                }
                return false;
            }
        });

        nav_anuncio.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())  {
                    case R.id.anuncios_item:
                        MyUtils.changeFragment(fl_framelayout, anuncioFragment, fragmentManager);

                        return true;
                    case R.id.meus_anuncios_item:
                        MyUtils.changeFragment(fl_framelayout, meusAnunciosFragment, fragmentManager);
                        //estado.setVisibility(View.VISIBLE);
                        return true;
                }
                return false;
            }
        });

    }

}
