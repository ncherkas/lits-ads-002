package com.ncherkas.lits.ads002.problems.wchain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Wchain {

  public static void main(String[] args) {
    long startMillis = System.currentTimeMillis();

    Path inputPath = args.length >= 1 ? Paths.get(args[0]) : Paths.get("wchain.in");
    Path outputPath = args.length >= 2 ? Paths.get(args[1]) : Paths.get("wchain.out");

    List<String> words = readInput(inputPath);
    if (!words.isEmpty()) {
      Map<Integer, List<Pair<String, Integer>>> wordsByLengths = new TreeMap<>();
      for (String word : words) {
        int wordLength = word.length();

        if (!wordsByLengths.containsKey(wordLength)) {
          wordsByLengths.put(wordLength, new ArrayList<>(2000)); // In order to resize less
        }

        Pair<String, Integer> pair = new Pair<>();
        pair.setKey(word);
        pair.setValue(1);
        wordsByLengths.get(wordLength).add(pair);
      }

      int maxChainSize = 1;
      Map.Entry<Integer, List<Pair<String, Integer>>> prevWordsByLengthEntry = null;

      for (Map.Entry<Integer, List<Pair<String, Integer>>> wordsByLengthEntry
          : wordsByLengths.entrySet()) {

        List<Pair<String, Integer>> wordsByLength = wordsByLengthEntry.getValue();
        int wordsByLengthSize = wordsByLength.size();

        if (prevWordsByLengthEntry != null) {
          List<Pair<String, Integer>> prevWordsByLength = prevWordsByLengthEntry.getValue();
          int prevWordsByLengthSize = prevWordsByLength.size();

          // Will work faster than iterator
          for (int prevIdx = 0; prevIdx < prevWordsByLengthSize; prevIdx++) {
            Pair<String, Integer> prevWordPair = prevWordsByLength.get(prevIdx);

            for (int currIdx = 0; currIdx < wordsByLengthSize; currIdx++) {
              Pair<String, Integer> currentWordPair = wordsByLength.get(currIdx);

              String word = currentWordPair.getKey();
              String prevWord = prevWordPair.getKey();

              Integer prevWordPairCounter = prevWordPair.getValue();
              Integer currentWordPairCounter = currentWordPair.getValue();

              if (wordsChained(prevWord, word) && currentWordPairCounter <= prevWordPairCounter) {
                int incrementedCounter = prevWordPairCounter + 1;
                currentWordPair.setValue(incrementedCounter);

                if (incrementedCounter > maxChainSize) {
                  maxChainSize = incrementedCounter;
                }
              }
            }
          }
        }

        prevWordsByLengthEntry = wordsByLengthEntry;
      }

      try {
        System.out.println("Result: " + maxChainSize);
        Files.write(outputPath, String.valueOf(maxChainSize).getBytes());
      } catch (IOException ex) {
        throw new RuntimeException("Failed to write into output file", ex);
      }

      System.out.println("Time spent: " + (System.currentTimeMillis() - startMillis));
    }
  }

  private static boolean wordsChained(String word1, String word2) {
    int word1Length = word1.length();
    int word2Length = word2.length();

    int largerWordLength = Math.max(word1Length, word2Length);
    int smallerWordLength = Math.min(word1Length, word2Length);

    if (word1Length == 0 || word2Length == 0 || largerWordLength - smallerWordLength != 1) {
      return false;
    }

    String largerWord = word1Length > word2Length ? word1 : word2;
    String smallerWord = word1Length < word2Length ? word1 : word2;

    for (int charCounter = 0, diffCounter = 0; charCounter < largerWordLength; charCounter++) {
      int smallerWordCharPos = charCounter - diffCounter;

      if (smallerWordCharPos < smallerWordLength
          && largerWord.charAt(charCounter) != smallerWord.charAt(smallerWordCharPos)
          && ++diffCounter > 1) {

        return false;
      }
    }

    return true;
  }

  private static List<String> readInput(Path inputPath) {
    try {
      List<String> lines = Files.readAllLines(inputPath);
      int wordsCount = Integer.parseInt(lines.get(0));
      return lines.subList(1, wordsCount + 1);
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read input file", ex);
    }
  }

  private static class Pair<K, V> {

    private K key;
    private V value;

    public K getKey() {
      return key;
    }

    public void setKey(K key) {
      this.key = key;
    }

    public V getValue() {
      return value;
    }

    public void setValue(V value) {
      this.value = value;
    }
  }
}
