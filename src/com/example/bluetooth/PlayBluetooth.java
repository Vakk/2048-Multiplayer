package com.example.bluetooth;

import android.os.Handler;
import android.util.Log;

import com.example.Constants;
import com.example.game.Painting;
import com.example.game.Play;

/**
 * Created by Valera on 27.11.2015.
 */
public class PlayBluetooth extends Play {
  private Handler bHandler;

  boolean moved;

  ////////////////////////////
  public PlayBluetooth(Painting painting, int height, int width, Handler handler, Handler bHandler) {
    super(painting, height, width, handler);
    this.bHandler = bHandler;
  }

  /**
   * перевіряє чи хід можливий і робить його
   */
  @Override
  protected void doMove() {
    Log.e("MY_TAG", "doMove");
    if (!moved) {
      super.doMove();

      Log.e("MY_TAG", "doMove - write");
      bHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1).sendToTarget();
    }
  }

  public void hasMoved(boolean move) {
    this.moved = move;
  }

  public boolean isMoved() {
    return moved;
  }

}
