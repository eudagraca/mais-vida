package mz.co.vida;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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


        main_nav = (BottomNavigationView) findViewById(R.id.nav_main);
        fl_framelayout = R.id.fl_framelayout;

        MyUtils.changeFragment(fl_framelayout, homeFragment, fragmentManager);

        main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())  {
                    case R.id.navigation_home:
                        MyUtils.changeFragment(fl_framelayout, homeFragment, fragmentManager);
                        return true;
                    case R.id.navigation_anunciar:
                        MyUtils.changeFragment(fl_framelayout, anuncioFragment, fragmentManager);
                        return true;
                    case R.id.navigation_perfil:
                        MyUtils.changeFragment(fl_framelayout, perfil_fragment, fragmentManager);
                        return true;
                    case R.id.navigation_sobre:
                        MyUtils.changeFragment(fl_framelayout, sobre_fragment, fragmentManager);
                        return true;
                }
                return false;
            }
        });
    }

}
