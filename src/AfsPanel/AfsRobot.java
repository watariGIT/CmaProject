package AfsPanel;import PsoPanel.PsoSimulation;import SuperPack.Panel.Intelligence;import SuperPack.Panel.Point2;import SuperPack.Panel.Robot;import SuperPack.Panel.SimulationPanel;import java.awt.*;/** * Created by Ishiwatari on 2016/11/24. */public class AfsRobot extends Robot {    Intelligence oldIntelligence;    //重みの決定    private static double ramdaA = 1800;//2;    private static double A = 0.2;    private static double B = 500;    private static double C = 60;    protected AfsRobot(SimulationPanel s) {        super(s);        oldIntelligence = new Intelligence(p.x, p.y, s.targetList);    }    protected AfsRobot(Point2 point, SimulationPanel s) {        super(point, s);        oldIntelligence = new Intelligence(p.x, p.y, s.targetList);    }    protected AfsRobot(SimulationPanel s, String robotString) {        super(s, robotString);        oldIntelligence = new Intelligence(p.x, p.y, s.targetList);    }    @Override    public void copy(Robot robot) {        super.copy(robot);        oldIntelligence = new Intelligence(p.x, p.y, field.targetList);    }    @Override    public void reset(double x, double y) {        super.reset(x, y);        oldIntelligence = new Intelligence(p.x, p.y, field.targetList);    }    @Override    public void move() {        //gの合計値の計算        Point2 sigma = new Point2(0, 0);        for (int i = 0; i < field.robotsNum; i++) {            if (field.robot[i] != this) {                field.communication_num++;                sigma.add(AttractionRepulsionFunc(field.robot[i].p));            }        }        //deltaの計算        double fitness = fitnessFunction(field.targetList);        double deltaX = p.x - oldIntelligence.x == 0 ? 0 : (ramdaA / 2) * (Math.pow(fitness, 2) - Math.pow(oldIntelligence.getFitnessValue(), 2)) / (p.x - oldIntelligence.x);        double deltaY = p.y - oldIntelligence.y == 0 ? 0 : (ramdaA / 2) * (Math.pow(fitness, 2) - Math.pow(oldIntelligence.getFitnessValue(), 2)) / (p.y - oldIntelligence.y);        oldIntelligence = new Intelligence(p.x, p.y, field.targetList);        //速度決定        Point2 v = new Point2(-1.0 * deltaX + sigma.x, -1.0 * deltaY + sigma.y);        if (v.distance() > maxv) {            double basis = maxv / v.distance();            v.setLocation(v.x * basis, v.y * basis);        }        //位置調整        p.x += v.x;        p.y += v.y;        if (p.x < 0) p.x = 0;        if (p.x > SimulationPanel.size) p.x = SimulationPanel.size;        if (p.y < 0) p.y = 0;        if (p.y > SimulationPanel.size) p.y = SimulationPanel.size;        /*if (id == 1) {            System.out.println("sigmaX: " + sigma.x + "sigmaY: " + sigma.y);            System.out.println("deltaX: " + deltaX + "deltaY: " + deltaY);            System.out.println("( " + v.x + ", " + v.y + ")");        }*/        angle = Math.atan2(v.y, v.x);    }    private Point2 AttractionRepulsionFunc(Point2 pj) {        double g = A - B * Math.exp(-1.0 * Math.pow(p.distance(pj), 2) / C);        double x = -1 * (p.x - pj.x) * g;        double y = -1 * (p.y - pj.y) * g;        /*if (id == 1) {            System.out.println("g: " + g + "( " + x + ", " + y + ") , " + B * Math.exp(-1.0 * Math.pow(p.distance(pj), 2) / C));        }*/        return new Point2(x, y);    }    @Override    protected Point getSwingPoint() {        return (new Point((int) (p.x * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startX),                (int) ((PsoSimulation.size - p.y) * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startY)));    }    static void setParam(double ramdaA, double A, double B, double C) {        AfsRobot.ramdaA = ramdaA;        AfsRobot.A = A;        AfsRobot.B = B;        AfsRobot.C = C;    }    /**     * CIの設定     */    @Override    public void setCI() {        //今回は使わない    }}