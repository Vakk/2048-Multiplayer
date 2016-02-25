package com.example.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.MyTestBluetooth.R;
import com.example.game.MyActivity;

/**
 * Created by Valera on 10.11.2015.
 */
public class MainMenu extends Activity implements View.OnTouchListener {
  @Override
  public void onCreate(Bundle savedInstanceOf) {
    super.onCreate(savedInstanceOf);
    setContentView(R.layout.main);
    Button start = (Button) findViewById(R.id.submit_play);
    start.setOnTouchListener(this);
    Button startMultiplayer = (Button) findViewById(R.id.submit_multiplayer);
    startMultiplayer.setOnTouchListener(this);
    Button exit = (Button) findViewById(R.id.exit);
    exit.setOnTouchListener(this);
  }

  @Override
  public boolean onTouch(View v, MotionEvent e) {
    switch (e.getAction()) {
      case (MotionEvent.ACTION_UP): {
        v.setPadding(0, 0, 0, 0);
        Intent intent;
        switch (v.getId()) {
          case (R.id.submit_play): {
            intent = new Intent(this, MyActivity.class);
            startActivity(intent);
            break;
          }
          case (R.id.submit_multiplayer): {
            intent = new Intent(this, MultiPlayerSwitch.class);
            startActivity(intent);
            break;
          }
          case (R.id.exit): {
            finish();
          }
        }
        return true;
      }
      case (MotionEvent.ACTION_DOWN): {
        v.setPadding(10, 10, 0, 0);
        return true;
      }
    }
    return false;
  }
}
