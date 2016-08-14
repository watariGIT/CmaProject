package SuperPack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


abstract public class SimulationPanel extends JPanel {
    public static int startX = 20;
    public static int startY = 10;
    public final static int length = 250;
    public final static int size = 1000;
    public static int robotsNum = 20;            //ロボットの初期台数
    public static int targetNum = 21;            //ターゲットの初期台数
    private final static int maxCount = 10000;//最大の捕獲時間
    public Robot[] robot;
    public ArrayList<Enemy> targetList = new ArrayList<Enemy>();

    public int communication_num = 0;            //通信回数
    protected int count = 0;                    //ステップ数

    protected SimulationPanel() {
    }

    /**
     * ディープコピーを行うメソッド
     *
     * @param s コピー元
     */
    public void copy(SimulationPanel s) {
        for (int i = 0; i < robotsNum; i++) {
            robot[i].copy(s.robot[i]);
        }

        targetList.clear();
        for (Enemy t : s.targetList) {
            targetList.add(new Enemy(t));
        }
    }

    public boolean setRobotNum(int n) {
        if (n > robotsNum)
            return false;
        robotsNum = n;
        reset();
        return true;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 255, 255));
        g2.fillRect(startX, startY, length, length);

        g2.setColor(Color.BLACK);
        g2.drawRect(startX, startY, length, length);

        //ターゲットの描写
        synchronized (targetList) {
            for (Enemy target : targetList) {
                target.paint(g2, true);
            }
        }

        for (int i = 0; i < robotsNum; i++)
            robot[i].paint(g2);
    }

    public ArrayList<Result> evaluation() {
        int tMax = targetList.size();
        int tNum = tMax;
        ArrayList<Result> resultList = new ArrayList<>();
        while (!isEnd()) {
            //1step動かす
            step();

            if (tNum != targetList.size()) {
                //たーげっとを捕獲した時の結果
                tNum = targetList.size();
                resultList.add(new Result(communication_num, count, distance(), robotDensity(5), tMax - tNum));
            }

            if (count > maxCount)
                return resultList;

        }
        reset();
        return resultList;
    }

    /**
     * 捕獲失敗時のデータの出力
     *
     * @return 失敗した時のフィールドの状況を表す文字列
     */
    public String getFailData() {
        boolean failFlag = false;
        String failData = null;
        while (!failFlag) {
            //失敗した時の状況の初期フィールドを文字列化
            failData = this.toString();
            while (!isEnd()) {
                //1step動かす
                step();

                if (count > maxCount) {
                    failFlag = true;
                    break;
                }
            }
            reset();
        }
        return failData;
    }


    private double distance() {
        double d = 0;
        for (Robot r : robot) {
            d += r.distance;
        }
        return d;
    }

    /**
     * ロボットの密度を求めるメソッド。
     * 値が高いほど分散している。
     * <p>
     * 計算式：ロボットのいるグリッドの数/全体のグリッドの数
     *
     * @return 分散度
     */
    private double robotDensity(int grid) {
        int gridCount = 0;
        int allGrid = grid * grid;

        for (int y = 0; y < size; y += size / grid) {
            for (int x = 0; x < size; x += size / grid) {
                for (Robot aRobot : robot) {
                    if (x <= aRobot.p.x && aRobot.p.x < x + size / grid
                            && y <= aRobot.p.y && aRobot.p.y < y + size / grid) {
                        gridCount++;
                        break;
                    }
                }
            }
        }

        return (double) gridCount / allGrid;
    }

    /**
     * 捕獲したターゲットを削除するメソッド
     */
    public void deleteCaptureTarget() {
        //ターゲットの削除
        synchronized (targetList) {
            Iterator itr = targetList.iterator();
            for (Robot r : robot) {
                while (itr.hasNext()) {
                    Enemy t = (Enemy) itr.next();
                    if (r.p.distance(t.p) <= 1.0) {
                        itr.remove();
                    }
                }
                itr = targetList.iterator();
            }
        }
    }

    abstract public void reset();

    /**
     * 1ステップフィールドを動かす
     */
    abstract public void step();

    /**
     * 探索が終了しているか
     *
     * @return TRUEなら終了
     */
    public boolean isEnd() {
        return targetList.size() == 0;
    }

    /**
     * 読み込んだファイルから、フィールドを再現するメソッド
     *
     * @param fileString 読み込むファイル名
     */
    public abstract void readFile(String fileString);

    /**
     * フィールドの情報を文字列で返すメソッド
     * ex: 0|0|0|T10,10|R424,257/6,3/424,257/424,257/0.9
     *
     * @return ターゲットの数|通信回数|ステップ数|ターゲットの情報|ロボットの情報|
     */
    @Override
    public String toString() {
        String string = "";

        // ターゲットの数|通信回数|ステップ数|
        string += targetList.size() + "|" + communication_num + "|" + count + "|";

        //ターゲットの情報
        for (Enemy target : targetList) {
            string += target.toString();
        }

        //ロボットの情報
        string += "|";
        for (Robot aRobot : robot) {
            string += aRobot;
        }

        return string;
    }
}
