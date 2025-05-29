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