package com.ncherkas.lits.ads002.problems.bugtrk;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

public class Bugtrk {

  private final long ticketsQuantity;
  private final int maxTicketSide;
  private final int minTicketSide;

  public Bugtrk(long ticketsQuantity, int ticketWidth, int ticketHeigh) {
    this.ticketsQuantity = ticketsQuantity;
    this.maxTicketSide = Math.max(ticketHeigh, ticketWidth);
    this.minTicketSide = Math.min(ticketHeigh, ticketWidth);
  }

  public long calculateMinDeskSide() {
    long maxTicketsPerSide = Math.max(2, (int) Math.ceil(Math.sqrt(ticketsQuantity)));
    return calculateRecursively(0, maxTicketsPerSide * maxTicketSide);
  }

  private long calculateRecursively(long previousDeskSize, long deskSizeToCheck) {
    long minTicketsQuantity = getMinTicketsQuantity(deskSizeToCheck);
    long deskSizeDelta = (long) Math.ceil((double) Math.abs(previousDeskSize - deskSizeToCheck) / 2);

    if (minTicketsQuantity >= ticketsQuantity) {
      if (getMinTicketsQuantity(deskSizeToCheck - 1) < ticketsQuantity) {
        return deskSizeToCheck;
      }
      return calculateRecursively(deskSizeToCheck, deskSizeToCheck - deskSizeDelta);
    }

    return calculateRecursively(deskSizeToCheck, deskSizeToCheck + deskSizeDelta);
  }

  private long getMinTicketsQuantity(long deskSize) {
    return (deskSize / minTicketSide) * (deskSize / maxTicketSide);
  }

  /**
   * App entry point
   * @param args args
   */
  public static void main(String[] args) {
    Path inputPath, outputPath;
    if (args.length >= 2) {
      inputPath = Paths.get(args[0]);
      outputPath = Paths.get(args[1]);
    } else {
      inputPath = Paths.get("bugtrk.in");
      outputPath = Paths.get("bugtrk.out");
    }

    try (BufferedReader bufferedReader = Files.newBufferedReader(inputPath)) {
      String line  = requireNonNull(bufferedReader.readLine());
      String[] splittedLine = line.split(" ");
      long ticketsQuantity = Long.parseLong(splittedLine[0]);
      int ticketWidth = Integer.parseInt((splittedLine[1]));
      int ticketHeigh = Integer.parseInt((splittedLine[2]));

      Bugtrk bugtrk = new Bugtrk(ticketsQuantity, ticketWidth, ticketHeigh);
      long minDeskSize = bugtrk.calculateMinDeskSide();
      System.out.println("Min desk size: " + minDeskSize);

      Files.write(outputPath, String.valueOf(minDeskSize).getBytes());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read input data", ex);
    }
  }
}
