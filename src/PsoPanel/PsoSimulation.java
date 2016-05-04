package PsoPanel;


import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SuperPack.Intelligence;
import SuperPack.Robot;
import SuperPack.SimulationPanel;
import SuperPack.Enemy;

/**
 * PSOのシミュレータクラス
 *
 * @author Watari
 *
 */
public class PsoSimulation extends SimulationPanel {

	private ArrayList<Intelligence> ciProcess;

	/**
	 * コンストラクタ
	 */
	public PsoSimulation() {
		robot = new PsoRobot[num];

		for (int i = 0; i < targetNum; i++)
			targetList.add(new Enemy());
		for (int i = 0; i < num; i++)
			robot[i] = new PsoRobot(this);
		for (int i = 0; i < num; i++)
			robot[i].setCI();
		ciProcess = new ArrayList<>();
	}

	@Override
	public void readFile(String fileString){
		Pattern allPattern = Pattern.compile("^\\d+\\|\\d+\\|\\d+\\|"
				+"(T\\d+,\\d+)+\\|"
				+"(R\\d+,\\d+/[-]?\\d+,[-]?\\d+/\\d+,\\d+/\\d+,\\d+/\\d+(\\.\\d+)?)+$");
		//ファイル全体の正規表現チェック
		if(allPattern.matcher(fileString).matches()){
			//捕獲数と通信回数とステップ数の抽出
			Pattern pattern=Pattern.compile("^(\\d+)\\|(\\d+)\\|(\\d+)\\|(.*)\\|(.*)$");
			Matcher matcher=pattern.matcher(fileString);
			if(matcher.matches()){
				System.out.println("捕獲数:"+matcher.group(1));
				System.out.println("通信回数:"+matcher.group(2));
				System.out.println("ステップ数:"+matcher.group(3));
				System.out.println("ターゲット表現文字列:"+matcher.group(4));
				System.out.println("ロボット表現文字列:"+matcher.group(5));

				//捕獲数・通信回数・ステップ数の読み込み
				huntedTarget=Integer.parseInt(matcher.group(1));
				communication_num = Integer.parseInt(matcher.group(2));
				count = Integer.parseInt(matcher.group(3));

				//ターゲットの抽出
				String[] targetStringArray = matcher.group(4).split("T", 0);//[0]は空文字列
				targetList.clear();
				targetNum = targetStringArray.length-1;

				for(int i=1;i<targetStringArray.length;i++){
					targetList.add(new Enemy(targetStringArray[i]));
				}

				//ロボットの抽出
				String[] robotStringArray = matcher.group(5).split("R", 0);//[R]は空文字列
				robot = new Robot[robotStringArray.length-1];

				for(int i=1;i<robotStringArray.length;i++){
					robot[i-1] = new PsoRobot(this,robotStringArray[i]);
				}
			}

		}else{
			System.out.println("ファイルが正しくありません。");
		}
	}

	/**
	 * 毎ステップの処理
	 * @return ターゲットを捕獲したらTrueを返す
     */
	public boolean step() {
		if (huntedTarget < targetNum) {
			count++;
			for (int i = 0; i < num; i++) {
				robot[i].move();
			}
			ciProcess.add(new Intelligence(robot[0].CI));

			//ターゲットの削除
			deleteCaptureTarget();

		}
		return false;
	}

	public void reset() {
		count = 0;
		communication_num = 0;
		huntedTarget = 0;
		targetList.clear();
		for (int i = 0; i < targetNum; i++)
			targetList.add(new Enemy());

		for (int i = 0; i < num; i++)
			robot[i].reset();
		for (int i = 0; i < num; i++)
			robot[i].setCI();
		ciProcess = new ArrayList<>();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
