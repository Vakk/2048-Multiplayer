package com.example.bluetooth.server;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.Constants;
import com.example.MyTestBluetooth.R;
import com.example.bluetooth.BluetoothServices;
import com.example.bluetooth.ConductActivity;
import com.example.bluetooth.PlayBluetooth;
import com.example.game.MyActivity;
import com.example.game.Painting;

/**
 * Created by Valera on 25.11.2015.
 */
public class ServerActivity extends ConductActivity {
  // стандартний блютуз адаптер пристрою
  BluetoothAdapter bluetooth;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    game = null;

    bluetooth = BluetoothAdapter.getDefaultAdapter();
    if (bluetooth == null) {
      Toast.makeText(this, "Sorry, your device is so old",
          Toast.LENGTH_LONG).show();
      finish();
      return;
    } else if (!bluetooth.isEnabled()) {
      Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableIntent, 0);
    }
    service = new BluetoothServices(this, mHandler);
  }

  /**
   * починає багатокористувацьку в режимі сервера
   */
  @Override
  protected void startGame() {
    hideWaitingText();
    if (painting != null) {
      painting = null;
    }

    if (game != null) {
      game = null;
    }
    painting = new Painting(this);
    FrameLayout tableLayout = (FrameLayout) findViewById(R.id.game_field_multiplayer);
    tableLayout.addView(painting);
    game = new PlayBluetooth(painting, height, width, handler, mHandler);
    game.hasMoved(false);
    tableLayout.setOnTouchListener(game);
    Log.e("MY_TAG", "startGame");
    mHandler.obtainMessage(Constants.MESSAGE_WRITE).sendToTarget();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (service != null)
      service.stop();
    service = null;
  }

  @Override
  public synchronized void onResume() {
    super.onResume();
    if (D)
      Log.e(TAG, "+ ON RESUME +");

    // Performing this check in onResume() covers the case in which BT was
    // not enabled during onStart(), so we were paused to enable it...
    // onResume() will be called when ACTION_REQUEST_ENABLE activity
    // returns.
    if (service != null) {
      // Only if the state is STATE_NONE, do we know that we haven't
      // started already
      if (service.getState() == BluetoothServices.STATE_NONE) {
        // Start the Bluetooth chat services
        service.start();
      }
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case ConductActivity.REQUEST_ENABLE_BT:
        // When the request to enable Bluetooth returns
        if (resultCode == Activity.RESULT_OK) {
          // Bluetooth is now enabled, so set up a chat session
          onResume();
        } else {
          // User did not enable Bluetooth or an error occurred
          //Log.d(TAG, "BT not enabled");
          Toast.makeText(this, R.string.bt_not_enabled_leaving,
              Toast.LENGTH_SHORT).show();
          finish();
        }
    }
  }

}
