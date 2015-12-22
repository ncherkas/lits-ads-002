package com.ncherkas.lits.ads002.problems.gamsrv;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

public class Node {

  public enum  Type {
    CLIENT, ROUTER
  }

  public final Type type;
  public final int id;
  public final List<Connection> connections;

  public Node(Type type, int id) {
    this(type, id, new ArrayList<>());
  }

  public Node(Type type, int id, List<Connection> connections) {
    this.type = requireNonNull(type);
    this.id = id;
    this.connections = requireNonNull(connections);
  }

  @Override
  public String toString() {
    return "\n\tNode{" +
        "type=" + type +
        ", id=" + id +
        ", connections=" + connections +
        '}';
  }
}
