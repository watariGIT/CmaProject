package AgentPanel;

import PsoPanel.PsoSimulation;
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

    protected MultiAgentSimulation2(){
        super();
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
    public void readFile(String fileString) {
        //TODDO
    }

    /**
     * デバック用の描写
     *
     * @param g2
     */
    protected void debugPaint(Graphics2D g2) {
        for (int i = 0; i < robotsNum; i++) {
            if (robot[i].id == debugMode) {
                robot[i].paint(g2);
                int ciSwingX = (int) (robot[i].CI.x * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startX);
                int ciSwingY = (int) ((PsoSimulation.size - robot[i].CI.y) * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startY);
                g2.setColor(Color.GREEN);
                g2.fillOval(ciSwingX - 3, ciSwingY - 3, 6, 6);

                int piSwingX = (int) (robot[i].PI.x * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startX);
                int piSwingY = (int) ((PsoSimulation.size - robot[i].PI.y) * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startY);
                g2.setColor(Color.YELLOW);
                g2.fillOval(piSwingX - 3, piSwingY - 3, 6, 6);
                break;
            }
        }
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
