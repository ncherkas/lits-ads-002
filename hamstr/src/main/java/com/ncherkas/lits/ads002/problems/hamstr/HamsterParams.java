package com.ncherkas.lits.ads002.problems.hamstr;

public class HamsterParams {

  private final Integer dailyFoodAmount;
  private final Integer grediness;

  public HamsterParams(int dailyFoodAmount, int grediness) {
    this.dailyFoodAmount = dailyFoodAmount;
    this.grediness = grediness;
  }

  public Integer getDailyFoodAmount() {
    return dailyFoodAmount;
  }

  public Integer getGrediness() {
    return grediness;
  }
}
