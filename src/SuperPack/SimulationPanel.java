package SuperPack;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;


abstract public class SimulationPanel extends JPanel{
	public static int startx = 20;
	public static int starty = 10;
	public final static int length = 250;
	public final static int size =1000;
	public static int num = 100;//ロボットの初期台数

	public static int targetNum=20;//ターゲットの数
	public int huntedTarget=0;//捕獲したターゲットの数
	public final static int maxCount=10000;//最大の捕獲時間

	public Robot[] robot;
	public enemy[] multiTarget;

	public int comunication_num=0; //通信回数
	public int count=0; //ステップ数

	public SimulationPanel(){
	}

	public void copy(SimulationPanel s){
		for(int i=0;i<num;i++){
			robot[i].copy(s.robot[i]);
		}

		for(int i=0;i<targetNum;i++){
			multiTarget[i].p.setLocation(s.multiTarget[i].p);
		}
	}

	public boolean setRobotNum(int n){
		if(n>num)
			return false;
		num=n;
		reset();
		return true;
	}

	public void paintComponent(Graphics g){
		g.setColor(new Color(255,255,255));
		g.fillRect(startx,starty,length,length);

		g.setColor(Color.BLACK);
		g.drawRect(startx,starty,length,length);

		for(int i=0;i<huntedTarget+1 && i < targetNum;i++){
			if(i==huntedTarget)
				multiTarget[i].paint(g,true);
			else
				multiTarget[i].paint(g,false);
		}

		for(int i=0;i<num;i++)
			robot[i].paint(g);
	}

	public Result[] setLog(){
		Result[] results = new Result[targetNum];

		while(true){
			if(step()){
				results[huntedTarget-1] = new Result(comunication_num, count, distance(),robotDensity(5));
				System.out.println("huntedTarget"+huntedTarget);

			}
			if(huntedTarget==targetNum){
				break;
			}
			if(count>maxCount){
				System.out.println("break");
				return results;
			}

		}
		reset();
		System.out.println(results);
		return results;
	}

	/**
	 * 捕獲失敗時のデータの出力
	 * @return
	 */

	public String getFailData() {
		boolean failFlag = false;
		String failData = null;
		while (!failFlag) {
			failData = this.toString();
			while (true) {

				if (step())
					failData = this.toString();

				if (huntedTarget == targetNum)
					break;

				if (count > maxCount){
					System.out.println(this.toString());
					System.out.println("debug"+failData);
					failFlag = true;
					break;
				}
			}
			reset();
		}
		return failData;
	}


	public double distance(){
		double d=0;
		for(int i=0;i<num;i++){
			d+=robot[i].distance;
		}
		return d;
	}

	/**
	 * ロボットの密度を求めるメソッド。
	 * 値が高いほど分散している。
	 *
	 * 計算式：ロボットのいるグリッドの数/全体のグリッドの数
	 * @return
	 */
	public double robotDensity(int grid){
		int gridCount=0;
		int allGrid = grid*grid;

		for (int y = 0; y < size; y += size / grid) {
			for (int x = 0; x < size; x += size / grid) {
				for (int i=0;i < robot.length;i++) {
					if (x <= robot[i].p.x && robot[i].p.x < x + size / grid
							&& y <= robot[i].p.y && robot[i].p.y < y + size / grid) {
						gridCount++;
						break;
					}
				}
			}
		}

		return (double)gridCount/allGrid;
	}

	/**
	 * ターゲットを捕獲したかを返すメソッド
	 * @return true:捕獲完了　false:未捕獲
	 */
	public boolean getEnd(){
		for(int i=0;i<num;i++){
			if(robot[i].p.distance(multiTarget[huntedTarget].p)<=0.0 )
				return true;
		}
		return false;
	}

	abstract public void reset();
	abstract public boolean step();

	/**
	 * 読み込んだファイルから、フィールドを再現するメソッド
	 * @param fileString
	 */
	public abstract void readFile(String fileString);

	/**
	 * フィールドの情報を文字列で返すメソッド
	 * ex: 0|0|0|T10,10|R424,257/6,3/424,257/424,257/0.9
	 * @return 捕獲数|通信回数|ステップ数|ターゲットの情報|ロボットの情報|
	 */
	@Override
	public String toString(){
		String string="";
		string+=huntedTarget+"|"+comunication_num+"|"+count+"|";
		for(int i=0;i < targetNum;i++){
			string+=multiTarget[i];
		}
		string+="|";
		for(int i=0;i < robot.length;i++){
			string+=robot[i];
		}

		return string;
	}
}
