package com.example.game;

import java.io.Serializable;

/**
 * Created by Valera on 31.10.2015.
 */
public class Cell implements Serializable {
  // значення клітинки
  int value;
  // позиція клітинки
  int position;

  public Cell(int position, int value) {
    this.position = position;
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getPosition() {
    return position;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Cell)) return false;

    Cell cell = (Cell) o;

    return position == cell.position
        && value == cell.value;
  }

  @Override
  public int hashCode() {
    int result = value;
    result = 31 * result + position;
    return result;
  }
}
