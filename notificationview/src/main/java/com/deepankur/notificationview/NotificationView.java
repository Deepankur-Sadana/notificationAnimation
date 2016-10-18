package com.deepankur.notificationview;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by deepankur on 10/16/16.
 */

@SuppressWarnings("FieldCanBeLocal")

public class NotificationView extends NestedScrollView {
    private String TAG = getClass().getSimpleName();
    private OnScrollChangeListener mOnScrollChangeListener;
    private Context context;

    //we have taken effective tab height as the one without including shadows
    //total height is taken as 80 dp and includes show of 24dp (8 dp at top and 24 dp at bottom)
    private int EFFECTIVE_TAB_HEIGHT = 56;
    private int TOTAL_TAB_HEIGHT = 80;
    private int scrollTo;


    public NotificationView(Context context) {
        super(context);
        init(context);
    }

    public NotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NotificationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.addChildrenToTheViews();
        super.setOnScrollChangeListener(scrollChangeListener);
    }

    private NestedScrollView.OnScrollChangeListener scrollChangeListener = new OnScrollChangeListener() {
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (mOnScrollChangeListener != null)
                mOnScrollChangeListener.onScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY);

            Log.d(TAG, "onScrollChange: scrollY " + scrollY);
            notifyScrolled(scrollY, oldScrollY);
        }
    };

    @Override
    public final void setOnScrollChangeListener(OnScrollChangeListener l) {
        this.mOnScrollChangeListener = l;
    }

    private LinearLayout normalChildHolder;

    private FrameLayout notificationTabsBackGroundFrame;
    private FrameLayout tabsHolder;

    void addChildrenToTheViews() {
        LinearLayout directChildLl = new LinearLayout(context);
        directChildLl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        directChildLl.setOrientation(LinearLayout.VERTICAL);

        normalChildHolder = new LinearLayout(context);
        normalChildHolder.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        normalChildHolder.setOrientation(LinearLayout.VERTICAL);

        notificationTabsBackGroundFrame = new FrameLayout(context);
        notificationTabsBackGroundFrame.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tabsHolder = new FrameLayout(context);
        tabsHolder.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        notificationTabsBackGroundFrame.addView(tabsHolder);
        directChildLl.addView(notificationTabsBackGroundFrame);
        directChildLl.addView(normalChildHolder);
        this.addView(directChildLl);
    }


    public void addNotificationChild(View view) {
        tabsHolder.addView(view, 0);
        addMarginsToViews(view);
    }

    public void addNormalChild(View view) {
        this.normalChildHolder.addView(view);
    }

    private void addMarginsToViews(View itemView) {


//        int TAB_HEIGHT = 80;//inclusive of shadow and shit
        ((FrameLayout.LayoutParams) itemView.getLayoutParams()).height = Utils.convertDpToPixels(context, TOTAL_TAB_HEIGHT);
        notificationTabsBackGroundFrame.requestLayout();
        addViewTreeObserver(itemView);
    }

    private void addViewTreeObserver(final View view) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    refreshTopMarginForContributionViews();
                }
            });
        }
    }

    private void refreshTopMarginForContributionViews() {
        int viewsTopOfCurrentView = 0;
        for (int i = tabsHolder.getChildCount() - 1; i >= 0; i--) {
            if (viewsTopOfCurrentView == 0) {//visibly top most view

            } else
                ((FrameLayout.LayoutParams) tabsHolder.getChildAt(i).getLayoutParams()).topMargin = Utils.convertDpToPixels(context, EFFECTIVE_TAB_HEIGHT * viewsTopOfCurrentView);
            ++viewsTopOfCurrentView;

            if (i == 0) {//visibly bottom most view
                ((FrameLayout.LayoutParams) tabsHolder.getChildAt(i).getLayoutParams()).bottomMargin = -Utils.convertDpToPixels(context, 8);
            } else {
                ((FrameLayout.LayoutParams) tabsHolder.getChildAt(i).getLayoutParams()).bottomMargin = 0;
            }
        }
    }

    private void notifyScrolled(int scrollY, int oldScrollY) {
        if (tabsHolder.getChildCount() == 0)
            return;

        for (int j = 0; j < tabsHolder.getChildCount(); j++) {
            tabsHolder.getChildAt(j).setTranslationY(getTranslationForTab(j, scrollY));
        }
    }

    private int getTranslationForTab(int index, int scrollY) {
        int translation = 0;

        if (index == 0)
            translation = 0;
        else {
            if (scrollY < getCutOffTranslationForTab(index))
                translation = scrollY;
            else
                translation = getCutOffTranslationForTab(index);
        }
        return translation;
    }

    private final int OVERLAP_DISTANCE = 6;//in dps

    private int getCutOffTranslationForTab(int index) {
        if (index == 0)
            return 0;
        return Utils.convertDpToPixels(context, ((EFFECTIVE_TAB_HEIGHT - OVERLAP_DISTANCE) * index));
    }


    private int getScrollForContractingTheStacks() {
        int displacementFromTop = Utils.convertDpToPixels(context, EFFECTIVE_TAB_HEIGHT + (tabsHolder.getChildCount() - 2) * OVERLAP_DISTANCE + 14);
        Log.d(TAG, "getScrollForContractingTheStacks: displacement from top " + displacementFromTop + " totalHeight " + notificationTabsBackGroundFrame.getMeasuredHeight() + " count " + tabsHolder.getChildCount());
        return notificationTabsBackGroundFrame.getMeasuredHeight() - displacementFromTop;
    }

    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            smoothScrollTo(0, scrollTo);
        }
    };

    public void contractStacks() {
        scrollTo = getScrollForContractingTheStacks();
        Log.d(TAG, "contractStacks: " + scrollTo);
        this.postDelayed(scrollRunnable, 100);
    }

    public void expandStacks() {
        scrollTo = 0;
        this.postDelayed(scrollRunnable, 100);
    }
}
