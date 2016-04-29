package AgentPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import PsoPanel.PsoSimulation;
import SuperPack.SimulationPanel;
import SuperPack.Robot;

class AgentRobot2 extends Robot{
	private Color col=new Color(55,55,155);

	AgentRobot2(SimulationPanel as) {
		super(as);
		CI=new Point(PI);
	}

	AgentRobot2(SimulationPanel s, String robotString){
		super(s,robotString);
	}

	public void move(){
		double oldPi=PI.distance(field.targetList[field.huntedTarget].p);
		super.move();
		double Pi=PI.distance(field.targetList[field.huntedTarget].p);

		//慣性定数の修正
		if(Pi > oldPi - 3){
			omega = 0.3;
		}else{
			omega = 0.9;
		}

		if(omega<0.0)omega=0.0;
	}

	public void reset(){
		super.reset();
		CI=new Point(PI);
		col=new Color(55,55,155);
	}

	public void paint(Graphics g){
		double scale;
		scale=(double)(PsoSimulation.length) / (PsoSimulation.size) ;

		if(omega==0.9)
			col=new Color(55,55,155);
		else
			col = new Color(155,155,155);

		g.setColor(col);
		g.fillOval(getSwingPoint().x-5,getSwingPoint().y-5,10,10);
		g.setColor(new Color(255,255,255));
		g.drawLine(getSwingPoint().x,getSwingPoint().y,
				getSwingPoint().x+(int)((20)*Math.cos(angle)*scale),
				getSwingPoint().y-(int)((20)*Math.sin(angle)*scale)
		);
	}

    /**
     * GUI表示用の座標を得るメソッド
     * @return GUI上でのロボットの座標
     */
	protected Point getSwingPoint() {
		return (new Point(p.x * PsoSimulation.length / PsoSimulation.size
				+ SimulationPanel.startX, (PsoSimulation.size - p.y)
				* PsoSimulation.length / PsoSimulation.size
				+ SimulationPanel.startY));
	}

    public void setCI() {
        if (p.distance(field.targetList[field.huntedTarget].p) <
                CI.distance(field.targetList[field.huntedTarget].p)) {
            CI.setLocation(p);
        }
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
