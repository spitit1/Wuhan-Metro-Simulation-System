import java.io.IOException;
import java.util.*;
public class Test {
    public static void main(String[] args) {
    SubwaySystem subway = new SubwaySystem();
        try {
            subway.loadFromFile("subway.txt");
        } catch (IOException e) {
            System.err.println("文件读取错误: " + e.getMessage());
            return;
        }

        // 1. 测试中转站查询
        System.out.println("===== 中转站查询 =====");
        Map<String, Set<String>> transfers = subway.getTransferStations();
        transfers.forEach((station, lines) -> 
            System.out.println(station + ": " + lines)
        );
        // 2. 测试距离查询
        System.out.println("\n===== 距离查询 =====");
        try {
            String queryStation = "华中科技大学";
            double maxDistance = 1.0;
            Map<String, Map.Entry<Set<String>, Double>> nearby = subway.getStationsWithinDistance(queryStation, maxDistance);
            nearby.forEach((station, info) -> 
                System.out.printf("%s: 线路=%s, 距离=%.1f公里\n", station, info.getKey(), info.getValue())
            );
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        // 3. 测试所有路径查询
        System.out.println("\n===== 所有路径查询 =====");
        try {
            String start = "华中科技大学";
            String end = "洪山广场";
            Set<List<String>> paths = subway.findAllPaths(start, end);
            System.out.println("路径数量: " + paths.size());
            paths.forEach(path -> System.out.println(path));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        // 4. 测试最短路径查询
        System.out.println("\n===== 最短路径查询 =====");
        try {
            String start = "华中科技大学";
            String end = "洪山广场";
            PathResult result = subway.findShortestPath(start, end);
            System.out.println("最短路径: " + result.getPath());
            System.out.printf("总距离: %.1f公里\n", result.getDistance());
            
            // 5. 测试路径打印
            System.out.println("\n===== 路径打印 =====");
            subway.printPath(result.getPath());
            
            // 6. & 7. 测试票价计算
            System.out.println("\n===== 票价计算 =====");
            double fare = subway.calculateFare(result.getDistance());
            double whtFare = subway.calculateWHTFare(result.getDistance());
            System.out.printf("普通票: %.1f元\n", fare);
            System.out.printf("武汉通: %.1f元\n", whtFare);
            System.out.println("日票: 0.0元");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
