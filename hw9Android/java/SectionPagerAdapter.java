package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new TabFragment("world");
        switch (position) {
            case 0:
                fragment = new TabFragment("world");
                break;
            case 1:
                fragment = new TabFragment("business");
                break;
            case 2:
                fragment = new TabFragment("politics");
                break;
            case 3:
                fragment = new TabFragment("sport");
                break;
            case 4:
                fragment = new TabFragment("technology");
                break;
            case 5:
                fragment = new TabFragment("science");
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "WORLD";
            case 1:
                return "BUSINESS";
            case 2:
                return "POLITICS";
            case 3:
                return "SPORTS";
            case 4:
                return "TECHNOLOGY";
            case 5:
                return "SCIENCE";
        }
        return null;
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }
}
