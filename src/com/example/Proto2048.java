package com.example;

import android.app.Application;
import android.os.StrictMode;

import com.example.MyTestBluetooth.BuildConfig;
public class Proto2048 extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build());

      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
              .detectAll()
          .penaltyLog()
          .build());
    }
  }
}
