package com.example.assesment.android;

import android.nfc.Tag;

/**
 * Created by dcastrillo on 25/09/2014.
 */

public interface MainActivityEventDispatch {

    public enum FragmentTag {
        GNOMES,
        GNOME_DETAIL,
    }

    public void onFragmentTransition(FragmentTag fTag);
    public void changeMainContentHeader(String title, String name);
    public void changeMainContentPicture(String url);
}
