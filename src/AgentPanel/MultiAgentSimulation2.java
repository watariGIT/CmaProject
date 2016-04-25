package AgentPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SuperPack.Result;
import SuperPack.Robot;
import SuperPack.SimulationPanel;
import SuperPack.enemy;



/**
 * CMAと速度減衰をするロボットを用いたシミュレータ
 * @author Watari
 *
 */
public class MultiAgentSimulation2 extends SimulationPanel{
	public AgentList multi; //エージェントのリスト
	int agentNum;			//エージェントのマック数

	/**
	 * コンストラクタ
	 * @param agentNum　エージェントの数
     */
	public MultiAgentSimulation2(int agentNum){
		robot=new AgentRobot2[num];
		multiTarget=new enemy[targetNum];

		for (int i = 0; i < targetNum; i++)
			multiTarget[i] = new enemy();
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

			//捕獲成功次の処理
			if (getEnd() && huntedTarget < targetNum) {
				huntedTarget++;
				//エージェントのログをリセット
				multi.robotLogReset();
				return true;
			}
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
		this.comunication_num=0;
		huntedTarget=0;

		for (int i = 0; i < targetNum; i++)
			multiTarget[i] = new enemy();
		multi.clear();
		for(int i=0;i<num;i++)
			robot[i].reset();
		for(int i=0;i<agentNum;i++)
			multi.addAgent(new Agent((AgentRobot2) robot[i]));
	}

	public void setAgentNum(int agentNum){
		this.agentNum=agentNum;
		reset();
	}

	public void reset(int agentNum){
		count=0;
		this.comunication_num=0;
		huntedTarget=0;

		for (int i = 0; i < targetNum; i++)
			multiTarget[i] = new enemy();
		multi.clear();
		for(int i=0;i<num;i++)
			robot[i].reset();
		for(int i=0;i<agentNum;i++)
			multi.addAgent(new Agent((AgentRobot2) robot[i]));
	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(255, 255, 255));
		g.fillRect(startx, starty, length, length);

		g.setColor(Color.BLACK);
		g.drawRect(startx, starty, length, length);

		for (int i = 0; i < huntedTarget + 1 && i < targetNum; i++) {
			if (i == huntedTarget)
				multiTarget[i].paint(g, true);
			else
				multiTarget[i].paint(g, false);
		}

		for (int i = 0; i < num; i++)
			robot[i].paint(g);
		multi.paint(g);
	}

	/**
	 * 読み込んだファイルから、フィールドを再現するメソッド
	 *
	 * @param fileString
	 */
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
				comunication_num = Integer.parseInt(matcher.group(2));
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
				multiTarget = new enemy[targetStringArray.length - 1];
				targetNum = targetStringArray.length - 1;

				for (int i = 1; i < targetStringArray.length; i++) {
					multiTarget[i - 1] = new enemy(targetStringArray[i]);
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
		string+=huntedTarget+"|"+comunication_num+"|"+count+"|"+multi+"|";
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
