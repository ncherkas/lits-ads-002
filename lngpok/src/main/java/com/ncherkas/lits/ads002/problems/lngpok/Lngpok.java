package com.ncherkas.lits.ads002.problems.lngpok;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

/**
 * TODO: Rewrite using binary search.
 */
public class Lngpok {

  public static void main(String[] args) {
    Path inputPath = Paths.get("lngpok.in");
    Path outputPath = Paths.get("lngpok.out");

    int[] cards = readCards(inputPath);
    MergeSort.sort(cards);

    int[] uniqueCards = getUniqueCards(cards);
    int jokersQuantity = getJokersQuantity(cards);

    int maxSequence = 0;

    for (int i = 0; i < uniqueCards.length; i++) {
      for (int j = i; j < uniqueCards.length; j++) {
        if (j != i && isValidSequence(uniqueCards, i, j, jokersQuantity)) {
          int sequenceLength = (j - i + 1) + jokersQuantity;
          if (sequenceLength > maxSequence) {
            maxSequence = sequenceLength;
          }
        }
      }
    }

    int result = uniqueCards.length == 1
        ? uniqueCards.length + jokersQuantity
        : Math.max(maxSequence, jokersQuantity);

    try {
      Files.write(outputPath, String.valueOf(result).getBytes());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to save result", ex);
    }
  }

  private static int[] readCards(Path path) {
    try (BufferedReader reader = Files.newBufferedReader(path)) {
      String line = Objects.requireNonNull(reader.readLine());
      String[] splittedLine = line.split(" ");
      int[] cards = new int[splittedLine.length];
      for (int i = 0; i < cards.length; i++) {
        cards[i] = Integer.parseInt(splittedLine[i]);
      }
      return cards;
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read cards", ex);
    }
  }

  private static boolean isValidSequence(int[] cards, int left, int right, int jokersQuantity) {
    return (cards[right] - cards[left] - 1) - (right - left - 1) <= jokersQuantity;
  }

  private static int[] getUniqueCards(int[] cards) {
    int[] uniqueCards = new int[cards.length];
    int uniqueCardsIdx = 0;

    for (int i = 0; i < cards.length; i++) {
      if (cards[i] == 0) {
        continue;
      }

      if (i == 0 || cards[i] != cards[i - 1]) {
        uniqueCards[uniqueCardsIdx++] = cards[i];
      }
    }
    return Arrays.copyOfRange(uniqueCards, 0, uniqueCardsIdx);
  }

  private static int getJokersQuantity(int[] cards) {
    int jokerCardsIdx = 0;
    while (jokerCardsIdx < cards.length && cards[jokerCardsIdx] == 0) {
      jokerCardsIdx++;
    }
    return jokerCardsIdx;
  }
}
