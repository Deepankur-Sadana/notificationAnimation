package com.example.deepankur.notificationanimation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deepankur.notificationview.NotificationView;
import com.deepankur.notificationview.Utils;


/**
 * Created by deepankur on 10/16/16.
 */

public class AnimationFragment extends Fragment {
    private String TAG = getClass().getSimpleName();
    private View rootView;
    NotificationView notificationView;
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_animation, container, false);
        notificationView = (NotificationView) rootView.findViewById(R.id.notificationView);
        for (int i = 0; i < 20; i++) {
            View v = inflater.inflate(R.layout.notification_card, null);
            notificationView.addNotificationChild(v);
            resetMargins(v);
        }


        return rootView;
    }

    void resetMargins(View itemView) {
        ((FrameLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = Utils.convertDpToPixels(context, 8);
        ((FrameLayout.LayoutParams) itemView.getLayoutParams()).rightMargin = Utils.convertDpToPixels(context, 8);
        ((FrameLayout.LayoutParams) itemView.getLayoutParams()).bottomMargin = Utils.convertDpToPixels(context, 8);
    }
}
