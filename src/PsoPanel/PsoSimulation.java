package PsoPanel;


import SuperPack.Panel.Enemy;
import SuperPack.Panel.Intelligence;
import SuperPack.Panel.Robot;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PSOのシミュレータクラス
 *
 * @author Watari
 */
public class PsoSimulation extends SimulationPanel {

    private ArrayList<Intelligence> ciProcess;

    /**
     * コンストラクタ
     */
    public PsoSimulation() {
        robot = new PsoRobot[robotsNum];

        for (int i = 0; i < targetNum; i++)
            targetList.add(new Enemy());
        for (int i = 0; i < robotsNum; i++)
            robot[i] = new PsoRobot(this);
        for (int i = 0; i < robotsNum; i++)
            robot[i].setCI();
        ciProcess = new ArrayList<>();
    }

    @Override
    public void readFile(String fileString) {
        Pattern allPattern = Pattern.compile("^\\d+\\|\\d+\\|\\d+\\|"
                + "(T\\d+,\\d+)+\\|"
                + "(R\\d+,\\d+/[-]?\\d+,[-]?\\d+/\\d+(\\.\\d+)?,\\d+(\\.\\d+)?/\\d+(\\.\\d+)?,\\d+(\\.\\d+)?/\\d+(\\.\\d+)?)+$");
        //ファイル全体の正規表現チェック
        if (allPattern.matcher(fileString).matches()) {
            //捕獲数と通信回数とステップ数の抽出
            Pattern pattern = Pattern.compile("^(\\d+)\\|(\\d+)\\|(\\d+)\\|(.*)\\|(.*)$");
            Matcher matcher = pattern.matcher(fileString);
            if (matcher.matches()) {
                System.out.println("捕獲数:" + matcher.group(1));
                System.out.println("通信回数:" + matcher.group(2));
                System.out.println("ステップ数:" + matcher.group(3));
                System.out.println("ターゲット表現文字列:" + matcher.group(4));
                System.out.println("ロボット表現文字列:" + matcher.group(5));

                //捕獲数・通信回数・ステップ数の読み込み
                communication_num = Integer.parseInt(matcher.group(2));
                count = Integer.parseInt(matcher.group(3));

                //ターゲットの抽出
                String[] targetStringArray = matcher.group(4).split("T", 0);//[0]は空文字列
                targetList.clear();
                for (int i = 1; i < targetStringArray.length; i++) {
                    targetList.add(new Enemy(targetStringArray[i]));
                }

                //ロボットの抽出
                String[] robotStringArray = matcher.group(5).split("R", 0);//[R]は空文字列
                robot = new Robot[robotStringArray.length - 1];

                for (int i = 1; i < robotStringArray.length; i++) {
                    robot[i - 1] = new PsoRobot(this, robotStringArray[i]);
                }
            }

        } else {
            System.out.println("PsoSimulationファイルが正しくありません。");
        }
    }

    @Override
    /**
     * ロボットをファイルから読み込み配置する
     */
    public void readRobotFile(String str) {
        super.readRobotFile(str);
        for (int i = 0; i < robotsNum; i++)
            robot[i].setCI();
        ciProcess = new ArrayList<>();
    }

    /**
     * 毎ステップの処理
     *
     * @return ターゲットを一体でも捕獲したらTrueを返す
     */
    @Override
    public void step() {
        count++;
        for (int i = 0; i < robotsNum; i++) {
            robot[i].move();
        }
        ciProcess.add(new Intelligence(robot[0].CI));
        //ターゲットの削除
        deleteCaptureTarget();
    }

    public void reset() {
        super.reset();
        for (int i = 0; i < robotsNum; i++)
            robot[i].setCI();
        ciProcess = new ArrayList<>();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        //CIの表示
        g.setColor(Color.ORANGE);
        int ciSwingX = (int) (robot[0].CI.x * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startX);
        int ciSwingY = (int) ((PsoSimulation.size - robot[0].CI.y) * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startY);
        g.fillOval(ciSwingX - 3, ciSwingY - 3, 6, 6);
    }
}
