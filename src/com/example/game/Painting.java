package com.example.game;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.view.View;

import com.example.MyTestBluetooth.R;

import java.util.ArrayList;

/**
 * Created by Valera on 31.10.2015.
 */
public class Painting extends View {
  int x;
  int y;
  int value;
  int size;
  // "�������" ���� ���� �c� ����������
  Paint paint;
  // ������ ����, ��� ���� ��������������
  ArrayList<Cell> list;
  int sizeField;

  public Painting(Context context) {
    super(context);
    paint = new Paint();
    paint.setColor(Color.BLUE);
    paint.setStyle(Paint.Style.FILL);
  }

  /**
   * �������������� ����� ���������
   * @param c
   */
  @Override
  public void onDraw(Canvas c) {
    Paint paint = new Paint();
    for (Cell a : list) {
      value = a.getValue();
      // ������� �������, �� ��� ���� ����������� ������������ ����
      y = size * (a.getPosition() / sizeField);
      x = (a.getPosition() % sizeField) * size;
      // ���������� ������������
      paint.setAntiAlias(true);
      // �����
      c.drawBitmap(getPicturesWithLvl(value, size), x, y, paint);
      // ���������� ������� ������ �� ������
      paint.setTextAlign(Paint.Align.CENTER);
      // ���������� ����� ������ (1/5 �� ������ �������)
      paint.setTextSize(size / 5);
      // ���� �������� �� ������� 1
      if (a.getValue() != 1)
        // ������ ���� ������ �������
        c.drawText(Integer.toString(value), x + size / 2, y + size / 2, paint);
    }
  }
  public void setList(ArrayList<Cell> list) {
    this.list = list;
  }

  /**
   *  ������ �������� � ������ � ��������� �� ���� ��'����
   * @param a �����
   * @param size ����� �������
   * @return ������ �������� ��� ���� ���������
   */
  private Bitmap getPicturesWithLvl(int a, int size) {
    Bitmap bmp;
    switch (a) {
      case (2): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_2);
        break;
      }
      case (4): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_4);
        break;
      }
      case (8): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_8);
        break;
      }
      case (16): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_16);
        break;
      }
      case (32): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_32);
        break;
      }
      case (64): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_64);
        break;
      }
      case (128): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_128);
        break;
      }
      case (256): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_256);
        break;
      }
      case (512): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_512);
        break;
      }
      case (1024): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_1024);
        break;
      }
      case (2048): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_2048);
        break;
      }
      case (4096): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_4096);
        break;
      }
      case (8192): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_8192);
        break;
      }
      case (16384): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_16384);
        break;
      }
      case (32768): {
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ceil_32768);
        break;
      }
      default:
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.background_ceil);
    }
    Bitmap bmHalf = Bitmap.createScaledBitmap(bmp, size, size, false);
    bmp = bmHalf;
    bmp = Bitmap.createBitmap(bmp);
    return bmp;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public void setSizeField(int sizeField) {
    this.sizeField = sizeField;
  }
}
