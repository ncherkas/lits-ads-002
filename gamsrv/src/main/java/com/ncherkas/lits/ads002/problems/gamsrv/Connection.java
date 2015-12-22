package com.ncherkas.lits.ads002.problems.gamsrv;

import static java.util.Objects.requireNonNull;

public class Connection {

  public final Node from;
  public final Node to;
  public final long latency;

  public Connection(Node from, Node to, long latency) {
    this.from = requireNonNull(from);
    this.to = requireNonNull(to);
    this.latency = latency;
  }

  @Override
  public String toString() {
    return "{" + from.id +
        "-" + latency + "->" + to.id +
        '}';
  }
}
