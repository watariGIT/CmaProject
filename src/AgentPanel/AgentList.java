package AgentPanel;

import java.awt.Graphics;

import SuperPack.Robot;


class AgentList {
	private Agent agent;
	private AgentList next;
	
	AgentList(){
		agent=null;
		next=null;
	}
	
	private AgentList(Agent a){
		agent=a;
		next=null;
	}
	
	void addAgent(Agent a){
		getLast().next=new AgentList(a);
	}

	void agentMove(Robot[] robot){
		if(agent!=null){
			agent.agentMove((AgentRobot2[]) robot);
			agent.setCI();
		}
		if(next!=null)
			next.agentMove(robot);
	}

	/**
	 * robot上にある重複しているエ－ジェントを削除する再帰関数
	 *
	 * @param robot　エージェントの配下にあるロボット
	 * @return 削除後のエージェントのリスト
     */
	AgentList deleteOverlapAgent(AgentRobot2 robot) {
		if (agent != null && next != null && agent.arobot == robot) {
			Agent ag = next.getAgent(robot);
			if (ag == null)
				return (this);

			if (agent.getCI() < ag.getCI()) {
				next = next.delAgent(ag);
				return (this);
			} else
				return (next.deleteOverlapAgent(robot));
		}

		if (next == null)
			return this;
		next = next.deleteOverlapAgent(robot);
		return (this);
	}


	private AgentList delAgent(Agent a){
		if(a==agent)
			return next;
		if(next==null)
			return null;
		next=next.delAgent(a);
		return (this);
	}
	void clear(){
		if(next!=null)
			next.clear();
		agent=null;
		next=null;
	}
	void paint(Graphics g){
		if(agent!=null)
		agent.paint(g);
		if(next!=null)
			next.paint(g);
		}
	private AgentList getLast(){
		if(next==null)
			return this;
		return next.getLast();
	}
	
	private int getNum(){
		if(next==null)
			return 1;
		return next.getNum()+1;
	}
	
	void robotLogReset(){
		if(agent!=null)
		agent.logReset();
		if(next!=null){
			next.robotLogReset();
		}
	}
	
	Agent getAgent(AgentRobot2 robot){
		if(agent!=null&&agent.arobot.equals(robot))
			return agent;
		if(next==null)
			return null;
		return next.getAgent(robot);
	}
	
	@Override
	public String toString(){
		String string=agent+":";
		if(next!=null)
			string+=next.toString();
		return string;
	}
}
