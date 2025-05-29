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
    }
}
