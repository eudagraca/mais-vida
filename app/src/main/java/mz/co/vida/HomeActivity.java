package mz.co.vida;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import mz.co.vida.Fragments.AnuncioFragment;
import mz.co.vida.Fragments.DoadoresFragment;
import mz.co.vida.Fragments.PerfilFragment;
import mz.co.vida.Fragments.RequisitantesFragment;
import mz.co.vida.Fragments.sobreFragment;
import mz.co.vida.Helper.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity {



    BottomNavigationView main_nav;

    int fl_framelayout;
    private FragmentManager fragmentManager;
    sobreFragment sobre_fragment;
    PerfilFragment perfil_fragment;
    DoadoresFragment doadoresFragment;
    AnuncioFragment anuncioFragment;
    RequisitantesFragment requisitantesFragment;


    // Components
    TabLayout tl_don_req;
    private int fl_create;

    public static final int DOADOR = 0;
    public static final int REQUISITANTES = 1;

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
        doadoresFragment = new DoadoresFragment();
        requisitantesFragment = new RequisitantesFragment();


        fl_create = R.id.fl_framelayout;
        tl_don_req = (TabLayout) findViewById(R.id.tl_donnor_reqq);

        int selectedTab_position = tl_don_req.getSelectedTabPosition();
        chooseTab(selectedTab_position);


        main_nav = (BottomNavigationView) findViewById(R.id.nav_main);
        isRequisitante = sharedPreferences.getBoolean("isRequisitante", false);
        BottomNavigationViewHelper.disableShiftMode(main_nav);


        menu = main_nav.getMenu();
        if (!isRequisitante) {
            menu.removeItem(R.id.navigation_anunciar);
        }

        fl_framelayout = R.id.fl_framelayout;
        tl_don_req.setVisibility(View.VISIBLE);

        MyUtils.changeFragment(fl_framelayout, doadoresFragment, fragmentManager);

        main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())  {
                    case R.id.navigation_home:
                        tl_don_req.setVisibility(View.VISIBLE);
                        MyUtils.changeFragment(fl_framelayout, doadoresFragment, fragmentManager);
                        return true;
                    case R.id.navigation_anunciar:
                        tl_don_req.setVisibility(View.GONE);
                        MyUtils.changeFragment(fl_framelayout, anuncioFragment, fragmentManager);
//                        Intent intent = new Intent(getBaseContext(), CreateActivity.class);
//                        startActivity(intent);
                        return true;
                    case R.id.navigation_perfil:
                        tl_don_req.setVisibility(View.GONE);
                        MyUtils.changeFragment(fl_framelayout, perfil_fragment, fragmentManager);
                        return true;
                    case R.id.navigation_sobre:
                        tl_don_req.setVisibility(View.GONE);
                        MyUtils.changeFragment(fl_framelayout, sobre_fragment, fragmentManager);
                        return true;
                }
                return false;
            }
        });



        tl_don_req.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                chooseTab(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {  }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                chooseTab(position);
            }
        });
    }




    void chooseTab(int position){
        switch (position){
            case DOADOR:
                MyUtils.changeFragment(fl_create, doadoresFragment, fragmentManager);
                break;

            case REQUISITANTES:
                MyUtils.changeFragment(fl_create, requisitantesFragment, fragmentManager);
                break;
        }
    }
}
