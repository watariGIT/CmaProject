package SuperPack;

public class Result implements Comparable {
    private double communication_num = 0;// 通信回数
    public int count = 0;// ステップ数
    private double distance = 0;// 移動距離
    private double density;
    private int huntedTarget = 0;

    public Result(double communication_num, int count, double distance, double density, int huntedTarget) {
        this.communication_num = communication_num;
        this.count = count;
        this.distance = distance;
        this.density = density;
        this.huntedTarget = huntedTarget;
    }

    /**
     * 結果の文字列化 例："500,600,1000"
     *
     * @return 文字列の表示
     */
    public String toString() {
        String s = "";
        s += huntedTarget+",";
        s += count + ",";
        s += communication_num + ",";
        s += distance + ",";
        s += density;

        return s;
    }

    @Override
    public int compareTo(Object o) {
        Result r = (Result) o;
        return count - r.count;
    }
}
