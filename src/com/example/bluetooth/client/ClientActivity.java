package com.example.bluetooth.client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.Constants;
import com.example.MyTestBluetooth.R;
import com.example.bluetooth.BluetoothServices;
import com.example.bluetooth.ConductActivity;
import com.example.bluetooth.DeviceListActivity;
import com.example.bluetooth.PlayBluetooth;
import com.example.game.Painting;

/**
 * Created by Valera on 25.11.2015.
 */

/**
 * Основне вікно клієнта
 */
public class ClientActivity extends ConductActivity {
  // Адаптер пристрою
  private BluetoothAdapter bluetooth;
  // змінні для activity
  private static final int REQUEST_CONNECT_DEVICE = 2;
  private static final int REQUEST_ENABLE_BT = 3;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    hideWaitingText();
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
    /////////   Search devices to connect ////////////////////
    Intent serverIntent = null;
    serverIntent = new Intent(this, DeviceListActivity.class);
    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
  }

  /**
   * Починаємо гру, встановлюємо змінні
   *
   */
  @Override
  protected void startGame() {
    if (painting != null) {
      painting = null;
    }
    if (game != null) {
      game = null;
    }
    painting = new Painting(this);
    // знаходимо місце для ігрового поля
    FrameLayout tableLayout = (FrameLayout) findViewById(R.id.game_field_multiplayer);
   // додаємо сюди клас який промальовує
    tableLayout.addView(painting);
    game = new PlayBluetooth(painting, height, width, handler, mHandler);
    game.hasMoved(false);
    //mHandler.obtainMessage(ConductActivity.MESSAGE_READ).sendToTarget();
    tableLayout.setOnTouchListener(game);
  }

  @Override
  public void onStop() {
    super.onStop();
    if (service != null) {
      service.stop();
      service = null;
    }
  }

  /**
   * при виході - очищаємо змінні, перериваємо з'єднання
   */
  @Override
  public void onDestroy() {
    super.onDestroy();
    if (service != null) {
      service.stop();
      service = null;
    }
  }

  /**
   * результати виконуючих вікон
   */
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_CONNECT_DEVICE:
        // When DeviceListActivity returns with a device to connect
        if (resultCode == Activity.RESULT_OK) {
          connectDevice(data);
        } else {
          Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
          finish();
        }
        break;
      case REQUEST_ENABLE_BT:
        // When the request to enable Bluetooth returns
        if (resultCode != Activity.RESULT_OK) {
          // User did not enable Bluetooth or an error occurred
          Toast.makeText(this, R.string.bt_not_enabled_leaving,
              Toast.LENGTH_SHORT).show();
          finish();
        }
    }
  }

  /**
   * З'єднання зі знайденим пристроєм
   * @param data
   */
  private void connectDevice(Intent data) {
    // Get the device MAC address
    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
    // Get the BluetoothDevice object
    BluetoothDevice device = bluetooth.getRemoteDevice(address);
    // Attempt to connect to the device
    service.connect(device, false);
    startGame();

    Log.e("MY_TAG", "connectDevice");
    mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1).sendToTarget();

  }
}
