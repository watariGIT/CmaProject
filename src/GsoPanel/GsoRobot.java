package GsoPanel;

import SuperPack.Panel.Point2;
import SuperPack.Panel.Robot;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by watariMac on 2016/11/10.
 */
public class GsoRobot extends Robot {
    //TODO 各パラメータをメンバに追加
    double luciferinLevel = 0;               //l(t)
    double variableRange;  //Rd
    final static double luciferinDecay = 0.5;       //ρ
    final static double luciferinEnencement = 10;  //γ
    final static double luciferinRange = 10; //Rs
    final static double stepSize=0.5; //s


    protected GsoRobot(SimulationPanel s) {
        super(s);
        luciferinLevel = luciferinEnencement * fitnessFunction(field.targetList);
    }

    protected GsoRobot(Point2 point, SimulationPanel s) {
        super(point, s);
        luciferinLevel = luciferinEnencement * fitnessFunction(field.targetList);
    }

    protected GsoRobot(SimulationPanel s, String robotString) {
        super(s, robotString);
        luciferinLevel = luciferinEnencement * fitnessFunction(field.targetList);
    }

    @Override
    public void move() {
        //速度を決定
        ArrayList<GsoRobot> neighborList = new ArrayList<>();
        //TODO neighborListの決定

        //確率の計算 jの決定
        //HashMap<Double,GsoRobot> neighborPMap=new HashMap<>();
        double lsum = 0;
        for (GsoRobot gr : neighborList) {
            lsum += gr.luciferinLevel - luciferinLevel;
        }
        double p0 = 0;
        double rand = Math.random();
        GsoRobot j=this;
        for (GsoRobot gr : neighborList) {
            double neighborP = gr.luciferinLevel - luciferinLevel / lsum;
            if (p0 < rand && rand <= neighborP) {
                j = gr;
                break;
            }
            p0+=neighborP;
        }

        //速度・位置の決定
        if(j!=this) {
            Point2 v = new Point2(stepSize*(j.p.x - p.x / j.p.distance(p)),stepSize*(j.p.y - p.y / j.p.distance(p)));

            if(v.x>maxv) v.x=maxv;
            if(v.x<-1*maxv) v.x=-1*maxv;
            if(v.y>maxv) v.y=maxv;
            if(v.y<-1*maxv)v.y=-1*maxv;

            p.x+=v.x;
            p.y+=v.y;

            if(p.x<0)p.x=0;
            if(p.x>SimulationPanel.size)p.x=SimulationPanel.size;
            if(p.y<0)p.y=0;
            if(p.y>SimulationPanel.size)p.y=SimulationPanel.size;

        }

        //TODO Rdの修正

        luciferinLevel = (1.0 - luciferinDecay) * luciferinLevel + luciferinEnencement * fitnessFunction(field.targetList);
    }


    @Override
    protected void captured() {
        //TODO 捕獲した時の処理
    }

    @Override
    public void copy(Robot robot) {
        //TODO
    }

    @Override
    public void reset() {
        //TODO
    }

    @Override
    protected Point getSwingPoint() {
        return null;
    }

    @Override
    public void setCI() {
        //TODO うまく
    }

    //TODO ハッショコードとequalメソッド
}
