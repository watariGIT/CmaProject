package OriginArpsoPanel;

import SuperPack.Panel.Enemy;
import SuperPack.Panel.Intelligence;
import SuperPack.Panel.SimulationPanel;

/**
 * Created by watariMac on 2017/03/31.
 */
public class ArPsoSimulation extends SimulationPanel{

    public ArPsoSimulation() {
        robot = new ArPsoRobot[robotsNum];

        for (int i = 0; i < targetNum; i++)
            targetList.add(new Enemy());
        for (int i = 0; i < robotsNum; i++)
            robot[i] = new ArPsoRobot(this);
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
}
