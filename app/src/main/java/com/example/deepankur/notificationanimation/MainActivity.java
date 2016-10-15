package com.example.deepankur.notificationanimation;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadChatFragment();
    }

    public void loadChatFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.activity_main, new AnimationFragment(), AnimationFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(AnimationFragment.class.getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }


}
