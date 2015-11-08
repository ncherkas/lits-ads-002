package com.ncherkas.lits.ads002.problems.hamstr;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Hamstr {
  /**
   * App entry point.
   */
  public static void main(String[] args) {
    Path inputFile, outputFile;
    if (args.length == 2) {
      inputFile = Paths.get(args[0]);
      outputFile = Paths.get(args[1]);
    } else {
      inputFile = Paths.get("hamstr.in");
      outputFile = Paths.get("hamstr.out");
    }

    System.out.println("Input file: " + inputFile);
    System.out.println("Output file: " + outputFile);

    try(BufferedReader inputReader = Files.newBufferedReader(inputFile)) {
      String foodAvailableAmountLine = inputReader.readLine();
      Objects.requireNonNull(foodAvailableAmountLine, "1st line must provide available amount of food");
      int dailyFoodAvailableAmount = Integer.valueOf(foodAvailableAmountLine);

      String hamstersQuantityLine = inputReader.readLine();
      Objects.requireNonNull(hamstersQuantityLine, "2nd line must provide hamsters quantity");
      int hamstersTotalQuantity = Integer.valueOf(hamstersQuantityLine);

      int hamstersAllowedQuantity;
      if (hamstersTotalQuantity > 0) {
        List<HamsterParams> hamsters = new ArrayList<>(hamstersTotalQuantity);
        for (int hamsterIdx = 0; hamsterIdx < hamstersTotalQuantity; hamsterIdx++) {
          String inputLine = inputReader.readLine();
          Objects.requireNonNull(inputLine, "Incorrect hamsters quantity provided");

          int separatorPos = inputLine.indexOf(" ");
          if (separatorPos <= 0 || separatorPos == inputLine.length() - 1) {
            throw new RuntimeException("Line '" + inputLine + "' has wrong format");
          }

          int dailyFoodAmount = Integer.valueOf(inputLine.substring(0, separatorPos));
          int greediness = Integer.valueOf(inputLine.substring(separatorPos + 1, inputLine.length()));

          hamsters.add(new HamsterParams(dailyFoodAmount, greediness));
        }

        Comparator<HamsterParams> hamsterFoodAmountComparator = (hamster1, hamster2) -> {
          int foodAmountComparison = hamster1.getDailyFoodAmount().compareTo(hamster2.getDailyFoodAmount());
          return foodAmountComparison != 0
              ? foodAmountComparison
              : hamster1.getGrediness().compareTo(hamster2.getGrediness());
        };
        QuickSort.sort(hamsters, hamsterFoodAmountComparator);
        int hamstersQuantityComparingFood = calculateHamstersQuantity(hamsters, dailyFoodAvailableAmount);

        Comparator<HamsterParams> hamsterGreedinessComparator = (hamster1, hamster2) -> {
          int greedinessComparison = hamster1.getGrediness().compareTo(hamster2.getGrediness());
          return greedinessComparison != 0
              ? greedinessComparison
              : hamster1.getDailyFoodAmount().compareTo(hamster2.getDailyFoodAmount());
        };
        QuickSort.sort(hamsters, hamsterGreedinessComparator);
        int hamstersQuantityComparingGreediness = calculateHamstersQuantity(hamsters, dailyFoodAvailableAmount);

        hamstersAllowedQuantity = Math.max(hamstersQuantityComparingFood, hamstersQuantityComparingGreediness);
      } else {
        hamstersAllowedQuantity = 0;
      }

      Files.write(outputFile, String.valueOf(hamstersAllowedQuantity).getBytes());
    } catch (IOException exception) {
      throw new RuntimeException("I/O error", exception);
    }
  }

  private static int calculateHamstersQuantity(List<HamsterParams> hamsters, int dailyFoodAvailableAmount) {
    if (hamsters.size() == 1) {
      return hamsters.get(0).getDailyFoodAmount() <= dailyFoodAvailableAmount ? 1 : 0;
    }

    int availableHamstersCounter = 0;

    // TODO: simplify & refactor
    for (int hamsterIdx = 0, dailyFoodAmountSum = 0, greedinessSum = 0, dailyFoodTotalAmount = 0;
        hamsterIdx < hamsters.size() && dailyFoodTotalAmount <= dailyFoodAvailableAmount;
        hamsterIdx++) {

      HamsterParams hamsterParams = hamsters.get(hamsterIdx);

      dailyFoodAmountSum += hamsterParams.getDailyFoodAmount();
      greedinessSum += hamsterParams.getGrediness();

      dailyFoodTotalAmount = dailyFoodAmountSum + (greedinessSum * hamsterIdx);

      availableHamstersCounter = dailyFoodTotalAmount <= dailyFoodAvailableAmount
          ? hamsterIdx + 1
          : hamsterIdx;
    }

    return availableHamstersCounter;
  }
}
