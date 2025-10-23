package com.ecom.api.type;

public enum FildsISO {
  TestName(0, 0),
  F22_1(22, 1),
  F22_5(22, 5),
  F22_6(22, 6),
  F3(3, 2),
  F26(26, 4);

  private final int position;
  private final int fild;

  public int getLength() {
    return this.position;
  }

  FildsISO(int fild, int position) {
    this.fild = fild;
    this.position = position;
  }
}
