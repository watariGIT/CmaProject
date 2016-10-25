package AgentPanel;

import PsoPanel.PsoSimulation;
import SuperPack.Intelligence;
import SuperPack.Point2;
import SuperPack.Robot;
import SuperPack.SimulationPanel;

import java.awt.*;
import java.util.HashMap;

public class AgentRobot2 extends Robot {
    private Color col = new Color(55, 55, 155);
    public HashMap<Integer, Intelligence> removeHashMap; //削除する情報

    public AgentRobot2(SimulationPanel as) {
        super(as);
        removeHashMap = new HashMap<>();
        CI = new Intelligence(PI.x, PI.y, as.targetList);
    }

    public AgentRobot2(SimulationPanel s, String robotString) {
        super(s, robotString);
        removeHashMap = new HashMap<>();
    }

    public AgentRobot2(Point2 point, SimulationPanel s) {
        super(point, s);
        removeHashMap = new HashMap<>();
        CI = new Intelligence(PI.x, PI.y, s.targetList);
    }

    public void move() {
        double oldPi = PI.getFitnessValue();
        super.move();
        double Pi = PI.getFitnessValue();

        //速度0の時にランダムな速度を与える
        if ((int) v.x == 0 && (int) v.y == 0) {
            v.setLocation(Math.random() * maxv, Math.random() * maxv);
        }

        //慣性定数の修正
        if (Pi > oldPi - 3) {
            //omega = 0.3;
        } else {
            omega = 0.9;
        }
        if (omega < 0.0) omega = 0.0;

    }

    public void reset() {
        super.reset();
        CI = new Intelligence(PI);
        col = new Color(55, 55, 155);
        removeHashMap.clear();
    }

    public void paint(Graphics2D g2) {
        double scale;
        scale = (double) (PsoSimulation.length) / (PsoSimulation.size);

        //FIXME デバック用
        if (!isCaptured)
            col = new Color(55, 55, 155);
        else
            col = new Color(155, 155, 155);

        g2.setColor(col);
        g2.fillOval(getSwingPoint().x - 5, getSwingPoint().y - 5, 10, 10);
        g2.setColor(new Color(255, 255, 255));
        g2.drawLine(getSwingPoint().x, getSwingPoint().y,
                getSwingPoint().x + (int) ((20) * Math.cos(angle) * scale),
                getSwingPoint().y - (int) ((20) * Math.sin(angle) * scale)
        );
    }

    /**
     * GUI表示用の座標を得るメソッド
     *
     * @return GUI上でのロボットの座標
     */
    protected Point getSwingPoint() {
        return (new Point((int) (p.x * PsoSimulation.length / PsoSimulation.size
                + SimulationPanel.startX), (int) ((PsoSimulation.size - p.y)
                * PsoSimulation.length / PsoSimulation.size
                + SimulationPanel.startY)));
    }

    /**
     * CIの更新
     */
    public void setCI() {
        if (PI.getFitnessValue() < CI.getFitnessValue()) {
            CI = new Intelligence(PI);
        }
    }

    @Override
    protected void captured() {
        removeHashMap.put(CI.hashCode(), CI);
        removeHashMap.put(PI.hashCode(), PI);
        PI = new Intelligence(p.x, p.y, field.targetList);
        CI = new Intelligence(p.x, p.y, field.targetList);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((col == null) ? 0 : col.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof AgentRobot2)) {
            return false;
        }
        AgentRobot2 other = (AgentRobot2) obj;
        if (col == null) {
            if (other.col != null) {
                return false;
            }
        } else if (!col.equals(other.col)) {
            return false;
        }
        return true;
    }

}
