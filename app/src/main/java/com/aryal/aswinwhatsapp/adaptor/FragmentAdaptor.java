package com.aryal.aswinwhatsapp.adaptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aryal.aswinwhatsapp.fragments.CallFragment;
import com.aryal.aswinwhatsapp.fragments.ChatFragment;
import com.aryal.aswinwhatsapp.fragments.StatusFragment;

public class FragmentAdaptor extends FragmentPagerAdapter {
    public FragmentAdaptor(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ChatFragment();
            case 1: return new StatusFragment();
            case 2: return new CallFragment();
            default: return new ChatFragment();


        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=null;
        switch (position){
            case 0:
                title="Chats";break;
            case 1:
                title="Status";break;
            case 2:
                title="Calls";break;
        }
        return title;
    }
}
