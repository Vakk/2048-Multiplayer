package com.example.game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Valera on 31.10.2015.
 */
public class Game {
  // к≥льк≥сть кл≥тинок на пол≥ в ширину чи в висоту
  protected int numberOfCellsInField;
  // колекц≥€ €ка м≥стить дан≥ про ≥грове поле
  protected ArrayList<Cell> listOfField;
  // очки гравц€
  protected int scores;
  protected int width;
  protected int height;
  // клас €кий все малюЇ
  Painting painting;
  protected boolean looser;

  Game(Painting painting, int height, int width) {
    numberOfCellsInField = 4;
    this.painting = painting;
    this.height = height;
    this.width = width;
    painting.setSize(width / numberOfCellsInField);
    painting.setSizeField(numberOfCellsInField);
    scores = 0;
    fillList();
    painting.setList(listOfField);
    boolean looser = false;
  }

  /**
   * попереднЇ заповненн€ пол€ об'Їктами
   */
  private void fillList() {
    int value = 1;
    listOfField = new ArrayList<Cell>();
    for (int i = 0; i < numberOfCellsInField * numberOfCellsInField; i++) {
      listOfField.add(new Cell(i, value));
    }
    getRandomCell();
    getRandomCell();
  }

  /**
   * отримуЇ вс≥ можлив≥ кл≥тинки дл€ ходу
   * @return список ≥з в≥льними кл≥тинками
   */
  public ArrayList<Integer> getAvailableRandomCell() {
    ArrayList<Integer> list;
    list = new ArrayList<Integer>(numberOfCellsInField * numberOfCellsInField);
    int count = 0;
    for (Cell obj : listOfField) {
      if (obj.getValue() == 1) {
        list.add(count);
      }
      count++;
    }
    list.trimToSize();
    return list;
  }

  /**
   * встановлюЇ значенн€ випадков≥й кл≥тинц≥ ≥з тих €к≥ Ї в≥льними
   */
  public void getRandomCell() {
    Random random = new Random();
    int number = 0;
    int n = listOfField.size();
    for (int i = 0; i < numberOfCellsInField; i++) {
      number = random.nextInt(n);
      if (number < n) {
        if (listOfField.get(number).getValue() == 1) {
          listOfField.get(number).setValue(2);
          return;
        }
      }
    }
    ArrayList<Integer> list = getAvailableRandomCell();
    if (list.size() == 0) {
      looser = true;
    } else {
      int setNumber;
      number = random.nextInt(list.size());
      setNumber = list.get(number);
      listOfField.get(setNumber).setValue(2);
    }
  }
}
