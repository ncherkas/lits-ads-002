package com.ncherkas.lits.ads002.problems.career;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Career {

  public static void main(String[] args) {
    Path inputPath = args.length >= 1 ? Paths.get(args[0]) : Paths.get("career.in");
    Path outputPath = args.length >= 2 ? Paths.get(args[1]) : Paths.get("career.out");

    int[][] hierarchy = readInput(inputPath);
    int maxCareerValue = hierarchy[0][0];

    for (int i = 1; i < hierarchy.length; i++) {
      int[] prevLevel = hierarchy[i - 1];
      int[] currLevel = hierarchy[i];

      for (int j = 0; j < currLevel.length; j++) {
        currLevel[j] += Math.max(
            prevLevel[Math.max(j - 1, 0)],
            prevLevel[Math.min(j, prevLevel.length - 1)]
        );

        int currMaxCareerValue = currLevel[j];
        if (maxCareerValue < currMaxCareerValue) {
          maxCareerValue = currMaxCareerValue;
        }
      }
    }

    try {
      Files.write(outputPath, String.valueOf(maxCareerValue).getBytes());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to write into output file", ex);
    }
  }

  private static int[][] readInput(Path inputPath) {
    try (Scanner scanner = new Scanner(inputPath)) {
      int hierarchySize = scanner.nextInt();

      int[][] hierarchy = new int[hierarchySize][];
      int i = 0;

      while (scanner.hasNextInt()) {
        hierarchy[i] = new int[i + 1];
        for (int j = 0; j < i + 1; j++) {
          hierarchy[i][j] = scanner.nextInt();
        }
        i++;
      }

      return hierarchy;
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read input file", ex);
    }
  }
}
