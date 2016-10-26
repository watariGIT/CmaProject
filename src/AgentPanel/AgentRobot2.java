package AgentPanel;

import SuperPack.AbstractAgentPanel.AgentRobot;
import SuperPack.Panel.Intelligence;
import SuperPack.Panel.Point2;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;
import java.util.HashMap;

public class AgentRobot2 extends AgentRobot {
    private Color col = new Color(55, 55, 155);
    public HashMap<Integer, Intelligence> removeHashMap; //削除する情報

    public AgentRobot2(SimulationPanel as) {
        super(as);
        CI = new Intelligence(PI.x, PI.y, as.targetList);
    }

    public AgentRobot2(SimulationPanel s, String robotString) {
        super(s, robotString);
    }

    public AgentRobot2(Point2 point, SimulationPanel s) {
        super(point, s);
        CI = new Intelligence(PI.x, PI.y, s.targetList);
    }

    @Override
    protected Point2 calVelocity() {
        CI = getGlobalBest();
        PI = getPersonalBest();

        Point2 v = new Point2(this.v.x * omega + c1 * Math.random() * (PI.x - p.x) + c2 * Math.random() * (CI.x - p.x),
                this.v.y * omega + c1 * Math.random() * (PI.y - p.y) + c2 * Math.random() * (CI.y - p.y));
        //速度0の時にランダムな速度を与える
        if ((int) v.x == 0 && (int) v.y == 0) {
            v.setLocation(Math.random() * maxv, Math.random() * maxv);
        }
        return v;
    }
}
