package FipsAgentPanel;import PsoPanel.PsoSimulation;import SuperPack.AbstractAgentPanel.Agent;import SuperPack.AbstractAgentPanel.AgentList;import SuperPack.AbstractAgentPanel.AgentSimulation;import SuperPack.Panel.Enemy;import SuperPack.Panel.Intelligence;import SuperPack.Panel.Point2;import SuperPack.Panel.SimulationPanel;import java.awt.*;import java.util.HashMap;import java.util.Map;/** * Created by Ishiwatari on 2016/08/12. */public class FipsAgentSimulation extends AgentSimulation {    protected FipsAgentSimulation() {        // new FipsAgentSimulation(100);    }    /**     * コンストラクタ     *     * @param agentNum 　エージェントの数     */    public FipsAgentSimulation(int agentNum) {        robot = new FipsRobot[robotsNum];        for (int i = 0; i < targetNum; i++) {            targetList.add(new Enemy());        }        for (int i = 0; i < robotsNum; i++)            robot[i] = new FipsRobot(this);        multi = new AgentList();        this.agentNum = agentNum;        //エージェントの生成        for (int i = 0; i < agentNum; i++) {            multi.addAgent(new Agent((FipsRobot) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));        }    }    @Override    public void readEnemyFile(String str) {        super.readEnemyFile(str);        multi.clear();        //エージェントの生成        for (int i = 0; i < agentNum; i++)            multi.addAgent(new Agent((FipsRobot) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));    }    /**     * ロボットをファイルから読み込み配置する     *     * @param str     */    @Override    public void readRobotFile(String str) {        String[] line = str.split(crlf);        robotsNum = line.length;        robot = new FipsRobot[robotsNum];        for (int i = 0; i < line.length; i++) {            String[] loc = line[i].split(",");            double x = Double.parseDouble(loc[0]);            double y = Double.parseDouble(loc[1]);            robot[i] = new FipsRobot(new Point2(x, y), this);        }        multi.clear();        if (robotsNum < agentNum)            agentNum = robotsNum;        //エージェントの生成        for (int i = 0; i < agentNum; i++) {            multi.addAgent(new Agent((FipsRobot) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));        }    }    @Override    public void reset() {        count = 0;        this.communication_num = 0;        targetList.clear();        for (int i = 0; i < targetNum; i++)            targetList.add(new Enemy());        multi.clear();        for (int i = 0; i < robotsNum; i++)            robot[i].reset();        for (int i = 0; i < agentNum; i++)            multi.addAgent(new Agent((FipsRobot) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));    }    /**     * デバック用の描写     *     * @param g2     */    @Override    protected void debugPaint(Graphics2D g2) {        for (int i = 0; i < robotsNum; i++) {            if (robot[i].id == debugMode) {                robot[i].paint(g2);                //oncurrentModificationException防止用                HashMap<Integer, Intelligence> printMap = new HashMap(((FipsRobot) robot[i]).intelligenceHashMap);                for (Map.Entry<Integer, Intelligence> entry : printMap.entrySet()) {                    int swingX = (int) (entry.getValue().x * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startX);                    int swingY = (int) ((PsoSimulation.size - entry.getValue().y) * PsoSimulation.length / PsoSimulation.size + SimulationPanel.startY);                    if (robot[i].id == entry.getKey()) {                        g2.setColor(Color.YELLOW);                    } else if (((FipsRobot) robot[i]).debugKey == entry.getKey()) {                        g2.setColor(Color.CYAN);                    } else {                        g2.setColor(Color.GREEN);                    }                    g2.fillOval(swingX - 3, swingY - 3, 6, 6);                }                //((FipsRobot)robot[i]).printIntelligence();                break;            }        }    }    /**     * 読み込んだファイルから、フィールドを再現するメソッド     *     * @param fileString 読み込むファイル名     */    @Override    public void readFile(String fileString) {        //TODO    }}