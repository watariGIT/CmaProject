package AgentPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PsoPanel.PsoSimulation;
import SuperPack.Robot;
import SuperPack.SimulationPanel;
//FIXME distance -> fitness function
class Agent {
    private Point CI;
    public AgentRobot2 arobot;
    private ArrayList<AgentRobot2> logList;
    private final static int range = 200;

    private ArrayList<Point> ciProcess;
    private ArrayList<Point> agentProcess;

    Agent(AgentRobot2 ag) {
        CI = new Point();
        CI.setLocation(ag.p);
        arobot = ag;
        logList = new ArrayList<>();
        logList.add(ag);

        ciProcess = new ArrayList<>();
        ciProcess.add(new Point(CI));
        agentProcess = new ArrayList<>();
        agentProcess.add(arobot.p);
    }

    /**
     * エージェント情報文字列からエージェントを精製。
     * この時セットされるロボットは仮のロボット（後から同じ情報をもつロボットをセットする）
     *
     * @param agentString 　エージェント情報文字列
     */
    Agent(MultiAgentSimulation2 s, String agentString) {
        Matcher agentMatcher = Pattern.compile("^AR(\\d+,\\d+/[-]?\\d+,[-]?\\d+/\\d+,\\d+/\\d+,\\d+/\\d+\\.\\d+)/(\\d+),(\\d+)$").matcher(agentString);

        if (agentMatcher.matches()) {
            arobot = new AgentRobot2(s, agentMatcher.group(1));
            CI = new Point(Integer.parseInt(agentMatcher.group(2)), Integer.parseInt(agentMatcher.group(3)));
        } else {
            System.out.println("エージェント情報が正しくありません" + agentString);
            arobot = null;
            CI = new Point();
        }

        logList = new ArrayList<>();
        ciProcess = new ArrayList<>();
        ciProcess.add(new Point(CI));
        agentProcess = new ArrayList<>();
        agentProcess.add(arobot.p);
    }

    void logReset() {
        logList.clear();
    }

    void setRobot(AgentRobot2 ag) {
        arobot = ag;
    }

    void setCI() {
        //CIの更新
        if (arobot.CI.distance(arobot.field.multiTarget[arobot.field.huntedTarget].p)
                < CI.distance(arobot.field.multiTarget[arobot.field.huntedTarget].p)) {
            CI.setLocation(arobot.PI);

            //ログをリセット
            logList.clear();
            logList.add(arobot);
        } else {
            arobot.CI.setLocation(CI);
        }
    }

    /**
     * AgentのCIからのfitness function
     * の値を取得
     * @return fitness function
     */
    double getCI() {
        return CI.distance(arobot.field.multiTarget[arobot.field.huntedTarget].p);
    }

    void agentMove(AgentRobot2 robots[]) {
        AgentRobot2 next = null;

        //次の行き先の決定
        for (int i = 0; i < SimulationPanel.num; i++) {
            if (arobot.p.distance(robots[i].p) < range && arobot != robots[i]) {
                if (next == null
                        || getAngle(robots[i], Math.toRadians(0)) < getAngle(
                        next, Math.toRadians(0))
                        && ContainNumInList(logList, robots[i]) < ContainNumInList(logList, arobot))
                    next = robots[i];
            }
        }

        //次のロボットが確定した場合
        if (next != null) {
            arobot = next;
            arobot.field.communication_num++;
        }

        //ログに現在のロボットを追加
        logList.add(arobot);
        arobot.omega = 0.9;

        if (arobot.v.distance(new Point(0, 0)) == 0) {
            arobot.v.setLocation((int) (Math.random() * Robot.maxv), (int) (Math.random() * Robot.maxv));
        }

        ciProcess.add(new Point(CI));
        agentProcess.add(arobot.p);
    }

    /**
     * リストに含まれる要素数を数えるメソッド
     *
     * @param l　リスト
     * @param searchO　検索対象
     * @return リストに含まれる検索対象の数
     */
    private int ContainNumInList(ArrayList<AgentRobot2> l, AgentRobot2 searchO) {
        int n = 0;
        for (AgentRobot2 o : l){
            if (searchO == o)
                n++;
        }
        return n;
    }
    private double getAngle(AgentRobot2 robot, double angle) {
        double dx = robot.p.x - arobot.p.x;
        double dy = robot.p.y - arobot.p.y;
        double angle2 = Math.atan2(dy, dx);
        double r = Math.abs((arobot.angle + angle - angle2)) % (Math.PI * 2);

        if (r > Math.PI)
            r = Math.PI - (r % Math.PI);
        return r;
    }

    void paint(Graphics g) {
        double scale = (double) (PsoSimulation.length) / (PsoSimulation.size);
        g.setColor(new Color(55, 55, 155));
        g.drawOval(arobot.getSwingPoint().x - 8,
                arobot.getSwingPoint().y - 8,
                16,
                16);
        g.drawOval((int) (arobot.getSwingPoint().x - range * scale),
                (int) (arobot.getSwingPoint().y - range * scale),
                (int) (range * scale * 2),
                (int) (range * scale * 2));
    }

    @Override
    public String toString() {
        return "A" + arobot + "/" + CI.x + "," + CI.y;
    }
}
