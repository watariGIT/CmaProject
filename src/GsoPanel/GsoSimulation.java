package GsoPanel;

import SuperPack.Panel.Enemy;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by watariMac on 2016/11/10.
 */
public class GsoSimulation extends SimulationPanel{


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
}
