package AgentPanel;

import SuperPack.Robot;

import java.awt.*;
import java.util.HashMap;


public class AgentList {
    public Agent agent;
    public AgentList next;

    public AgentList() {
        agent = null;
        next = null;
    }

    protected AgentList(Agent a) {
        agent = a;
        next = null;
    }

    /**
     * Agentのついか
     *
     * @param agent 追加するAgent
     */
    public void addAgent(Agent agent) {
        getLast().next = new AgentList(agent);
    }

    /**
     * AgentListのすべてのAgentを移動&更新
     *
     * @param robots ロボットの群れ
     */
    void agentsMove(Robot[] robots) {
        if (agent != null) {
            //捕獲していたら
            if (agent.arobot.isCaptured)
                agent.captured();
            agent.agentMove((AgentRobot2[]) robots);
            agent.updateAI();
        }

        if (next != null)
            next.agentsMove(robots);
    }

    /**
     * robot上にある重複しているエ－ジェントを削除する再帰関数
     *
     * @param robot 　エージェントの配下にあるロボット
     * @return 削除後のエージェントのリスト
     */
    public AgentList deleteOverlapAgent(AgentRobot2 robot) {
        if (agent != null && next != null && agent.arobot == robot) {
            Agent ag = next.getAgent(robot);
            if (ag == null)
                return (this);

            //エージェンと結合処理
            if (agent.getAiFitnessValue() < ag.getAiFitnessValue()) {

                //削除情報の結合
                ag.removeHashMap.putAll(agent.removeHashMap);
                agent.removeHashMap = new HashMap<>(ag.removeHashMap);

                agent.logList.addAll(ag.logList);
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


    public AgentList delAgent(Agent a) {
        if (a == agent)
            return next;
        if (next == null)
            return null;
        next = next.delAgent(a);
        return (this);
    }

    public void clear() {
        if (next != null)
            next.clear();
        agent = null;
        next = null;
    }

    void paint(Graphics2D g2) {
        if (agent != null)
            agent.paint(g2);
        if (next != null)
            next.paint(g2);
    }

    protected AgentList getLast() {
        if (next == null)
            return this;
        return next.getLast();
    }


    void robotLogReset() {
        if (agent != null)
            agent.logReset();
        if (next != null) {
            next.robotLogReset();
        }
    }

    /**
     * 指定したrobot上にあるagentの取得
     *
     * @param robot 検索するrobot
     * @return robot上にあるagent
     */
    public Agent getAgent(AgentRobot2 robot) {
        if (agent != null && agent.arobot.equals(robot))
            return agent;
        if (next == null)
            return null;
        return next.getAgent(robot);
    }

    @Override
    public String toString() {
        String string = agent + ":";
        if (next != null)
            string += next.toString();
        return string;
    }
}
