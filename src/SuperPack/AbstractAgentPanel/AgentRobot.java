package SuperPack.AbstractAgentPanel;import PsoPanel.PsoSimulation;import SuperPack.Panel.Intelligence;import SuperPack.Panel.Point2;import SuperPack.Panel.Robot;import SuperPack.Panel.SimulationPanel;import java.awt.*;import java.util.HashMap;import java.util.Map;/** * Created by Ishiwatari on 2016/10/25. */public abstract class AgentRobot extends Robot {    private Color col = new Color(55, 55, 155);    public HashMap<Integer, Intelligence> intelligenceHashMap; //keyロボットのID Value:そこから得た座標    public HashMap<Integer, Intelligence> removeHashMap; //削除する情報    protected AgentRobot(SimulationPanel as) {        super(as);        CI = null;        intelligenceHashMap = new HashMap<>();        intelligenceHashMap.put(id, new Intelligence(PI));        removeHashMap = new HashMap<>();    }    protected AgentRobot(Point2 point, SimulationPanel s) {        super(point, s);        intelligenceHashMap = new HashMap<>();        intelligenceHashMap.put(id, PI);        removeHashMap = new HashMap<>();    }    protected AgentRobot(SimulationPanel s, String robotString) {        //TODO 後で修正        super(s, robotString);        intelligenceHashMap.put(id, PI);        removeHashMap = new HashMap<>();    }    abstract protected Point2 calVelocity();    /**     * デバック用     */    void printIntelligence() {        if (id != 0) {            System.out.println("------------------Robot" + id + "--------------------------------");            for (Map.Entry<Integer, Intelligence> entry : intelligenceHashMap.entrySet()) {                System.out.println("id: " + entry.getKey() + " value" + entry.getValue() + " fitness" + entry.getValue().getFitnessValue());            }            System.out.println("PI: " + PI.x + " , " + PI.y + "/" + PI.getFitnessValue());            System.out.println("V: " + v.x + " , " + v.y);            System.out.println("-----------------------------------------------------");        }    }    protected Intelligence getGlobalBest() {        Intelligence ci = null;        for (Map.Entry<Integer, Intelligence> entry : intelligenceHashMap.entrySet()) {            Intelligence i = entry.getValue();            if (ci == null || (i.getFitnessValue() < ci.getFitnessValue()))                ci = new Intelligence(i);        }        return ci;    }    protected Intelligence getPersonalBest() {        Intelligence pi = intelligenceHashMap.get(id);        return pi;    }    protected void captured() {        //FIXME デバック用        //printIntelligence();        removeHashMap.putAll(intelligenceHashMap);        intelligenceHashMap.clear();        PI = new Intelligence(p.x, p.y, field.targetList);        intelligenceHashMap.put(id, new Intelligence(PI));    }    /**     * inteligenceHashMapの更新     */    public void setCI() {        Intelligence pi = new Intelligence(p.x, p.y, field.targetList);        if (intelligenceHashMap.get(id) == null || pi.getFitnessValue() < intelligenceHashMap.get(id).getFitnessValue())            intelligenceHashMap.put(id, pi);    }    public void copy(Robot robot) {        super.copy(robot);        CI = null;        intelligenceHashMap.put(id, new Intelligence(PI));    }    @Override    public void reset(double x, double y) {        super.reset(x, y);        col = new Color(55, 55, 155);        CI = null;        intelligenceHashMap.clear();        intelligenceHashMap.put(id, new Intelligence(PI));    }    public void paint(Graphics2D g2) {        double scale;        scale = (double) (PsoSimulation.length) / (PsoSimulation.size);        //FIXME デバック用        if (!isCaptured)            col = new Color(55, 55, 155);        else            col = new Color(155, 155, 155);        g2.setColor(col);        g2.fillOval(getSwingPoint().x - 5, getSwingPoint().y - 5, 10, 10);        g2.setColor(new Color(255, 255, 255));        g2.drawLine(getSwingPoint().x, getSwingPoint().y,                getSwingPoint().x + (int) ((20) * Math.cos(angle) * scale),                getSwingPoint().y - (int) ((20) * Math.sin(angle) * scale)        );    }    /**     * GUI表示用の座標を得るメソッド     *     * @return GUI上でのロボットの座標     */    protected Point getSwingPoint() {        return (new Point((int) (p.x * PsoSimulation.length / PsoSimulation.size                + SimulationPanel.startX), (int) ((PsoSimulation.size - p.y)                * PsoSimulation.length / PsoSimulation.size                + SimulationPanel.startY)));    }}