package com.example.menu;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.MyTestBluetooth.R;
import com.example.bluetooth.client.ClientActivity;
import com.example.bluetooth.server.ServerActivity;

/**
 * Created by Valera on 24.11.2015.
 */
public class MultiPlayerSwitch extends Activity implements View.OnTouchListener {
  private static final int REQUEST_ENABLE_BT = 3;

  @Override
  public void onCreate(Bundle instances) {
    super.onCreate(instances);
    setContentView(R.layout.multiplayer_activity);
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    Button client = (Button) findViewById(R.id.start_client);
    Button server = (Button) findViewById(R.id.start_server);
    client.setOnTouchListener(this);
    server.setOnTouchListener(this);
    if (bluetooth == null) {
      Toast.makeText(this, "Sorry, your device is so old",
          Toast.LENGTH_LONG).show();
      finish();
      return;
    } else if (!bluetooth.isEnabled()) {
      Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // When the request to enable Bluetooth returns
    if (resultCode == Activity.RESULT_OK) {
      // Bluetooth is now enabled, so set up a chat session
    } else {
      // User did not enable Bluetooth or an error occurred
      Toast.makeText(this, R.string.bt_not_enabled_leaving,
          Toast.LENGTH_SHORT).show();
      finish();
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent e) {
    switch (e.getAction()) {
      case (MotionEvent.ACTION_UP): {
        v.setPadding(0, 0, 0, 0);
        Intent intent;
        switch (v.getId()) {
          case (R.id.start_client): {
            intent = new Intent(this, ClientActivity.class);
            startActivity(intent);
            break;
          }
          case (R.id.start_server): {
            intent = new Intent(this, ServerActivity.class);
            startActivity(intent);
            break;
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
