package FipsAgentPanel;import AgentPanel.AgentRobot2;import SuperPack.Intelligence;import SuperPack.Point2;import SuperPack.Robot;import SuperPack.SimulationPanel;import java.util.HashMap;import java.util.Map;/** * Created by Ishiwatari on 2016/08/12. */public class FipsRobot extends AgentRobot2 {    protected HashMap<Integer, Intelligence> intelligenceHashMap; //keyロボットのID Value:そこから得た座標    FipsRobot(SimulationPanel as) {        super(as);        intelligenceHashMap.put(id, PI);    }    FipsRobot(SimulationPanel s, String robotString) {        //TODO 後で修正        super(s, robotString);        intelligenceHashMap.put(id, PI);    }    /**     * FIPSに従って、dx,dyを求める     *     * @return     */    @Override    protected Point2 calVelocity() {        double sumX = 0, sumY = 0;        for (Map.Entry<Integer, Intelligence> entry : intelligenceHashMap.entrySet()) {            sumX += Math.round(1) * (entry.getValue().x - p.x);            sumY += Math.round(1) * (entry.getValue().y - p.y);        }        //TODO サイの値がわからんぽよ        double dx = (v.x + sumX / intelligenceHashMap.size()) * 0.7298;        double dy = (v.y + sumY / intelligenceHashMap.size()) * 0.7298;        return new Point2(dx, dy);    }    @Override    protected void captured() {        intelligenceHashMap.clear();        PI = new Intelligence(p.x, p.y, field.targetList);        intelligenceHashMap.put(id, PI);    }    /**     * inteligenceHashMapの更新     */    @Override    public void setCI() {        if (PI.getFitnessValue() < intelligenceHashMap.get(id).getFitnessValue())            intelligenceHashMap.put(id, PI);    }    @Override    public void copy(Robot robot) {        super.copy(robot);        intelligenceHashMap.put(id, PI);    }    @Override    public void reset() {        super.reset();        intelligenceHashMap.clear();        intelligenceHashMap.put(id, PI);    }}