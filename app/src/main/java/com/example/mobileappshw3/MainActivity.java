package com.example.mobileappshw3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    //private TabItem tabItem_character, tabItem_episode, tabItem_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadFragment(new FragmentCharacter());
                        break;
                    case 1:
                        loadFragment(new FragmentEpisode());
                        break;
                    case 2:
                        loadFragment(new FragmentLocation());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadFragment(new FragmentCharacter());
                        break;
                    case 1:
                        loadFragment(new FragmentEpisode());
                        break;
                    case 2:
                        loadFragment(new FragmentLocation());
                        break;
                }
            }
        });

        // NOT WORKING !!!
        //tabItem_character = findViewById(R.id.TabItem_character);
        //tabItem_episode = findViewById(R.id.TabItem_episode);
        //tabItem_location = findViewById(R.id.TabItem_location);
        //tabItem_character.setOnClickListener(v -> loadFragment(new FragmentCharacter()));
        //tabItem_episode.setOnClickListener(v -> loadFragment(new FragmentEpisode()));
        //tabItem_location.setOnClickListener(v -> loadFragment(new FragmentLocation()));

    }

    public void loadFragment(Fragment fragment) {
        // create a fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // create a fragment transaction to begin the transaction and replace the fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // replacing the placeholder - fragmentContainerView with the fragment that is passed as parameter
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
}