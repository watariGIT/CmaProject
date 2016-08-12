package AgentPanel;

import PsoPanel.PsoSimulation;
import SuperPack.Intelligence;
import SuperPack.Robot;
import SuperPack.SimulationPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//FIXME distance -> fitness function
public class Agent {
    private Intelligence AI;
    public AgentRobot2 arobot;
    public ArrayList<AgentRobot2> logList;
    private final static int range = 200;
    private Color color;

    private ArrayList<Intelligence> ciProcess;
    private ArrayList<Point> agentProcess;

    public Agent(AgentRobot2 ag) {
        AI = new Intelligence(ag.PI); //TODO 糞
        arobot = ag;
        logList = new ArrayList<>();
        logList.add(ag);
        ciProcess = new ArrayList<>();
        ciProcess.add(new Intelligence(AI));
        agentProcess = new ArrayList<>();
        agentProcess.add(arobot.p);
        color = new Color(55, 55, 155);
    }


    public Agent(AgentRobot2 ag, Color col) {
        AI = new Intelligence(ag.PI); //TODO 糞
        arobot = ag;
        logList = new ArrayList<>();
        logList.add(ag);
        ciProcess = new ArrayList<>();
        ciProcess.add(new Intelligence(AI));
        agentProcess = new ArrayList<>();
        agentProcess.add(arobot.p);
        color = col;
    }

    /**
     * エージェント情報文字列からエージェントを精製。
     * この時セットされるロボットは仮のロボット（後から同じ情報をもつロボットをセットする）
     *
     * @param agentString 　エージェント情報文字列
     */
    public Agent(MultiAgentSimulation2 s, String agentString) {
        Matcher agentMatcher = Pattern.compile("^AR(\\d+,\\d+/[-]?\\d+,[-]?\\d+/\\d+\\.\\d+,\\d+\\.\\d+/\\d+\\.\\d+,\\d+\\.\\d+/\\d+\\.\\d+)/(\\d+\\.\\d+),(\\d+\\.\\d+)$").matcher(agentString);

        if (agentMatcher.matches()) {
            arobot = new AgentRobot2(s, agentMatcher.group(1));
            AI = new Intelligence(Double.parseDouble(agentMatcher.group(2)), Double.parseDouble(agentMatcher.group(3)), s.targetList);
        } else {
            //TODO 例外処理
            System.out.println(getClass().getName() + "エージェント情報が正しくありません" + agentString);
            arobot = null;
            AI = null;
        }

        logList = new ArrayList<>();
        ciProcess = new ArrayList<>();
        ciProcess.add(new Intelligence(AI));
        agentProcess = new ArrayList<>();
        agentProcess.add(arobot.p);
        color = new Color((int) (105 + 100 * Math.random()), (int) (55 + 200 * Math.random()), (int) (105 + 100 * Math.random()));
    }

    void logReset() {
        logList.clear();
    }

    void setRobot(AgentRobot2 ag) {
        arobot = ag;
    }


    void agentMoveUpdate(AgentRobot2 robots[]) {
        AgentRobot2 next = null;

        if (arobot.isCaptured) {
            AI.copy(arobot.CI);
        }

        //次の行き先の決定
        // 前方優先
        for (int i = 0; i < SimulationPanel.robotsNum; i++) {
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

        ciProcess.add(new Intelligence(AI));
        agentProcess.add(arobot.p);
        updateAI();
    }

    /**
     * AgentのAI：ROBOTのCIを更新するメソッド
     */
    private void updateAI() {
        //CIの更新
        //TODO fitnessFunctionの修正
        if (arobot.CI.getFitnessValue()
                < AI.getFitnessValue()) {
            AI.copy(arobot.CI);

            //ログをリセット
            logList.clear();
            logList.add(arobot);
        } else {
            arobot.CI.copy(AI);
        }
    }

    void agentMoveDelete(AgentRobot2 robots[]) {
        AgentRobot2 next = null;

        //次の行き先の決定
        for (int i = 0; i < SimulationPanel.robotsNum; i++) {
            if (arobot.p.distance(robots[i].p) < range && arobot != robots[i]) {
                if (next == null
                        || getAngle(robots[i], Math.toRadians(0)) < getAngle(
                        next, Math.toRadians(0))
                        && ContainNumInList(logList, arobot) < ContainNumInList(logList, robots[i]))
                    next = robots[i];
            }
        }

        //次のロボットが確定した場合
        if (next != null) {
            arobot = next;
            arobot.field.communication_num++;
        }

        //ログに現在のロボットを追加
        arobot.omega = 0.9;

        if (arobot.v.distance(new Point(0, 0)) == 0) {
            arobot.v.setLocation((int) (Math.random() * Robot.maxv), (int) (Math.random() * Robot.maxv));
        }

        ciProcess.add(new Intelligence(AI));
        agentProcess.add(arobot.p);
        deleteAI();

        if (logList.isEmpty()) {
            AI.copy(arobot.CI);
        }
        System.out.println("" + AI.getFitnessValue() + "," + logList.size());
    }

    /**
     * ログにいるarobotのInteligenceを初期化し、ログからarobotを削除する
     */
    private void deleteAI() {
        if (logList.indexOf(arobot) != -1) {
            arobot.PI = new Intelligence(arobot.p.x, arobot.p.y);
            arobot.CI = new Intelligence(arobot.p.x, arobot.p.y);
        }
        while (logList.remove((AgentRobot2) arobot)) {
        }
    }

    /**
     * AgentのAIからのfitness function
     * の値を取得
     *
     * @return fitness function
     */
    //TODO メソッド名が糞
    double getAiFitnessValue() {
        return AI.getFitnessValue();
    }

    /**
     * リストに含まれる要素数を数えるメソッド
     *
     * @param l       　リスト
     * @param searchO 　検索対象
     * @return リストに含まれる検索対象の数
     */
    private int ContainNumInList(ArrayList<AgentRobot2> l, AgentRobot2 searchO) {
        int n = 0;
        for (AgentRobot2 o : l) {
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

    void paint(Graphics2D g2) {

        //エージェント表示
        double scale = (double) (PsoSimulation.length) / (PsoSimulation.size);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(arobot.getSwingPoint().x - 8,
                arobot.getSwingPoint().y - 8,
                16, 16);
        g2.setStroke(new BasicStroke(1));

        //エージェントの範囲表示
        /*g2.drawOval((int) (arobot.getSwingPoint().x - range * scale),
                (int) (arobot.getSwingPoint().y - range * scale),
                (int) (range * scale * 2),
                (int) (range * scale * 2));*/

        //AIの表示
        int aiSwingX = (int) (AI.x * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startX);
        int aiSwingY = (int) ((PsoSimulation.size - AI.y) * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startY);
        g2.fillOval(aiSwingX - 3, aiSwingY - 3, 6, 6);
    }

    @Override
    public String toString() {
        return "A" + arobot + "/" + AI.x + "," + AI.y;
    }
}
