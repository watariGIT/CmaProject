package FipsAgentPanel;import AgentPanel.AgentRobot2;import SuperPack.Intelligence;import SuperPack.Point2;import SuperPack.Robot;import SuperPack.SimulationPanel;import java.util.HashMap;import java.util.Map;/** * Created by Ishiwatari on 2016/08/12. */public class FipsRobot extends AgentRobot2 {    protected HashMap<Integer, Intelligence> intelligenceHashMap; //keyロボットのID Value:そこから得た座標    FipsRobot(SimulationPanel as) {        super(as);        CI = null;        intelligenceHashMap = new HashMap<>();        intelligenceHashMap.put(id, new Intelligence(PI));    }    FipsRobot(SimulationPanel s, String robotString) {        //TODO 後で修正        super(s, robotString);        intelligenceHashMap.put(id, PI);    }    /**     * FIPSに従って、dx,dyを求める     *     * @return     */    @Override    protected Point2 calVelocity() {        double sumX = 0.0, sumY = 0.0;        int n = 0;        for (Map.Entry<Integer, Intelligence> entry : intelligenceHashMap.entrySet()) {            if ( entry.getValue().getFitnessValue() <= PI.getFitnessValue()) {                double u = Math.random() * 2.0;                sumX += u * (entry.getValue().x - p.x);                sumY += u * (entry.getValue().y - p.y);                n++;            }        }        //TODO サイの値がわからんぽよ        double dx = (v.x + sumX / n) * 0.8;        double dy = (v.y + sumY / n) * 0.8;        //fixme デバック用        /*if (id == 30) {            System.out.println("sumx/2n " + sumX / (double) (n) + "  sumy/2n " + sumY / (double) (n));            System.out.println("dV: " + dx + " , " + dy);            printIntelligence();        }*/        return new Point2(dx, dy);    }    /**     * デバック用     */    private void printIntelligence() {        if (id != 0) {            System.out.println("------------------Robot" + id + "--------------------------------");            for (Map.Entry<Integer, Intelligence> entry : intelligenceHashMap.entrySet()) {                System.out.println("id: " + entry.getKey() + " value" + entry.getValue() + " fitness" + entry.getValue().getFitnessValue());            }            System.out.println("PI: " + PI.x + " , " + PI.y + "/" + PI.getFitnessValue());            System.out.println("V: " + v.x + " , " + v.y);            System.out.println("-----------------------------------------------------");        }    }    @Override    protected void captured() {        printIntelligence();        intelligenceHashMap.clear();        PI = new Intelligence(p.x, p.y, field.targetList);        intelligenceHashMap.put(id, new Intelligence(PI));    }    /**     * inteligenceHashMapの更新     */    @Override    public void setCI() {        if (PI.getFitnessValue() < intelligenceHashMap.get(id).getFitnessValue())            intelligenceHashMap.put(id, new Intelligence(PI));    }    @Override    public void copy(Robot robot) {        super.copy(robot);        CI = null;        intelligenceHashMap.put(id, new Intelligence(PI));    }    @Override    public void reset() {        super.reset();        CI = null;        intelligenceHashMap.clear();        intelligenceHashMap.put(id, new Intelligence(PI));    }}