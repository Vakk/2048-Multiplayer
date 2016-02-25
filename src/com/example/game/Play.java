package com.example.game;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

import java.util.ArrayList;

/**
 * Created by Valera on 02.10.2015.
 */
public class Play extends Game implements View.OnTouchListener {
  float x = 0, y;
  float dx = 0, dy;
  protected Handler handler;
  ///////////////////////////
  final int DIRECTION_UP = 1;
  final int DIRECTION_RIGHT = 2;
  final int DIRECTION_DOWN = 3;
  final int DIRECTION_LEFT = 4;
  final int MIN_DISTANCE = 100;
  /////////////////////////////
  int direction;
  protected boolean moved;

  public Play(Painting painting, int height, int width, Handler handler) {
    super(painting, height, width);
    this.handler = handler;
  }

  /**
   * �������� ������� ���������� �� ������� �������� ����
   */
  void correction() {
    float differenceX = x - dx;
    float differenceY = y - dy;
    if (Math.abs(differenceX) > MIN_DISTANCE || Math.abs(differenceY) > MIN_DISTANCE) {
      if (differenceX < 0 && Math.abs(differenceX) > Math.abs(differenceY)) {
        direction = DIRECTION_RIGHT;
      } else if (differenceX > 0 && Math.abs(differenceX) > Math.abs(differenceY)) {
        direction = DIRECTION_LEFT;
      } else if (differenceY < 0 && Math.abs(differenceY) > Math.abs(differenceX)) {
        direction = DIRECTION_DOWN;
      } else if (differenceY > 0 && Math.abs(differenceY) > Math.abs(differenceX)) {
        direction = DIRECTION_UP;
      }
      }
      move();
    }

  /**
   * ������ ��� �������� �� �������� ����
   */
  protected void move() {
    moved = false;
    switch (direction) {
      case DIRECTION_UP: {
        doMoveUp();
        break;
      }
      case DIRECTION_DOWN: {
       doMoveDown();
        break;
      }
      case DIRECTION_LEFT: {
        doMoveLeft();
        break;
      }
      case DIRECTION_RIGHT: {
        doMoveRight();
        break;
      }
    }
    doMove();
  }
  protected void doMove() {
    // ����������� ����
    byte[] send = Integer.toString(scores).getBytes();
    handler.obtainMessage(MyActivity.NEED_DRAW_SCORE, -1, -1, send).sendToTarget();
    if (moved) {
      getRandomCell();
      handler.obtainMessage(MyActivity.MOVED, -1, -1, -1).sendToTarget();
      if (looser) {
        handler.obtainMessage(MyActivity.PLAYER_LOSE, -1, -1, 0).sendToTarget();
      }
    }
    if (!moved && looser) {
      handler.obtainMessage(MyActivity.PLAYER_LOSE, -1, -1, 0).sendToTarget();
    }
  }
  private void doMoveRight(){
    int previousValue;
    int currentValue;
    for (int k = 0; k < numberOfCellsInField - 1; k++) {
      for (int i = 0; i < listOfField.size() - 1; i++) {
        previousValue = listOfField.get(i + 1).getValue();
        currentValue = listOfField.get(i).getValue();
        if (((i + 1) % numberOfCellsInField) != 0) {
          if (previousValue == 1 && currentValue != 1) {
            listOfField.get(i + 1).setValue(currentValue);
            listOfField.get(i).setValue(1);
            moved = true;
          } else if ((previousValue == currentValue) && currentValue != 1) {
            listOfField.get(i + 1).setValue(currentValue * 2);
            listOfField.get(i).setValue(1);
            scores += currentValue * 2;
            moved = true;
          }
        }
      }
    }
  }
  private void doMoveLeft(){
    int previousValue;
    int currentValue;
    for (int k = 0; k < numberOfCellsInField - 1; k++) {
      for (int i = listOfField.size() - 1; i > 0; i--) {
        previousValue = listOfField.get(i - 1).getValue();
        currentValue = listOfField.get(i).getValue();
        if (i % numberOfCellsInField != 0) {
          if (previousValue == 1 && currentValue != 1) {
            listOfField.get(i - 1).setValue(currentValue);
            listOfField.get(i).setValue(1);
            moved = true;
          } else if ((previousValue == currentValue) && currentValue != 1) {
            listOfField.get(i - 1).setValue(currentValue * 2);
            listOfField.get(i).setValue(1);
            scores += currentValue * 2;
            moved = true;
          }
        }
      }
    }
  }
  private void doMoveDown(){
    int previousValue;
    int currentValue;
    for (int k = 0; k < numberOfCellsInField - 1; k++) {
      for (int i = 0; i < listOfField.size() - numberOfCellsInField; i++) {
        previousValue = listOfField.get(i + numberOfCellsInField).getValue();
        currentValue = listOfField.get(i).getValue();
        if (previousValue == 1 && currentValue != 1) {
          listOfField.get(i + numberOfCellsInField).setValue(currentValue);
          listOfField.get(i).setValue(1);
          moved = true;
        } else if ((previousValue == currentValue) && currentValue != 1) {
          listOfField.get(i + numberOfCellsInField).setValue(currentValue * 2);
          listOfField.get(i).setValue(1);
          scores += currentValue * 2;
          moved = true;
        }
      }
    }
  }
  private void doMoveUp(){
    int previousPosition;
    int previousValue;
    int currentValue;
    for (int k = 0; k < numberOfCellsInField - 1; k++) {
      for (int i = listOfField.size() - 1; i >= numberOfCellsInField; i--) {
        previousPosition = i - numberOfCellsInField;
        previousValue = listOfField.get(previousPosition).getValue();
        currentValue = listOfField.get(i).getValue();
        if (previousValue == 1 && currentValue != 1) {
          listOfField.get(previousPosition).setValue(currentValue);
          listOfField.get(i).setValue(1);
          moved = true;
        } else if ((previousValue == currentValue) && currentValue != 1) {
          listOfField.get(previousPosition).setValue(currentValue * 2);
          listOfField.get(i).setValue(1);
          scores += currentValue * 2;
          moved = true;
        }
      }

    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN: {
        x = event.getX();
        y = event.getY();
        return true;
      }
      case MotionEvent.ACTION_UP: {
        dx = event.getX();
        dy = event.getY();
        correction();
        return true;
      }
    }
    return true;
  }
  public synchronized void setList(ArrayList<Cell> list) {
    listOfField = list;
    painting.setList(list);
  }
  public int getScores(){
    return scores;
  }
  public synchronized ArrayList<Cell> getList() {
    return listOfField;
  }
}
