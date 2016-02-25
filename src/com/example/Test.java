package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.MyTestBluetooth.R;

/**
 * Created by Valera on 02.12.2015.
 */
public class Test extends Activity {
    ImageView refreshMove;
    private int currentRotation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayer_game);
        currentRotation=1;
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        TextView textView = (TextView) findViewById(R.id.textView4);
        refreshMove = (ImageView) findViewById(R.id.arrows);
        //////////////////////////
        ///////////////////////////////
    }

}
