package com.ncherkas.lits.ads002.problems.discnt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Discnt {

  public double getMinTotalPrice(List<Integer> prices, int discount) {
    Objects.requireNonNull(prices);

    if (!prices.isEmpty() && discount > 0) {
      QuickSort.sort(prices, Integer::compare);

      int totalPricesQuantity = prices.size();
      // Each 3rd price is discounted
      int fullPricesQuantity = totalPricesQuantity - totalPricesQuantity / 3;

      int fullPricesSum = prices.stream()
          .limit(fullPricesQuantity).unordered()
          .mapToInt(Integer::intValue)
          .sum();
      double discountedPricesSum = prices.stream()
          .skip(fullPricesQuantity).unordered()
          .mapToDouble(price -> price * ((double) (100 - discount) / 100))
          .sum();

      return fullPricesSum + discountedPricesSum;
    }

    return Integer.valueOf(prices.stream().mapToInt(Integer::intValue).sum())
        .doubleValue();
  }

  /**
   * App entry point.
   */
  public static void main(String[] args) {
    Path inputFile, outputFile;
    if (args.length == 2) {
      inputFile = Paths.get(args[0]);
      outputFile = Paths.get(args[1]);
    } else {
      inputFile = Paths.get("discnt.in");
      outputFile = Paths.get("discnt.out");
    }

    if (Files.notExists(inputFile)) {
      throw new IllegalArgumentException("Input file '" + inputFile + "' doesn't exist");
    }

    System.out.println("Input file: " + inputFile);
    System.out.println("Output file: " + outputFile);

    try {
      List<String> lines = Files.readAllLines(inputFile);
      if (lines.size() < 2) {
        throw new IllegalArgumentException("Input file '" + inputFile + "' must contain two "
            + "lines - list of prices (separated by space) and discount");
      }

      List<Integer> prices = Arrays.stream(lines.get(0).trim().split(" "))
          .map(Integer::valueOf)
          .collect(Collectors.toList());

      int discount = Integer.valueOf(lines.get(1).trim());

      Discnt discnt = new Discnt();
      double minTotalPrice = discnt.getMinTotalPrice(prices, discount);

      Files.write(outputFile, String.format("%.2f", minTotalPrice).getBytes());
    } catch (IOException exception) {
      throw new RuntimeException("I/O error", exception);
    }
  }
}
