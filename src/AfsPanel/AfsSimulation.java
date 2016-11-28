package AfsPanel;import SuperPack.Panel.Enemy;import SuperPack.Panel.SimulationPanel;import java.util.ArrayList;/** * Created by Ishiwatari on 2016/11/24. */public class AfsSimulation extends SimulationPanel {    /**     * コンストラクタ     */    public AfsSimulation() {        robot = new AfsRobot[robotsNum];        targetList = new ArrayList<>();        for (int i = 0; i < targetNum; i++)            targetList.add(new Enemy());        for (int i = 0; i < robotsNum; i++)            robot[i] = new AfsRobot(this);    }    public AfsSimulation(int robotsNum,double ramdaA,double A,double B,double C) {        AfsRobot.setParam(ramdaA,A,B,C);        this.robotsNum = robotsNum;        robot = new AfsRobot[robotsNum];        targetList = new ArrayList<>();        for (int i = 0; i < targetNum; i++)            targetList.add(new Enemy());        for (int i = 0; i < robotsNum; i++)            robot[i] = new AfsRobot(this);    }    /**     * ロボットをファイルから読み込み配置する     *     * @param str     */    @Override    public void readRobotFile(String str) {        //ToDO    }    /**     * 1ステップフィールドを動かす     */    @Override    public void step() {        count++;        for (int i = 0; i < robotsNum; i++) {            robot[i].move();        }        //ターゲットの削除        deleteCaptureTarget();    }    /**     * 読み込んだファイルから、フィールドを再現するメソッド     *     * @param fileString 読み込むファイル名     */    @Override    public void readFile(String fileString) {    }}