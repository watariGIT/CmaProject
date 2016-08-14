package FipsAgentPanel;import AgentPanel.Agent;import AgentPanel.AgentRobot2;import AgentPanel.MultiAgentSimulation2;import SuperPack.Intelligence;import java.awt.*;import java.util.HashMap;import java.util.Map;/** * Created by Ishiwatari on 2016/08/12. */public class FipsAgent extends Agent {    HashMap<Integer, Intelligence> intelligenceHashMap; //keyロボットのID Value:そこから得た座標    FipsAgent(AgentRobot2 ag) {        super(ag);        intelligenceHashMap = new HashMap<>();        intelligenceHashMap.put(ag.id, ag.PI);    }    FipsAgent(AgentRobot2 ag, Color col) {        super(ag, col);        intelligenceHashMap = new HashMap<>();        intelligenceHashMap.put(ag.id, ag.PI);    }    /**     * エージェント情報文字列からエージェントを精製。     * この時セットされるロボットは仮のロボット（後から同じ情報をもつロボットをセットする）     *     * @param s     * @param agentString 　エージェント情報文字列     */    FipsAgent(MultiAgentSimulation2 s, String agentString) {        //TODO あとで        super(s, agentString);    }    /**     * ターゲットを捕獲した際に行う処理     */    @Override    protected void captured() {        intelligenceHashMap.clear();        intelligenceHashMap.put(arobot.id, arobot.PI);    }    /**     * AgentのAI：ROBOTのCIを更新するメソッド     */    @Override    protected void updateAI() {        //ロボットの情報の更新        for (Map.Entry<Integer, Intelligence> agentEntry : intelligenceHashMap.entrySet()) {            int key = agentEntry.getKey();            Intelligence agentValue = agentEntry.getValue();            Intelligence robotValue = ((FipsRobot) arobot).intelligenceHashMap.get(key);            if (robotValue == null) {                ((FipsRobot) arobot).intelligenceHashMap.put(key, agentValue);            } else {                if (agentValue.getFitnessValue() < robotValue.getFitnessValue()) {                    ((FipsRobot) arobot).intelligenceHashMap.put(key, agentValue);                }            }        }        //エージェントの情報の更新        for (Map.Entry<Integer, Intelligence> robotEntry : ((FipsRobot) arobot).intelligenceHashMap.entrySet()) {            intelligenceHashMap.put(robotEntry.getKey(), robotEntry.getValue());        }        //fixme デバック用        double sX = 0, sY = 0;        for (Map.Entry<Integer, Intelligence> entry : intelligenceHashMap.entrySet()) {            sX += entry.getValue().x;            sY += entry.getValue().y;        }        AI.x = sX / intelligenceHashMap.size();        AI.y = sY / intelligenceHashMap.size();        //printIntelligence();    }    /**     * デバック用     */    private void printIntelligence() {        System.out.println("------------------Agent robot" + arobot.id + "--------------------------------");        for (Map.Entry<Integer, Intelligence> entry : intelligenceHashMap.entrySet()) {            System.out.println("id: " + entry.getKey() + " value" + entry.getValue() + " fitness" + entry.getValue().getFitnessValue());        }        System.out.println("-----------------------------------------------------");    }    @Override    public String toString() {        return "FIPS Agent";    }}