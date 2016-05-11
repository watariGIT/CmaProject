package SuperPack;

import java.awt.*;
import java.util.Arrays;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Iterator;


abstract public class SimulationPanel extends JPanel{
	public static int startX = 20;
	public static int startY = 10;
	public final static int length = 250;
	public final static int size = 1000;
	public static int num = 10;            //ロボットの初期台数
	private final static int maxCount = 10000;//最大の捕獲時間

	//FIXME ここいる？
	protected static int targetNum = 10;        //ターゲットの数


	public Robot[] robot;
	public ArrayList<Enemy> targetList = new ArrayList<Enemy>();

	public int communication_num =0; 			//通信回数
	protected int count=0; 					//ステップ数

	protected SimulationPanel(){
	}

	/**
	 * ディープコピーを行うメソッド
	 * @param s コピー元
     */
	public void copy(SimulationPanel s) {
		for (int i = 0; i < num; i++) {
			robot[i].copy(s.robot[i]);
		}

		targetList.clear();
		for (int i = 0; i < s.targetList.size(); i++) {
			targetList.add(s.targetList.get(i).copy());
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
		Graphics2D g2=(Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(new Color(255,255,255));
		g2.fillRect(startX,startY,length,length);

		g2.setColor(Color.BLACK);
		g2.drawRect(startX,startY,length,length);

		//ターゲットの描写
		synchronized (targetList) {
			for (Enemy target : targetList) {
				target.paint(g2, true);
			}
		}

		for(int i=0;i<num;i++)
			robot[i].paint(g2);
	}

	//FIXME メソッド名にセンスが無い
	public Result[] setLog(){
		Result[] results = new Result[targetNum];

		while(true){
			if(step()){
				//FiXME 後で修正
				//results[huntedTarget-1] = new Result(communication_num, count, distance(),robotDensity(5));
				//System.out.println("huntedTarget"+huntedTarget);

			}
			if(targetList.size() == 0){
				break;
			}
			if(count>maxCount){
				System.out.println("break");
				return results;
			}

		}
		reset();
		System.out.println(Arrays.toString(results));
		return results;
	}

	/**
	 * 捕獲失敗時のデータの出力
	 * @return 失敗した時のフィールドの状況を表す文字列
	 */

	public String getFailData() {
		boolean failFlag = false;
		String failData = null;
		while (!failFlag) {
			failData = this.toString();
			while (true) {

				if (step())
					failData = this.toString();

				if (targetList.size() == 0)
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


	private double distance(){
		double d=0;
		for(Robot r:robot){
			d+=r.distance;
		}
		return d;
	}

	/**
	 * ロボットの密度を求めるメソッド。
	 * 値が高いほど分散している。
	 *
	 * 計算式：ロボットのいるグリッドの数/全体のグリッドの数
	 * @return 分散度
	 */
	private double robotDensity(int grid){
		int gridCount=0;
		int allGrid = grid*grid;

		for (int y = 0; y < size; y += size / grid) {
			for (int x = 0; x < size; x += size / grid) {
				for (Robot aRobot : robot) {
					if (x <= aRobot.p.x && aRobot.p.x < x + size / grid
							&& y <= aRobot.p.y && aRobot.p.y < y + size / grid) {
						gridCount++;
						break;
					}
				}
			}
		}

		return (double)gridCount/allGrid;
	}

	/**
	 * 捕獲したターゲットを削除するメソッド
	 */
	public void deleteCaptureTarget() {
		//ターゲットの削除
		synchronized (targetList) {
			Iterator itr = targetList.iterator();
			for (Robot r : robot) {
				while (itr.hasNext()) {
					Enemy t = (Enemy) itr.next();
					if (r.p.distance(t.p) <= 0.0) {
						itr.remove();
					}
				}
				itr = targetList.iterator();
			}
		}
	}

	abstract public void reset();
	abstract public boolean step();

	/**
	 * 読み込んだファイルから、フィールドを再現するメソッド
	 * @param fileString 読み込むファイル名
	 */
	public abstract void readFile(String fileString);

	/**
	 * フィールドの情報を文字列で返すメソッド
	 * ex: 0|0|0|T10,10|R424,257/6,3/424,257/424,257/0.9
	 * @return ターゲットの数|通信回数|ステップ数|ターゲットの情報|ロボットの情報|
	 */
	@Override
	public String toString(){
		String string="";
		string+=targetList.size()+"|"+ communication_num +"|"+count+"|";
		for(Enemy target:targetList){
			string+= target.toString();
		}
		string+="|";
		for (Robot aRobot : robot) {
			string += aRobot;
		}

		return string;
	}
}
