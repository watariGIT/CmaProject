package PsoPanel;

import SuperPack.Panel.Intelligence;
import SuperPack.Panel.Point2;
import SuperPack.Panel.Robot;

import java.awt.*;



class PsoRobot extends Robot{


	public PsoRobot(PsoSimulation s) {
		super(s);
	}

	public PsoRobot(PsoSimulation s, String robotString) {
		super(s, robotString);
	}

	public PsoRobot(Point2 point, PsoSimulation s) {
		super(point, s);
	}

	public void setCI() {
		if (CI == null)
			CI = new Intelligence(p.x,p.y,field.targetList);
		if (fitnessFunction(field.targetList) < CI.getFitnessValue()) {
			CI = new Intelligence(p.x,p.y,field.targetList);
			for (int i = 0; i < field.robotsNum; i++) {
				if (this != field.robot[i])
					communication(field.robot[i]);
			}
		}
	}

	private void communication(Robot r) {
		r.CI = new Intelligence(CI);
		field.communication_num++;
	}

	protected Point getSwingPoint() {
		return (new Point((int) (p.x * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startX),
				(int) ((PsoSimulation.size - p.y) * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startY)));
	}

}
