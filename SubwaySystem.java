import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
// 站点类
class Station {
    private String name;
    private Set<String> lines;

    public Station(String name) {
        this.name = name;
        this.lines = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<String> getLines() {
        return lines;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    @Override
    public String toString() {
        return name;
    }
}

// 边类（表示站点间连接）
class Edge {
    private String target;
    private double distance;
    private String line;

    public Edge(String target, double distance, String line) {
        this.target = target;
        this.distance = distance;
        this.line = line;
    }

    public String getTarget() {
        return target;
    }

    public double getDistance() {
        return distance;
    }

    public String getLine() {
        return line;
    }
}

// 路径结果类（用于存储路径信息）
class PathResult {
    private List<String> path;
    private double distance;

    public PathResult(List<String> path, double distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<String> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }
}

class SubwaySystem {
    private Map<String, Station> stations;
    private Map<String, List<Edge>> graph;

    public SubwaySystem() {
        stations = new HashMap<>();
        graph = new HashMap<>();
    }

    // 从文件加载地铁数据
    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length < 4) continue;
                
                String lineName = parts[0];
                String stationA = parts[1];
                String stationB = parts[2];
                double distance = Double.parseDouble(parts[3]);
                
                // 添加站点和线路信息
                stations.computeIfAbsent(stationA, Station::new).addLine(lineName);
                stations.computeIfAbsent(stationB, Station::new).addLine(lineName);
                
                // 添加边（双向）
                graph.computeIfAbsent(stationA, k -> new ArrayList<>()).add(new Edge(stationB, distance, lineName));
                graph.computeIfAbsent(stationB, k -> new ArrayList<>()).add(new Edge(stationA, distance, lineName));
            }
        }
    }
    // 1. 获取所有中转站（至少两条线路）
    public Map<String, Set<String>> getTransferStations() {
        Map<String, Set<String>> result = new HashMap<>();
        stations.forEach((name, station) -> {
            if (station.getLines().size() >= 2) {
                result.put(name, station.getLines());
            }
        });
        return result;
    }
    // 2. 获取距离小于n的所有站点
    public Map<String, Map.Entry<Set<String>, Double>> getStationsWithinDistance(String start, double n) {
        if (!stations.containsKey(start)) 
            throw new IllegalArgumentException("站点不存在: " + start);
        
        Map<String, Double> minDist = new HashMap<>();
        minDist.put(start, 0.0);
        
        PriorityQueue<Map.Entry<String, Double>> queue = new PriorityQueue<>(Map.Entry.comparingByValue());
        queue.add(new AbstractMap.SimpleEntry<>(start, 0.0));
        
        Map<String, Map.Entry<Set<String>, Double>> result = new HashMap<>();
        
        while (!queue.isEmpty()) {
            Map.Entry<String, Double> entry = queue.poll();
            String current = entry.getKey();
            double currentDist = entry.getValue();
            
            if (currentDist > n) continue;
            if (!current.equals(start)) { // 排除起点自身
                result.put(current, new AbstractMap.SimpleEntry<>(
                    stations.get(current).getLines(), 
                    currentDist
                ));
            }
            
            for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                String neighbor = edge.getTarget();
                double newDist = currentDist + edge.getDistance();
                
                if (newDist <= n && newDist < minDist.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    minDist.put(neighbor, newDist);
                    queue.add(new AbstractMap.SimpleEntry<>(neighbor, newDist));
                }
            }
        }
        return result;
    }
    // 3. 查找所有路径（无重复站点）
    public Set<List<String>> findAllPaths(String start, String end) {
        if (!stations.containsKey(start) || !stations.containsKey(end))
            throw new IllegalArgumentException("站点不存在");
        
        Set<List<String>> paths = new HashSet<>();
        dfs(start, end, new LinkedHashSet<>(), paths);
        return paths;
    }

    private void dfs(String current, String end, Set<String> visited, Set<List<String>> paths) {
        visited.add(current);
        
        if (current.equals(end)) {
            paths.add(new ArrayList<>(visited));
        } else {
            for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                String neighbor = edge.getTarget();
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, end, visited, paths);
                }
            }
        }
        
        visited.remove(current);
    }
    // 4. 查找最短路径（Dijkstra算法）
    public PathResult findShortestPath(String start, String end) {
        if (!stations.containsKey(start) || !stations.containsKey(end))
            throw new IllegalArgumentException("站点不存在");
        
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<Map.Entry<String, Double>> queue = new PriorityQueue<>(Map.Entry.comparingByValue());
        
        // 初始化
        stations.keySet().forEach(station -> dist.put(station, Double.MAX_VALUE));
        dist.put(start, 0.0);
        queue.add(new AbstractMap.SimpleEntry<>(start, 0.0));
        
        while (!queue.isEmpty()) {
            Map.Entry<String, Double> entry = queue.poll();
            String current = entry.getKey();
            
            if (current.equals(end)) break;
            if (entry.getValue() > dist.get(current)) continue;
            
            for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                String neighbor = edge.getTarget();
                double newDist = dist.get(current) + edge.getDistance();
                
                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    prev.put(neighbor, current);
                    queue.add(new AbstractMap.SimpleEntry<>(neighbor, newDist));
                }
            }
        }
        
        // 回溯路径
        List<String> path = new LinkedList<>();
        for (String at = end; at != null; at = prev.get(at)) {
            path.add(0, at);
        }
        
        return new PathResult(path, dist.get(end));
    }
    // 5. 打印路径（简洁格式）
    public void printPath(List<String> path) {
        if (path.size() < 2) {
            System.out.println("无效路径");
            return;
        }
        
        String currentLine = getLineBetween(path.get(0), path.get(1));
        String segmentStart = path.get(0);
        StringBuilder output = new StringBuilder();
        
        for (int i = 1; i < path.size(); i++) {
            String line = (i < path.size() - 1) ? getLineBetween(path.get(i), path.get(i + 1)) : null;
            
            if (i == path.size() - 1 || !currentLine.equals(line)) {
                output.append("乘坐").append(currentLine).append("从").append(segmentStart).append("到").append(path.get(i));
                if (i < path.size() - 1) {
                    output.append("，换乘");
                    currentLine = line;
                    segmentStart = path.get(i);
                }
            }
        }
        
        System.out.println(output);
    }

    // 获取两站点间的线路
    private String getLineBetween(String a, String b) {
        for (Edge edge : graph.get(a)) {
            if (edge.getTarget().equals(b)) {
                return edge.getLine();
            }
        }
        return null;
    }
}