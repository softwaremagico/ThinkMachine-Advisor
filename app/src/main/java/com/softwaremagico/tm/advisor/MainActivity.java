package com.softwaremagico.tm.advisor;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.softwaremagico.tm.advisor.ui.main.TabsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());

    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(tabsPagerAdapter);

    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);
  }
}