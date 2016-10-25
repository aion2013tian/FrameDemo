package com.frame.aion.framedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;

import com.frame.aion.framedemo.view.Rotate3DAnimation;

public class MainActivity extends AppCompatActivity {

    private ImageView rotate;
    int mDuration = 500;
    float mCenterX = 0.0f;
    float mCenterY = 0.0f;
    float mDepthZ  = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rotate = (ImageView) findViewById(R.id.rotate);
        final int width = this.getResources().getDisplayMetrics().widthPixels;
        final int height = this.getResources().getDisplayMetrics().heightPixels;
//        rotate();
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCenterX = rotate.getWidth()/2;
                mCenterY = rotate.getHeight()/2;
                getDepthZ();
                applyRotation(rotate, 0, 60);
            }
        });
    }

    private void rotate() {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(200);
        rotateAnimation.setRepeatCount(1000);
        rotate.setAnimation(rotateAnimation);
        rotate.startAnimation(rotateAnimation);
    }

    public int dip2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    private void getDepthZ() {
        EditText editText = (EditText) findViewById(R.id.edit_depthz);
        String string = editText.getText().toString();

        try
        {
//            mDepthZ = (float)Integer.parseInt(string);
            mDepthZ = 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void applyRotation(View animView, float startAngle, float toAngle) {
        float centerX = mCenterX;
        float centerY = mCenterY;
        Rotate3DAnimation rotation = new Rotate3DAnimation(
                startAngle, toAngle, centerX, centerY, mDepthZ, true);
        rotation.setDuration(mDuration);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                rotate.clearAnimation();
//                rotate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animView.startAnimation(rotation);
    }
}
