package PsoPanel;
import java.awt.Point;

import SuperPack.Intelligence;
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
			CI = new Intelligence(p.x,p.y,field.targetList);
		if (fitnessFunction(field.targetList) < CI.getFitnessValue()) {
			CI = new Intelligence(p.x,p.y,field.targetList);
			for (int i = 0; i < PsoSimulation.num; i++) {
				if (this != field.robot[i])
					communication(field.robot[i]);
			}
		}
	}

	private void communication(Robot r) {
		r.CI = CI; //TODO くさそう
		field.communication_num++;
	}

	protected Point getSwingPoint() {
		return (new Point(p.x * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startX,
				(PsoSimulation.size - p.y) * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startY));
	}

}
