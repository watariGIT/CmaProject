package SuperPack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PsoPanel.PsoSimulation;


public abstract class Robot {
	public final static int maxv=10;
	public double omega=0.9;
	private double c1=2.0;
	private double c2=2.0;
	public Point p;
	public Point v;
	public Intelligence PI;
	public Intelligence CI;
	public double angle;
	double distance;
	public SimulationPanel field;

	protected Robot(SimulationPanel s) {
		p = new Point((int) (Math.random() * PsoSimulation.size), (int) (Math.random() * PsoSimulation.size));
		PI = new Intelligence(p.x,p.y,s.targetList);
		v = new Point((int) (Math.random() * maxv), (int) (Math.random() * maxv));
		field = s;
		distance = 0;
		angle = 0;
	}
	
	protected Robot(SimulationPanel s,String robotString){
		Matcher robotMatcher=Pattern.compile("^(\\d+),(\\d+)/([-]?\\d+),([-]?\\d+)/(\\d+),(\\d+)/(\\d+),(\\d+)/(\\d+\\.\\d+)$").matcher(robotString);
		
		if(robotMatcher.matches()){
			p=new Point(Integer.parseInt(robotMatcher.group(1)),Integer.parseInt(robotMatcher.group(2)));
			
			v=new Point(Integer.parseInt(robotMatcher.group(3)),Integer.parseInt(robotMatcher.group(4)));
			
			PI=new Intelligence(Integer.parseInt(robotMatcher.group(5)),Integer.parseInt(robotMatcher.group(6)),s.targetList);
			CI=new Intelligence(Integer.parseInt(robotMatcher.group(7)),Integer.parseInt(robotMatcher.group(8)),s.targetList);

			omega=Double.parseDouble(robotMatcher.group(9));
			
			field=s;
			distance=0;
			angle=0;
		}else{
			//TODO 例外処理
			System.out.println("Error robotString"+robotString);
		}
	}
	
	
	public void move(){
		double dx,dy;
		Point oldP = new Point(p);
		dx=v.x*omega+c1*Math.random()*(PI.x-p.x)+c2*Math.random()*(CI.x-p.x);
		dy=v.y*omega+c1*Math.random()*(PI.y-p.y)+c2*Math.random()*(CI.y-p.y);
		if(dx>maxv)dx=maxv;
		if(dx<-1*maxv)dx=-1*maxv;
		if(dy>maxv)dy=maxv;
		if(dy<-1*maxv)dy=-1*maxv;
		v.setLocation(Math.round(dx), Math.round(dy));
		
		p.x=(int) Math.round(p.x+dx);
		p.y=(int) Math.round(p.y+dy);
		
		if(p.x<0)p.x=0;
		if(p.x>SimulationPanel.size)p.x=SimulationPanel.size;
		if(p.y<0)p.y=0;
		if(p.y>SimulationPanel.size)p.y=SimulationPanel.size;
		
		distance+=oldP.distance(p); //TODO 	謎の変数

		if(fitnessFunction(field.targetList)<PI.getFitnessValue())
			PI= new Intelligence(p.x,p.y,field.targetList);
		setCI();
		angle=Math.atan2(dy,dx);
	}
	
	public void copy(Robot robot){
		p.setLocation(robot.p);
		PI.copy(robot.PI);
		CI.copy(PI);
		v.setLocation(robot.v);
	}
	
	public void reset(){
		p.setLocation((int)(Math.random()*SimulationPanel.size),(int)(Math.random()*SimulationPanel.size));
		PI = new Intelligence(p.x,p.y,field.targetList);
		v.setLocation((int)(Math.random()*maxv),(int)(Math.random()*maxv));
		CI=null;
		omega=0.9;
		distance=0;
		angle=0;
	}
	
	public void paint(Graphics g){
		double scale;
		scale=(double)(PsoSimulation.length) / (PsoSimulation.size) ;
		g.setColor(new Color(55,55,155));
		g.fillOval(getSwingPoint().x-5,getSwingPoint().y-5,10,10);
		g.setColor(new Color(255,255,255));
		g.drawLine(getSwingPoint().x,getSwingPoint().y,
				getSwingPoint().x+(int)((20)*Math.cos(angle)*scale),
				getSwingPoint().y-(int)((20)*Math.sin(angle)*scale)
				);
		/*
		g.fillArc(getSwingPoint().x-6,getSwingPoint().y-6,15,15,
				(int)Math.toDegrees(angle-visualAngle/2), (int)Math.toDegrees(visualAngle));*/
	}
	
	abstract protected Point getSwingPoint();
	
	abstract public void setCI();
	
	@Override
	public String toString(){
		String string="R";
		string+=p.x+","+p.y+"/";
		string+=v.x+","+v.y+"/";
		string+=PI;
		string+=CI;
		string+=omega;
		return string;
	}

	/**
	 * 一番近いターゲットからの距離を得るメソッド
	 *
	 * @param tList ターゲットのリスト
	 * @return 一番近いターゲット
	 */
	protected double fitnessFunction(ArrayList<Enemy> tList) {
		double d = Double.MAX_VALUE;
		for (Enemy t : tList) {
			if (p.distance(t.p) < d)
				d = p.distance(t.p);
		}
		return d;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((CI == null) ? 0 : CI.hashCode());
		result = prime * result + ((PI == null) ? 0 : PI.hashCode());
		long temp;
		temp = Double.doubleToLongBits(angle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(c1);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(c2);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(distance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(omega);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
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
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Robot)) {
			return false;
		}
		Robot other = (Robot) obj;
		if (CI == null) {
			if (other.CI != null) {
				return false;
			}
		} else if (!CI.equals(other.CI)) {
			return false;
		}
		if (PI == null) {
			if (other.PI != null) {
				return false;
			}
		} else if (!PI.equals(other.PI)) {
			return false;
		}
		if (Double.doubleToLongBits(angle) != Double
				.doubleToLongBits(other.angle)) {
			return false;
		}
		if (Double.doubleToLongBits(c1) != Double.doubleToLongBits(other.c1)) {
			return false;
		}
		if (Double.doubleToLongBits(c2) != Double.doubleToLongBits(other.c2)) {
			return false;
		}
		if (Double.doubleToLongBits(distance) != Double
				.doubleToLongBits(other.distance)) {
			return false;
		}
		if (Double.doubleToLongBits(omega) != Double
				.doubleToLongBits(other.omega)) {
			return false;
		}
		if (p == null) {
			if (other.p != null) {
				return false;
			}
		} else if (!p.equals(other.p)) {
			return false;
		}
		if (v == null) {
			if (other.v != null) {
				return false;
			}
		} else if (!v.equals(other.v)) {
			return false;
		}
		return true;
	}
}
