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
      Map<Integer, Map<String, Integer>> wordsByLengths = new TreeMap<>();
      for (String word : words) {
        int wordLength = word.length();

        if (!wordsByLengths.containsKey(wordLength)) {
          wordsByLengths.put(wordLength, new HashMap<>());
        }

        wordsByLengths.get(wordLength).put(word, 1);
      }


      Map.Entry<Integer, Map<String, Integer>> prevWordsByLengthEntry = null;
      int maxChainSize = 1;

      for (Map.Entry<Integer, Map<String, Integer>> wordsByLengthEntry
          : wordsByLengths.entrySet()) {

        Map<String, Integer> wordsByLength = wordsByLengthEntry.getValue();
        if (prevWordsByLengthEntry != null) {
          Map<String, Integer> prevWordsByLength = prevWordsByLengthEntry.getValue();

          for (Map.Entry<String, Integer> wordEntry : wordsByLength.entrySet()) {
            String word = wordEntry.getKey();
            StringBuilder wordBuilder = new StringBuilder(word);

            for (int charPos = 0; charPos < word.length(); charPos++) {
              char charToRemove = wordBuilder.charAt(charPos);
              String wordToCheck = wordBuilder.deleteCharAt(charPos).toString();

              if (prevWordsByLength.containsKey(wordToCheck)) {
                int currentChainSize = prevWordsByLength.get(wordToCheck) + 1;
                wordsByLength.put(word, currentChainSize);

                if (currentChainSize > maxChainSize) {
                  maxChainSize = currentChainSize;
                }
              }

              wordBuilder.insert(charPos, charToRemove);
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

  private static List<String> readInput(Path inputPath) {
    try {
      List<String> lines = Files.readAllLines(inputPath);
      int wordsCount = Integer.parseInt(lines.get(0));
      return lines.subList(1, wordsCount + 1);
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read input file", ex);
    }
  }
}
