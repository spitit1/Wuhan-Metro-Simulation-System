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

    }
}
