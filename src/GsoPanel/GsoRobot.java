package GsoPanel;

import SuperPack.Panel.Point2;
import SuperPack.Panel.Robot;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;

/**
 * Created by watariMac on 2016/11/10.
 */
public class GsoRobot extends Robot {
    protected GsoRobot(SimulationPanel s) {
        super(s);
    }

    protected GsoRobot(Point2 point, SimulationPanel s) {
        super(point, s);
    }

    protected GsoRobot(SimulationPanel s, String robotString) {
        super(s, robotString);
    }

    @Override
    protected Point getSwingPoint() {
        return null;
    }

    @Override
    public void setCI() {

    }
}
