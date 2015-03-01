package org.hackillinios.studentplanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Michael Smith on 2/28/2015.
 */
public class HomeworkList extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout drawer;
    private ListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_list);
        initDrawer();

        Fragment fragment = new AssignmentFragment();
        getSupportActionBar().setTitle("Assignments");
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    private void initDrawer() {
        toolbar = (Toolbar)findViewById(R.id.mytoolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerList = (ListView) findViewById(R.id.list_slidermenu);
        String[] list = {"Assignments, Classes, Upcoming, Settings"};

        drawerList.setAdapter(new DrawerCustomAdapter(this, list));

        drawerList.setOnItemClickListener(this);

        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawer.setScrimColor(Color.TRANSPARENT);

        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.draweropen,
                R.string.drawerclosed) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            public void onDrawerClosed(View view) {
            }

            // /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            }

        };
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = new AssignmentFragment();
                getSupportActionBar().setTitle("Assignments");
                break;

            case 1:
                //fragment = new ClassesFragment();
                getSupportActionBar().setTitle("Classes");
                break;

            case 2:
                //fragment = new UpcomingFragment():
                getSupportActionBar().setTitle("Upcoming");
                break;

            case 3:
                //fragment = new SettingsFragment();
                getSupportActionBar().setTitle("Settings");
                break;
        }
        if(fragment != null){
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            drawer.closeDrawer(drawerList);
        }
    }
}
