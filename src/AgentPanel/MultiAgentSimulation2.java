package AgentPanel;

import SuperPack.AbstractAgentPanel.Agent;
import SuperPack.AbstractAgentPanel.AgentList;
import SuperPack.AbstractAgentPanel.AgentSimulation;
import SuperPack.Panel.Enemy;
import SuperPack.Panel.Point2;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;


/**
 * CMAと速度減衰をするロボットを用いたシミュレータ
 *
 * @author Watari
 */
public class MultiAgentSimulation2 extends AgentSimulation {

    protected MultiAgentSimulation2() {
    }

    /**
     * コンストラクタ
     *
     * @param agentNum 　エージェントの数
     */
    public MultiAgentSimulation2(int agentNum) {
        robot = new AgentRobot2[robotsNum];

        for (int i = 0; i < targetNum; i++) {
            targetList.add(new Enemy());
        }
        for (int i = 0; i < robotsNum; i++)
            robot[i] = new AgentRobot2(this);
        multi = new AgentList();
        this.agentNum = agentNum;

        //エージェントの生成
        for (int i = 0; i < agentNum; i++) {
            multi.addAgent(new Agent((AgentRobot2) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));
        }
    }

    @Override
    public void step() {
        count++;
        //ロボットを動かす
        for (int i = 0; i < robotsNum; i++) {
            robot[i].move();
        }
        //エージェントを動かす
        multi.agentsMove(robot);
        //重複しているロボットの削除
        for (int i = 0; i < robotsNum; i++) {
            multi.deleteOverlapAgent((AgentRobot2) (robot[i]));
        }
        //ターゲットの削除
        deleteCaptureTarget();
    }

    public void copy(SimulationPanel s) {
        super.copy(s);

        //エージェントの初期化
        multi.clear();
        for (int i = 0; i < agentNum; i++)
            multi.addAgent(new Agent((AgentRobot2) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));
    }

    @Override
    public void reset() {
        count = 0;
        this.communication_num = 0;
        targetList.clear();

        for (int i = 0; i < targetNum; i++)
            targetList.add(new Enemy());
        multi.clear();
        for (int i = 0; i < robotsNum; i++)
            robot[i].reset();
        for (int i = 0; i < agentNum; i++)
            multi.addAgent(new Agent((AgentRobot2) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 255, 255));
        g2.fillRect(startX, startY, length, length);

        g2.setColor(Color.BLACK);
        g2.drawRect(startX, startY, length, length);
        g2.drawString("STEP:" + this.count, startX + 5, startY + length + 20);

        //ターゲットの描写
        synchronized (targetList) {
            for (Enemy target : targetList) {
                target.paint(g2, true);
            }
        }

        if (debugMode == -1) {
            for (int i = 0; i < robot.length; i++)
                robot[i].paint(g2);
            multi.paint(g2);
        } else {
            //デバック用の描写
            debugPaint(g2);
        }
    }

    @Override
    public void readEnemyFile(String str) {
        super.readEnemyFile(str);
        multi.clear();
        //エージェントの生成
        for (int i = 0; i < agentNum; i++) {
            multi.addAgent(new Agent((AgentRobot2) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));
        }
    }

    /**
     * ロボットをファイルから読み込み配置する
     *
     * @param str
     */
    @Override
    public void readRobotFile(String str) {
        String[] line = str.split(crlf);
        robotsNum = line.length;
        robot = new AgentRobot2[robotsNum];

        for (int i = 0; i < line.length; i++) {
            String[] loc = line[i].split(",");
            double x = Double.parseDouble(loc[0]);
            double y = Double.parseDouble(loc[1]);
            robot[i] = new AgentRobot2(new Point2(x, y), this);
        }

        multi.clear();
        if (robotsNum < agentNum)
            agentNum = robotsNum;
        //エージェントの生成
        for (int i = 0; i < agentNum; i++) {
            multi.addAgent(new Agent((AgentRobot2) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));
        }
    }

    @Override
    public void readFile(String fileString) {
        //TODDO
    }

    /**
     * フィールドの情報を文字列で返すメソッド
     *
     * @return ターゲットの数|通信回数|ステップ数|エージェントの情報|ターゲットの情報|ロボットの情報|
     */
    @Override
    public String toString() {
        //TODO
        return "HOGE";
    }
}
