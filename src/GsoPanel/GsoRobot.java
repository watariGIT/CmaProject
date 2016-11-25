package GsoPanel;

import PsoPanel.PsoSimulation;
import SuperPack.Panel.Point2;
import SuperPack.Panel.Robot;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by watariMac on 2016/11/10.
 */
public class GsoRobot extends Robot {
    //各パラメータをメンバに追加
    double luciferinLevel;  //l(t)
    double variableRange;  //Rd
    double lIni=5;    //l(t)のしょきち
    double vIni = 250;  //Rdのしょきち
    final static double luciferinDecay = 0.4;       //ρ
    final static double luciferinEnencement = 0.6;  //γ
    final static double luciferinRange = 300; //Rs
    final static double stepSize = maxv * 1.5; //s
    final static int desieredNeighbors = 5;


    protected GsoRobot(SimulationPanel s) {
        super(s);
        luciferinLevel = lIni;
        variableRange = vIni;
    }

    protected GsoRobot(Point2 point, SimulationPanel s) {
        super(point, s);
        luciferinLevel = lIni;
        variableRange = vIni;
    }

    protected GsoRobot(SimulationPanel s, String robotString) {
        super(s, robotString);
        luciferinLevel = lIni;
        variableRange = vIni;
    }

    @Override
    public void move() {
        //速度を決定

        //neighborListの決定
        ArrayList<GsoRobot> neighborList = new ArrayList<>();
        for(Robot r:field.robot){
            if(r.id!=this.id && r.p.distance(p) < variableRange){
                GsoRobot gr=(GsoRobot)r;
                if(gr.luciferinLevel < luciferinLevel)
                    neighborList.add(gr);
                field.communication_num++;
            }
        }

        //確率の計算 jの決定
        //HashMap<Double,GsoRobot> neighborPMap=new HashMap<>();
        double lsum = 0;
        for (GsoRobot gr : neighborList) {
            lsum += luciferinLevel - gr.luciferinLevel;
        }
        double p0 = 0;
        double rand = Math.random();
        GsoRobot j = this;
        for (GsoRobot gr : neighborList) {
            double neighborP = luciferinLevel - gr.luciferinLevel / lsum;
            if (p0 < rand && rand <= neighborP) {
                j = gr;
                break;
            }
            p0 += neighborP;
        }

        //速度・位置の決定
        if (j != this) {
            Point2 v = new Point2(stepSize * (j.p.x - p.x) / j.p.distance(p),
                    stepSize * (j.p.y - p.y) / j.p.distance(p));

            if (v.distance() > maxv) {
                double basis = maxv / v.distance();
                v.setLocation(v.x * basis, v.y * basis);
            }

            if (field.debugMode == id) {
                System.out.println("id:" + id);
                System.out.println("l=" + luciferinLevel + "/ rd=" + variableRange);
                System.out.println("N " + neighborList.size());
                System.out.println("dRd " + 5 * (desieredNeighbors - neighborList.size()));
                System.out.println("V( " + v.x + ", " + v.y + ")");
            }

            p.x += v.x;
            p.y += v.y;

            if (p.x < 0) p.x = 0;
            if (p.x > SimulationPanel.size) p.x = SimulationPanel.size;
            if (p.y < 0) p.y = 0;
            if (p.y > SimulationPanel.size) p.y = SimulationPanel.size;
            angle = Math.atan2(v.y, v.x);
        }

        //Rdの修正
        variableRange = Math.min(luciferinRange,
                Math.max(0, variableRange + 5 * (desieredNeighbors - neighborList.size())));


        luciferinLevel = (1.0 - luciferinDecay) * luciferinLevel
                + luciferinEnencement * fitnessFunction(field.targetList);
    }


    @Override
    protected void captured() {
        //TODO 捕獲した時の処理
    }

    @Override
    public void copy(Robot robot) {
        super.copy(robot);
        luciferinLevel = lIni;
        variableRange = vIni;
    }

    @Override
    public void reset() {
        super.reset();
        luciferinLevel = lIni;
        variableRange = vIni;
    }

    @Override
    protected Point getSwingPoint() {
        return (new Point((int) (p.x * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startX),
                (int) ((PsoSimulation.size - p.y) * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startY)));
    }

    @Override
    public void setCI() {
        //今回は使わない
    }

    //ハッショコードとequalメソッド?
}
