package mz.co.vida;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class CreateActivity extends AppCompatActivity {

    // Components
    TabLayout tl_an_not;
    int fl_create;

    // Fragments
    FragmentManager      fragmentManager;
    AnuncioFragment      anuncioFragment;
    NotificationFragment notificationFragment;

    public static final int ANUNCIO = 0;
    public static final int NOTIFICATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        fragmentManager = getSupportFragmentManager();

        anuncioFragment      = new AnuncioFragment();
        notificationFragment = new NotificationFragment();

        fl_create = R.id.fl_create;
        tl_an_not = (TabLayout) findViewById(R.id.tl_an_not);


        int selectedTab_position = tl_an_not.getSelectedTabPosition();
        chooseTab(selectedTab_position);

        tl_an_not.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
            case ANUNCIO:
                MyUtils.changeFragment(fl_create, anuncioFragment, fragmentManager);
                break;

            case NOTIFICATION:
                MyUtils.changeFragment(fl_create, notificationFragment, fragmentManager);
                break;
        }
    }
}
