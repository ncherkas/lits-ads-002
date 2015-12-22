package com.ncherkas.lits.ads002.problems.gamsrv;

import static com.ncherkas.lits.ads002.problems.gamsrv.Node.Type.ROUTER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkGraph {

  public final Map<Integer, Node> nodes;

  public NetworkGraph() {
    this(new HashMap<>());
  }

  public NetworkGraph(Map<Integer, Node> nodes) {
    this.nodes = nodes;
  }

  public List<Node> getRouterNodes() {
    List<Node> routerNodes = new ArrayList<>(nodes.size());
    for (Node node : nodes.values()) {
      if (node.type == ROUTER) {
        routerNodes.add(node);
      }
    }
    return routerNodes;
  }

  @Override
  public String toString() {
    return "NetworkGraph{" +
        "nodes=" + nodes.values() +
        '}';
  }
}
