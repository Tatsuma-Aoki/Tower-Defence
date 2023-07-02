package uectd.game.gameScene.gameMain;

import java.util.*;

import uectd.gameSystem.util.Vector2;

public class Graph {
    public class Vertex {
        public Vector2 position;
        public int idx;
        public boolean enabled;

        public Vertex(Vector2 position, int idx) {
            this.position = position;
            this.idx = idx;
            this.enabled = true;
        }
    }

    public class Edge {
        public int from, to;
        public double dist;

        public Edge(int from, int to, double dist) {
            this.from = from;
            this.to = to;
            this.dist = dist;
        }
    }

    public ArrayList<Vertex> vertexes; // 頂点の情報のリスト
    public ArrayList<ArrayList<Edge>> data; // 隣接リスト表現のマップデータ

    public Graph(int vNum) {
        vertexes = new ArrayList<>();
        data = new ArrayList<>();
        for (int i = 0; i < vNum; i++) {
            data.add(new ArrayList<>());
        }
    }

    public void addVertex(Vector2 position, int idx) {
        vertexes.add(new Vertex(position, idx));
    }

    public void addEdge(int from, int to) {
        double dist = Vector2.diff(vertexes.get(from).position, vertexes.get(to).position).magnitude();
        data.get(from).add(new Edge(from, to, dist));
        data.get(to).add(new Edge(to, from, dist));
    }

    public static class DistVertexPair implements Comparable<DistVertexPair> {
        double dist;
        int vertexIdx;

        public DistVertexPair(double dist, int vertex) {
            this.dist = dist;
            this.vertexIdx = vertex;
        }

        @Override
        public int compareTo(DistVertexPair o) {
            return this.dist < o.dist ? -1 : 1;
        }

    }

    public Vertex nearestVertex(Vector2 position) {
        if (vertexes.isEmpty())
            return null;
        Vertex res = vertexes.get(0);
        double minDistSquare = Double.POSITIVE_INFINITY;
        for (var vertex : vertexes) {
            double distSquare = Vector2.diff(position, vertex.position).norm();
            if (Vector2.diff(position, vertex.position).norm() < minDistSquare) {
                res = vertex;
                minDistSquare = distSquare;
            }
        }
        return res;
    }

    public Stack<Vertex> shortestPath(Vertex s, Vertex t) {
        var dist = new double[this.vertexes.size()];
        var prev = new int[this.vertexes.size()];

        PriorityQueue<DistVertexPair> pq = new PriorityQueue<>();
        pq.add(new DistVertexPair(0, s.idx));
        prev[s.idx] = -1;
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[s.idx] = 0.0;
        while (!pq.isEmpty()) {
            DistVertexPair current = pq.poll();
            int current_pos = current.vertexIdx;
            double current_dist = current.dist;
            for (Edge edge : data.get(current_pos)) {
                if (dist[edge.to] > current_dist + edge.dist) {
                    dist[edge.to] = current_dist + edge.dist;
                    pq.add(new DistVertexPair(dist[edge.to], edge.to));
                    prev[edge.to] = current_pos;
                }
            }
        }
        var res = new Stack<Vertex>();
        int current_pos = t.idx;
        while (current_pos != -1) {
            res.push(vertexes.get(current_pos));
            current_pos = prev[current_pos];
        }
        return res;
    }

}