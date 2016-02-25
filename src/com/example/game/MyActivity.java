package com.example.game;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.MyTestBluetooth.R;

import java.util.ArrayList;

public class MyActivity extends Activity {
  ////////////////////////////
  public static final int PLAYER_LOSE = 0;
  public static final int PLAYER_WIN = 1;
  public static final int MOVED = 2;
  public static final int NEED_DRAW_SCORE = 3;
  //////////////////////////////////
  private boolean drawedLose;
  protected int width;
  protected int height;
  protected Painting painting;
  protected Play game;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    preSettings();

  }

  protected void preSettings() {
    setContentView(R.layout.game_layout);
    drawedLose = false;
    DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
    width = displaymetrics.widthPixels;
    height = displaymetrics.heightPixels;
    Button restart = (Button) findViewById(R.id.restart_button);
    restart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startGame();
        handler.obtainMessage(NEED_DRAW_SCORE, -1, -1, "0".getBytes()).sendToTarget();
      }
    });
    startGame();
  }
  protected void startGame() {
    if (painting != null) {
      painting = null;
    }
    if (game != null) {
      game = null;
    }

    FrameLayout tableLayout = (FrameLayout) findViewById(R.id.game_field);
    tableLayout.removeAllViewsInLayout();
    painting = new Painting(this);
    tableLayout.addView(painting);
    game = new Play(painting, height, width, handler);
    tableLayout.setOnTouchListener(game);
    System.gc();
  }

  /**
   * обробник подій (малювання очок, перемалювання поля і вібрація після ходу)
   */
  protected Handler handler = new Handler() {
    @Override
    public void handleMessage(android.os.Message msg) {
      switch (msg.what) {
        case (NEED_DRAW_SCORE): {
          TextView textView = (TextView) findViewById(R.id.scores);
          byte[] readBuf = (byte[]) msg.obj;
          //construct a string from the valid bytes in the buffer
          String readMessage = new String(readBuf);
          textView.setText(readMessage);
          Log.v("View handler",readMessage);
          break;
        }
        case (MOVED): {
          Log.e("MY_TAG", "handler - moved");
          painting.postInvalidate();
         Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
         v.vibrate(20);
          break;
        }
        case (PLAYER_LOSE): {
          if (!drawedLose) {
            drawLoseScreen();
            drawedLose = true;
          }
          break;
        }
      }
    }
  };

  /**
   * Малює програшне вікно
   */
  public void drawLoseScreen() {
    FrameLayout tableLayout = (FrameLayout) findViewById(R.id.game_field);
    tableLayout.addView(new View(getBaseContext()) {
      @Override
      public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(200, 255, 200, 100);
        canvas.drawPaint(paint);
        paint.setColor(Color.CYAN);
        paint.setTextSize(getResources().getDimension(R.dimen.text_in_cells) + 10);
        canvas.drawText("YOU LOSE", width / 3, width / 2, paint);
      }
    });
    tableLayout.setOnTouchListener(null);
  }

  protected Handler getHandler() {
    return handler;
  }
}