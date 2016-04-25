package PsoPanel;
import java.awt.Point;

import SuperPack.Robot;



class PsoRobot extends Robot{


	public PsoRobot(PsoSimulation s) {
		super(s);
	}

	public PsoRobot(PsoSimulation s, String robotString) {
		super(s, robotString);
	}

	public void setCI() {
		if (CI == null)
			CI = new Point(p);
		if (p.distance(field.multiTarget[field.huntedTarget].p) < CI.distance(field.multiTarget[field.huntedTarget].p)) {
			CI.setLocation(p);
			for (int i = 0; i < PsoSimulation.num; i++) {
				if (this != field.robot[i])
					comunication(field.robot[i]);
			}
		}
	}

	private void comunication(Robot r) {
		r.CI = CI;
		field.comunication_num++;
	}

	protected Point getSwingPoint() {
		return (new Point(p.x * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startx,
				(PsoSimulation.size - p.y) * PsoSimulation.length / PsoSimulation.size + PsoSimulation.starty));
	}

}
