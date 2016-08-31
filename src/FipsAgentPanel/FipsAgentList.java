package FipsAgentPanel;import AgentPanel.Agent;import AgentPanel.AgentList;import AgentPanel.AgentRobot2;import SuperPack.Intelligence;import java.util.HashMap;import java.util.Map;/** * Created by Ishiwatari on 2016/08/12. */public class FipsAgentList extends AgentList {    public FipsAgentList() {        agent = null;        next = null;    }    protected FipsAgentList(Agent a) {        agent = a;        next = null;    }    /**     * Agentのついか     *     * @param agent 追加するAgent     */    @Override    public void addAgent(Agent agent) {        getLast().next = new FipsAgentList(agent);    }    /**     * robot上にある重複しているエ－ジェントを削除する再帰関数     *     * @param robot 　エージェントの配下にあるロボット     * @return 削除後のエージェントのリスト     */    @Override    public AgentList deleteOverlapAgent(AgentRobot2 robot) {        if (agent != null && next != null && agent.arobot == robot) {            Agent ag = next.getAgent(robot); //同一ロボットにいるエージェントの取得            if (ag == null)                return (this);            agent.logList.addAll(ag.logList);            //削除情報の結合            ag.removeHashMap.putAll(agent.removeHashMap);            agent.removeHashMap = new HashMap<>(ag.removeHashMap);            for (Map.Entry<Integer, Intelligence> removeEntry : ((FipsAgent) ag).removeHashMap.entrySet()) {                int key = removeEntry.getKey();                Intelligence val = removeEntry.getValue();                Intelligence agentValue = ((FipsAgent) agent).intelligenceHashMap.get(key);                Intelligence agValue = ((FipsAgent) ag).intelligenceHashMap.get(key);                if (agentValue != null && agentValue.equals(val)) {                    ((FipsAgent) agent).intelligenceHashMap.remove(key);                }                if (agValue != null && agValue.equals(val)) {                    (((FipsAgent) ag)).intelligenceHashMap.remove(key);                }            }            //ハッシュマップの結合            for (Map.Entry<Integer, Intelligence> agEntry : ((FipsAgent) ag).intelligenceHashMap.entrySet()) {                int key = agEntry.getKey();                Intelligence agValue = agEntry.getValue();                Intelligence agentValue = ((FipsAgent) agent).intelligenceHashMap.get(key);                if (agentValue == null) {                    ((FipsAgent) agent).intelligenceHashMap.put(key, new Intelligence(agValue));                } else {                    if (agValue.getFitnessValue() < agentValue.getFitnessValue()) {                        ((FipsAgent) agent).intelligenceHashMap.put(key, new Intelligence(agValue));                    }                }            }            next = next.delAgent(ag);            return (this);        }        if (next == null)            return this;        next = next.deleteOverlapAgent(robot);        return (this);    }}