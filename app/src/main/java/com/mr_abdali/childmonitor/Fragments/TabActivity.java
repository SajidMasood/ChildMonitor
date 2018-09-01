package com.mr_abdali.childmonitor.Fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mr_abdali.childmonitor.Adapters.ViewPagerAdapter;
import com.mr_abdali.childmonitor.R;

public class TabActivity extends AppCompatActivity {

    // TODO: 8/7/2018 Variables Declaration section....
    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        // TODO: 8/10/2018 Tool bar....
        toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        toolbar.setTitle("Child Monitor");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.TabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        // TODO: 8/8/2018 Adding Fragment Layout in TabActivity...
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragCallLogs(), "Calls");
        adapter.addFragment(new FragContactList(), "Contact");
        adapter.addFragment(new FragMessagesList(),"SMS");
        adapter.addFragment(new FragLocation(),"Location");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_phone);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_contacts);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_message);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_map);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
