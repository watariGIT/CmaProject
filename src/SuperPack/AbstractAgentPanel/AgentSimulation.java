package SuperPack.AbstractAgentPanel;import SuperPack.Panel.Enemy;import SuperPack.Panel.SimulationPanel;import java.awt.*;/** * Created by Ishiwatari on 2016/10/25. */abstract public class AgentSimulation extends SimulationPanel {    public AgentList multi; //エージェントのリスト    public int agentNum;            //エージェントの最大数    @Override    public void step() {        count++;        //ロボットを動かす        for (int i = 0; i < robotsNum; i++) {            robot[i].move();        }        //エージェントを動かす        multi.agentsMove(robot);        //重複しているロボットの削除        for (int i = 0; i < robotsNum; i++) {            multi.deleteOverlapAgent((AgentRobot) (robot[i]));        }        //ターゲットの削除        deleteCaptureTarget();    }    @Override    public void copy(SimulationPanel s) {        super.copy(s);        //エージェントの初期化        multi.clear();        for (int i = 0; i < agentNum; i++)            multi.addAgent(new Agent((AgentRobot) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));    }    @Override    public void paintComponent(Graphics g) {        Graphics2D g2 = (Graphics2D) g;        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,                RenderingHints.VALUE_ANTIALIAS_ON);        g2.setColor(new Color(255, 255, 255));        g2.fillRect(startX, startY, length, length);        g2.setColor(Color.BLACK);        g2.drawRect(startX, startY, length, length);        g2.drawString("STEP:" + this.count, startX + 5, startY + length + 20);        //ターゲットの描写        synchronized (targetList) {            for (Enemy target : targetList) {                target.paint(g2, true);            }        }        if (debugMode == -1) {            for (int i = 0; i < robot.length; i++)                robot[i].paint(g2);            multi.paint(g2);        } else {            //デバック用の描写            debugPaint(g2);        }    }    @Override    public void reset() {        count = 0;        this.communication_num = 0;        targetList.clear();        for (int i = 0; i < targetNum; i++)            targetList.add(new Enemy());        multi.clear();        for (int i = 0; i < robotsNum; i++)            robot[i].reset();        for (int i = 0; i < agentNum; i++)            multi.addAgent(new Agent((AgentRobot) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));    }    @Override    public void readEnemyFile(String str) {        super.readEnemyFile(str);        multi.clear();        //エージェントの生成        for (int i = 0; i < agentNum; i++)            multi.addAgent(new Agent((AgentRobot) robot[i], new Color(105 + (i * 31) % 100, 55 + (i * 23) % 200, 105 + (i * 13) % 100)));    }    /**     * デバック用の描写     *     * @param g2     */    abstract protected void debugPaint(Graphics2D g2);}