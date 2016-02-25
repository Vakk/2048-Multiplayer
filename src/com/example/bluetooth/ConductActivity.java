package com.example.bluetooth;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.*;

import com.example.Constants;
import com.example.MyTestBluetooth.R;
import com.example.game.Cell;
import com.example.game.MyActivity;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Valera on 28.11.2015.
 */
public class ConductActivity extends MyActivity {
  // для перевірки на помилки
  protected final String TAG = "ConductActivity";
  protected boolean D = true;
  public static final int REQUEST_ENABLE_BT = 3;
  // Ключові слова отримані з обробника
  public static final String DEVICE_NAME = "device_name";
  public static final String TOAST = "toast";
  // запам'ятовувані обєкти для подальшої роботи
  protected BluetoothServices service;
  protected PlayBluetooth game;
  protected ObjectOutputStream ous = null;
  protected ObjectInputStream ois = null;
  protected ImageView arrow;

  // назва зєднаного пристрою
  protected String mConnectedDeviceName = null;
  // змінна поля з очками противника
  protected TextView enemyScores;
  private int currentRotation =10;
  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    enemyScores = (TextView) findViewById(R.id.enemy_scores);
    arrow = (ImageView)findViewById(R.id.arrows);
  }

  protected final void setStatus(int resId) {
    final ActionBar actionBar = getActionBar();
    actionBar.setSubtitle(resId);
  }

  protected final void setStatus(CharSequence subTitle) {
    final ActionBar actionBar = getActionBar();
    actionBar.setSubtitle(subTitle);
  }

  protected Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case Constants.MESSAGE_STATE_CHANGE:
          if (D)
            Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
          switch (msg.arg1) {
            case BluetoothServices.STATE_CONNECTED:
              setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
              startGame();
              break;
            case BluetoothServices.STATE_CONNECTING:
              setStatus(R.string.title_connecting);
              break;
            case BluetoothServices.STATE_LISTEN:
            case BluetoothServices.STATE_NONE:
              setStatus(R.string.title_not_connected);
              break;
          }
          break;
        case Constants.MESSAGE_WRITE:
          onMessageWrite(msg);
          break;
        case Constants.MESSAGE_READ:
          onMessageRead(msg);
          break;
        case Constants.MESSAGE_DEVICE_NAME:
          // save the connected device's name
          mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
          Toast.makeText(getApplicationContext(),
              "Connected to " + mConnectedDeviceName,
              Toast.LENGTH_SHORT).show();
          break;
        case Constants.MESSAGE_TOAST:
          Toast.makeText(getApplicationContext(),
              msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
              .show();
          break;
      }
    }
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mHandler = null;
  }

  /**
   * Зчитує отримані з потоку байти
   * @param msg
   */
  private void onMessageRead(Message msg) {
    Log.e("MY_TAG", "onMessageRead");
    byte[] readBuf = (byte[]) msg.obj;
    if (msg.arg1 >= 333) {

      //construct a string from the valid bytes in the buffer
      //String readMessage = new String(readBuf, 0, msg.arg1);
      ByteArrayInputStream bais = null;
      try {
        int enemieScores;
        bais = new ByteArrayInputStream(readBuf);
        ois = null;
        ois = new ObjectInputStream(bais);
        try {
          enemieScores = (Integer)ois.readInt();
          enemyScores.setText(Integer.toString(enemieScores));
          ArrayList<Cell> tmp = (ArrayList<Cell>) ois.readObject();
            Log.e("MY_TAG", "onMessageRead - if");
            game.hasMoved(false);
            game.setList(tmp);

            handler.obtainMessage(MyActivity.MOVED).sendToTarget();
            service.flushingOutStream();
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
        turn();
        ois.close();

      } catch (IOException e) {
        e.printStackTrace();
        if (bais != null) {
          try {
            bais.close();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * Зчитування отриманої через блютуз інформації
   * @param msg
   */
  private void onMessageWrite(Message msg) {
    Log.e("MY_TAG", "onMessageWrite");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      service.flushingOutStream();
      if (ous != null) {

        ous.flush();
        ous.close();
        ous = null;

      }
      ous = new ObjectOutputStream(baos);
      ous.writeInt(game.getScores());
      ous.writeObject(game.getList());
      service.write(baos.toByteArray());
      handler.obtainMessage(MyActivity.MOVED, -1, -1).sendToTarget();
      game.hasMoved(true);
      service.flushingOutStream();
      turn();
    } catch (IOException e) {
      e.printStackTrace();
    }
    game.hasMoved(true);
  }

  /**
   * Поворот стрілки при зміні черги ходу
   */
  public void turn() {
    RotateAnimation anim = new RotateAnimation(currentRotation, currentRotation + 180,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
    currentRotation = (currentRotation + 180) % 360;

    anim.setInterpolator(new LinearInterpolator());
    anim.setDuration(800);
    anim.setFillEnabled(true);

    anim.setFillAfter(true);
    arrow.startAnimation(anim);
  }

  /**
   * Встановлює ігрове поле мультиплеєрного режиму і визначає розмір екрану (для промалювання ігрового поля)
   */
  @Override
  protected void preSettings() {
    setContentView(R.layout.multiplayer_game);
    DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
    width = displaymetrics.widthPixels;
    height = displaymetrics.heightPixels;
  }

  /**
   * Робить напис очікування з'єднання невидимою
   */
  protected void hideWaitingText(){
    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
    progressBar.setVisibility(View.GONE);
    TextView textView = (TextView) findViewById(R.id.textView4);
    textView.setVisibility(View.GONE);
  }
}