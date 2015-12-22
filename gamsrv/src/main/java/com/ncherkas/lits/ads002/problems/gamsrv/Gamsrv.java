package com.ncherkas.lits.ads002.problems.gamsrv;

import static com.ncherkas.lits.ads002.problems.gamsrv.Node.Type.CLIENT;
import static com.ncherkas.lits.ads002.problems.gamsrv.Node.Type.ROUTER;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Gamsrv {

  private static final String SEPARATOR = " ";

  public static void main(String[] args) {
    Path inputPath = args.length >= 1 ? Paths.get(args[0]) : Paths.get("gamsrv.in");
    Path outputPath = args.length >= 2 ? Paths.get(args[1]) : Paths.get("gamsrv.out");

    NetworkGraph networkGraph = readNetworkGraph(inputPath);

    List<Node> routerNodes = networkGraph.getRouterNodes();
    List<Long> maxLatencies = new ArrayList<>(routerNodes.size());
    for (Node routerNode : routerNodes) {
      Map<Node, Long> latencies = dijkstra(networkGraph, routerNode.id);

      List<Long> clientLatencies = new ArrayList<>(networkGraph.nodes.size() - routerNodes.size());
      for (Map.Entry<Node, Long> latency : latencies.entrySet()) {
        if (latency.getKey().type == CLIENT) {
          clientLatencies.add(latency.getValue());
        }
      }

      maxLatencies.add(Collections.max(clientLatencies));
    }

    Long minMaxLatency = Collections.min(maxLatencies);
    writeResult(outputPath, minMaxLatency);
  }

  private static Map<Node, Long> dijkstra(NetworkGraph networkGraph, int sourceNodeId) {
    Node sourceNode = networkGraph.nodes.get(sourceNodeId);

    Map<Node, Long> latencies = new HashMap<>(networkGraph.nodes.size());
    for (Node node : networkGraph.nodes.values()) {
      latencies.put(node, Long.MAX_VALUE);
    }
    latencies.put(sourceNode, 0L);

    Comparator<Node> nodeComparator = (n1, n2) -> latencies.get(n1).compareTo(latencies.get(n2));
    PriorityQueue<Node> nodesQueue = new PriorityQueue<>(nodeComparator);
    nodesQueue.add(sourceNode);

    while (!nodesQueue.isEmpty()) {
      Node minLatencyNode = nodesQueue.remove();

      for (Connection connection : minLatencyNode.connections) {
        Node neighborNode = connection.to;
        long alternativeLatency = latencies.get(minLatencyNode) + connection.latency;

        if (alternativeLatency < latencies.get(neighborNode)) {
          latencies.put(neighborNode, alternativeLatency);
          nodesQueue.add(neighborNode);
        }
      }
    }

    return latencies;
  }

  private static NetworkGraph readNetworkGraph(Path inputPath) {
    try (BufferedReader inputReader = Files.newBufferedReader(inputPath)) {
      String nodesCountLine = inputReader.readLine();
      int nodesCount = Integer.valueOf(nodesCountLine.split(SEPARATOR)[0]);

      Map<Integer, Node> nodes = new HashMap<>(nodesCount);

      String clientIdsLine = inputReader.readLine();
      for (String clientIdString : clientIdsLine.split(SEPARATOR)) {
        int clientId = Integer.parseInt(clientIdString);
        nodes.put(clientId, new Node(CLIENT, clientId));
      }

      for (String line; (line = inputReader.readLine()) != null;) {
        String[] lineValues = line.split(SEPARATOR);

        Node from = nodes.computeIfAbsent(Integer.parseInt(lineValues[0]),
            nodeId -> new Node(ROUTER, nodeId));
        Node to = nodes.computeIfAbsent(Integer.parseInt(lineValues[1]),
            nodeId -> new Node(ROUTER, nodeId));
        long latency = Long.parseLong(lineValues[2]);

        Connection connection = new Connection(from, to, latency);
        from.connections.add(connection);

        Connection reverseConnection = new Connection(to, from, latency);
        to.connections.add(reverseConnection);
      }
      return new NetworkGraph(nodes);
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read network graph", ex);
    }
  }

  private static void writeResult(Path outputPath, long result) {
    try {
      Files.write(outputPath, String.valueOf(result).getBytes());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to write result", ex);
    }
  }
}
