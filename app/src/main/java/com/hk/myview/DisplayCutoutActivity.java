package com.hk.myview;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by hk on 2019/7/2.
 */
public class DisplayCutoutActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean hasDisplayCutout = hasDisplayCutout(window);
        if (hasDisplayCutout) {
            WindowManager.LayoutParams params = window.getAttributes();

            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(params);

            int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int visibility = window.getDecorView().getSystemUiVisibility();
            visibility |= flags;
            window.getDecorView().setSystemUiVisibility(visibility);
        }

        setContentView(R.layout.activity_display_cutout);

        RelativeLayout layout = findViewById(R.id.container);
        layout.setPadding(layout.getPaddingLeft(), heightForDisplayCutout(), layout.getPaddingRight(), layout.getPaddingBottom());
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean hasDisplayCutout(Window window) {
        DisplayCutout displayCutout;
        View rootView = window.getDecorView();
        WindowInsets insets = rootView.getRootWindowInsets();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && insets != null) {
            displayCutout = insets.getDisplayCutout();
            if (displayCutout != null) {
                if (displayCutout.getBoundingRects() != null && displayCutout.getBoundingRects().size() > 0 && displayCutout.getSafeInsetTop() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public int heightForDisplayCutout() {
        int resID = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resID > 0) {
            return getResources().getDimensionPixelSize(resID);
        }
        return 0;
    }
}
