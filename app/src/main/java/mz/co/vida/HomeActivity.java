package mz.co.vida;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import mz.co.vida.Fragments.AnuncioFragment;
import mz.co.vida.Fragments.HomeFragment;
import mz.co.vida.Fragments.PerfilFragment;
import mz.co.vida.Fragments.sobreFragment;
import mz.co.vida.Helper.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity {



    BottomNavigationView main_nav;
    int fl_framelayout;
    private FragmentManager fragmentManager;
    sobreFragment sobre_fragment;
    PerfilFragment perfil_fragment;
    HomeFragment homeFragment;
    AnuncioFragment anuncioFragment;

    SharedPreferences sharedPreferences;
    Menu menu;

    boolean isRequisitante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentManager = getSupportFragmentManager();
        sharedPreferences = getSharedPreferences(MyUtils.SP_NAME, MODE_PRIVATE);

        // Fragment init
        perfil_fragment  = new PerfilFragment();
        sobre_fragment = new sobreFragment();
        anuncioFragment = new AnuncioFragment();
        homeFragment = new HomeFragment();


        main_nav = (BottomNavigationView) findViewById(R.id.nav_main);
        isRequisitante = sharedPreferences.getBoolean("isRequisitante", false);
        BottomNavigationViewHelper.disableShiftMode(main_nav);


        menu = main_nav.getMenu();
        if (!isRequisitante) {
            menu.removeItem(R.id.navigation_anunciar);
        }

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
                        //MyUtils.changeFragment(fl_framelayout, anuncioFragment, fragmentManager);
                        Intent intent = new Intent(getBaseContext(), CreateActivity.class);
                        startActivity(intent);
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
