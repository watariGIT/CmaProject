package SuperPack;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Enemy {
	public Point2 p;

	public Enemy(){
		p = new Point2((Math.random() * SimulationPanel.size),
				(Math.random() * SimulationPanel.size));
	}

	public Enemy(Point2 point) {
		p=point;
	}

	public Enemy(Enemy e){
		p = new Point2(e.p);
	}

	public Enemy(String targetString){
		p = new Point2(0, 0);
		Matcher targetMatcher=Pattern.compile("^(\\d+),(\\d+)$").matcher(targetString);
		if(targetMatcher.matches()){
			p.setLocation(Integer.parseInt(targetMatcher.group(1)),Integer.parseInt(targetMatcher.group(2)));
		}else{
			p.setLocation((Math.random() * SimulationPanel.size),
					(Math.random() * SimulationPanel.size));
			System.out.println("ターゲット情報が正しくありません"+targetString);
		}
	}

	/**
	 * フィールドをgrainSizeで分割し、[x,y]の範囲でランダムにターゲットを配置する。
	 * @param x 分割したフィールドのx
	 * @param y 分割したフィールドのy
	 * @param grainSize 分割する数
	 */
	public Enemy(int x, int y, int grainSize){
		if(x < 0)
			x=0;
		if(x > grainSize)
			x=grainSize;

		if(y < 0)
			y=0;
		if(y > grainSize)
			y=grainSize;

		p = new Point2(0, 0);
		p.setLocation((SimulationPanel.size / grainSize * x + Math.random() * SimulationPanel.size / grainSize),
				(SimulationPanel.size / grainSize * (grainSize - y) - Math.random() * SimulationPanel.size / grainSize));
	}

	public void reset(){
		do {
			p.setLocation((int)(Math.random()*SimulationPanel.size),
					(int)(Math.random()*SimulationPanel.size));
		}while(p.distance(SimulationPanel.size/2,SimulationPanel.size/2)>100);
	}


	public void paint(Graphics2D g2, Boolean ovalFlag){
		g2.setColor(new Color(200,80,80));

		if(ovalFlag)
			g2.fillOval(getPoint().x,getPoint().y,8,8);
		else
			g2.fillRect(getPoint().x,getPoint().y,8,8);
	}

	private Point getPoint(){
		return (new Point((int) (p.x * SimulationPanel.length / SimulationPanel.size + SimulationPanel.startX),
				(int) (SimulationPanel.size - p.y) * SimulationPanel.length / SimulationPanel.size + SimulationPanel.startY));
	}

	@Override
	public String toString(){
		return "T"+p.x+","+p.y;
	}
}
