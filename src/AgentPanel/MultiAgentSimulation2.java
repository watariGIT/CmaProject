package AgentPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SuperPack.Robot;
import SuperPack.SimulationPanel;
import SuperPack.Enemy;



/**
 * CMAと速度減衰をするロボットを用いたシミュレータ
 * @author Watari
 *
 */
public class MultiAgentSimulation2 extends SimulationPanel{
	private AgentList multi; //エージェントのリスト
	private int agentNum;			//エージェントのマック数

	/**
	 * コンストラクタ
	 * @param agentNum　エージェントの数
     */
	public MultiAgentSimulation2(int agentNum){
		robot=new AgentRobot2[num];

		for(int i=0;i<targetNum;i++) {
			targetList.add(new Enemy());
		}
		for(int i=0;i<num;i++)
			robot[i]=new AgentRobot2(this);
		multi=new AgentList();

		this.agentNum = agentNum;
		for(int i=0;i<agentNum;i++){
			multi.addAgent(new Agent((AgentRobot2) robot[i]));
		}
	}

	public boolean step() {
		if (huntedTarget < targetNum) {
			count++;

			//ロボットを動かす
			for (int i = 0; i < num; i++) {
				robot[i].move();
			}

			//エージェントを動かす
			multi.agentMove(robot);

			//重複しているロボットの削除
			for (int i = 0; i < num; i++) {
				multi.deleteOverlapAgent((AgentRobot2) (robot[i]));
			}

			//ターゲットの削除
			deleteCaptureTarget();
		}
		return false;
	}

	public void copy(SimulationPanel s){
		super.copy(s);

		//エージェントの初期化
		multi.clear();
		for(int i=0;i<agentNum;i++)
			multi.addAgent(new Agent((AgentRobot2) robot[i]));
	}

	public void reset(){
		count=0;
		this.communication_num =0;
		huntedTarget=0;
		targetList.clear();

		for (int i = 0; i < targetNum; i++)
			targetList.add(new Enemy());
		multi.clear();
		for(int i=0;i<num;i++)
			robot[i].reset();
		for(int i=0;i<agentNum;i++)
			multi.addAgent(new Agent((AgentRobot2) robot[i]));
	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(255, 255, 255));
		g.fillRect(startX, startY, length, length);

		g.setColor(Color.BLACK);
		g.drawRect(startX, startY, length, length);

		for(Enemy target:targetList){
			target.paint(g,true);
		}
		for (int i = 0; i < num; i++)
			robot[i].paint(g);
		multi.paint(g);
	}

	@Override
	public void readFile(String fileString) {
		Pattern allPattern = Pattern
				.compile("^\\d+\\|\\d+\\|\\d+\\|"
						+"(null:|A(R\\d+,\\d+/[-]?\\d+,[-]?\\d+/\\d+,\\d+/\\d+,\\d+/\\d+(\\.\\d+))/\\d+,\\d+:)+\\|"
						+ "(T\\d+,\\d+)+\\|"
						+ "(R\\d+,\\d+/[-]?\\d+,[-]?\\d+/\\d+,\\d+/\\d+,\\d+/\\d+(\\.\\d+)?)+$");
		// ファイル全体の正規表現チェック
		if (allPattern.matcher(fileString).matches()) {
			// 捕獲数と通信回数とステップ数の抽出
			Pattern pattern = Pattern
					.compile("^(\\d+)\\|(\\d+)\\|(\\d+)\\|(.*)\\|(.*)\\|(.*)$");
			Matcher matcher = pattern.matcher(fileString);
			if (matcher.matches()) {
				System.out.println("捕獲数:" + matcher.group(1));
				System.out.println("通信回数:" + matcher.group(2));
				System.out.println("ステップ数:" + matcher.group(3));
				System.out.println("エージェント表現文字列:" + matcher.group(4));
				System.out.println("ターゲット表現文字列:" + matcher.group(5));
				System.out.println("ロボット表現文字列:" + matcher.group(6));

				// 捕獲数・通信回数・ステップ数の読み込み
				huntedTarget = Integer.parseInt(matcher.group(1));
				communication_num = Integer.parseInt(matcher.group(2));
				count = Integer.parseInt(matcher.group(3));
				//エージェントの抽出
				String[] agentStringArray = matcher.group(4).split(":", 0);// [0]は空文字列
				multi = new AgentList();
				for (int i = 1; i < agentStringArray.length; i++) {
					if(!agentStringArray[i].equals("null")){
						multi.addAgent(new Agent(this,agentStringArray[i]));
					}
				}

				// ターゲットの抽出
				String[] targetStringArray = matcher.group(5).split("T", 0);// [0]は空文字列
				targetList.clear();
				targetNum = targetStringArray.length - 1;

				for (int i = 1; i < targetStringArray.length; i++) {
					targetList.add(new Enemy(targetStringArray[i]));
				}

				// ロボットの抽出
				String[] robotStringArray = matcher.group(6).split("R", 0);// [0]は空文字列
				robot = new AgentRobot2[robotStringArray.length - 1];

				for (int i = 1; i < robotStringArray.length; i++) {
					robot[i - 1] = new AgentRobot2(this, robotStringArray[i]);
					Agent agent = multi.getAgent((AgentRobot2) robot[i - 1]);
					if (agent != null) {
						agent.setRobot((AgentRobot2) robot[i - 1]);
					}
				}
			}
		} else {
			System.out.println("ファイルが正しくありません。");
		}
	}

	/**
	 * フィールドの情報を文字列で返すメソッド
	 * @return 捕獲数|通信回数|ステップ数|エージェントの情報|ターゲットの情報|ロボットの情報|
	 */
	@Override
	public String toString(){
		String string="";
		string+=huntedTarget+"|"+ communication_num +"|"+count+"|"+multi+"|";

		for(Enemy target:targetList){
			string+= target.toString();
		}
		string+="|";

		for(Robot r:robot){
			string+=r;
		}

		return string;
	}
}
