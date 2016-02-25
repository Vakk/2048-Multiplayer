package com.example.game;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Valera on 31.10.2015.
 */
public class Game {
  // ������� ������� �� ��� � ������ �� � ������
  protected int numberOfCellsInField;
  // �������� ��� ������ ��� ��� ������ ����
  protected ArrayList<Cell> listOfField;
  // ���� ������
  protected int scores;
  protected int width;
  protected int height;
  // ���� ���� ��� �����
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
   * �������� ���������� ���� ��'������
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
   * ������ �� ������ ������� ��� ����
   * @return ������ �� ������� ���������
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
   * ���������� �������� ��������� ������� �� ��� �� � �������
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
