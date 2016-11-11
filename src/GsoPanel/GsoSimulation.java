package GsoPanel;

import SuperPack.Panel.Enemy;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;

/**
 * Created by watariMac on 2016/11/10.
 */
public class GsoSimulation extends SimulationPanel {


    /**
     * コンストラクタ
     */
    public GsoSimulation() {
        robot = new GsoRobot[robotsNum];

        for (int i = 0; i < targetNum; i++)
            targetList.add(new Enemy());
        for (int i = 0; i < robotsNum; i++)
            robot[i] = new GsoRobot(this);
    }

    @Override
    public void readRobotFile(String str) {
        //todo
    }

    @Override
    public void reset() {
        count = 0;
        communication_num = 0;
        targetList.clear();
        for (int i = 0; i < targetNum; i++)
            targetList.add(new Enemy());

        for (int i = 0; i < robotsNum; i++)
            robot[i].reset();
    }

    @Override
    public void step() {
        count++;
        for (int i = 0; i < robotsNum; i++) {
            robot[i].move();
        }
        //ターゲットの削除
        deleteCaptureTarget();
    }

    @Override
    public void readFile(String fileString) {
        //todo
    }

    @Override
    public void paintComponent(Graphics g) {
        //FIXME 継承しやすいように修正(ターゲット・ロボットの描写とその他をわける)
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 255, 255));
        g2.fillRect(startX, startY, length, length);

        g2.setColor(Color.BLACK);
        g2.drawRect(startX, startY, length, length);
        g2.drawString("STEP:" + this.count, startX + 5, startY + length + 20);
        g2.drawString("CLASS:" + this.getClass().getName(), startX + 75, startY + length + 20);

        //ターゲットの描写
        if (debugMode == -1) {
            synchronized (targetList) {
                for (Enemy target : targetList) {
                    target.paint(g2, true);
                }
            }
            for (int i = 0; i < robotsNum; i++)
                robot[i].paint(g2);
        } else {
            GsoRobot gr = null;
            for (int i = 0; i < robotsNum; i++) {
                if (robot[i].id == debugMode) {
                    gr = (GsoRobot) robot[i];
                    break;
                }
            }

            synchronized (targetList) {
                for (Enemy target : targetList) {
                    target.paint(g2, true);
                }
            }

            for (int i = 0; i < robotsNum; i++) {
                if (robot[i].id == gr.id)
                    robot[i].paint(g2, new Color(55, 155, 155));
                else {
                    if (robot[i].p.distance(gr.p) <= gr.variableRange) {
                        if (((GsoRobot) robot[i]).luciferinLevel < gr.luciferinLevel)
                            robot[i].paint(g2, new Color(155, 55, 155));
                        else
                            robot[i].paint(g2);
                    }
                }
            }

            //rdの描写
            g2.setColor(new Color(55, 155, 155));
            int rdSwing = (int) Math.round(gr.variableRange * length / size);
            if (rdSwing != 0) {
                g2.drawOval(gr.getSwingPoint().x - rdSwing, gr.getSwingPoint().y - rdSwing, rdSwing * 2, rdSwing * 2);
            }
        }
    }
}
