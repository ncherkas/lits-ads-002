package com.ncherkas.lits.ads002.problems.discnt;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class DiscntTest {

  @DataProvider(name = "testCases")
  public Object[][] getTestCases() {
    return new Object[][] {
      new Object[] {
          Arrays.asList(100, 50, 30, 20, 17),
          10,
          207.0
      },
      new Object[] {
          Arrays.asList(1, 7, 3, 4, 6, 5, 2),
          100,
          15.0
      },
      new Object[] {
          Arrays.asList(1, 1, 1),
          33,
          2.67
      },
    };
  }

  @Test(dataProvider = "testCases")
  public void testGetMinTotalPrice(List<Integer> prices, int discount,
      double expectedMinTotalPrice) {

    Discnt discnt = new Discnt();
    double minTotalPrice = discnt.getMinTotalPrice(prices, discount);
    Assert.assertEquals(minTotalPrice, expectedMinTotalPrice);
  }
}
