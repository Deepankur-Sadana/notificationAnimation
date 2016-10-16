package com.example.deepankur.notificationanimation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deepankur.notificationview.NotificationView;
import com.deepankur.notificationview.Utils;
import com.squareup.picasso.Picasso;


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
        this.context = context;
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
        for (int i = 0; i < 6; i++) {
            View v = inflater.inflate(R.layout.notification_card, null);
            notificationView.addNotificationChild(v);
            resetMargins(v);
        }
        addNonAnimatingChildren();
        return rootView;
    }

    private void resetMargins(View itemView) {
        ((FrameLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = Utils.convertDpToPixels(context, 8);
        ((FrameLayout.LayoutParams) itemView.getLayoutParams()).rightMargin = Utils.convertDpToPixels(context, 8);
        ((FrameLayout.LayoutParams) itemView.getLayoutParams()).bottomMargin = Utils.convertDpToPixels(context, 8);
    }


    private void addNonAnimatingChildren() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
            Picasso.with(getActivity()).load(imageUrls[i]).fit().centerCrop().into(imageView);

            View dividerView = new View(context);
            dividerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            linearLayout.addView(imageView);
            linearLayout.addView(dividerView);
        }
        notificationView.addNormalChild(linearLayout);
    }

    private final String[] imageUrls = new String[]{
            "http://english.republika.mk/wp-content/uploads/2015/12/Sachsen-Fichtelberg-Winter.jpg",
            "http://moviehole.net/img/gijo.jpg",
            "http://p10cdn4static.sharpschool.com/UserFiles/Servers/Server_153659/Image/Departments/Library/5578b314afca2-01-fall-mason-jar-lgn-54605162.jpg",
            "http://static.giantbomb.com/uploads/original/11/112562/2183973-gi_joe_crew.jpg"
    };
}
