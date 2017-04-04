package OriginArpsoPanel;

import PsoPanel.PsoSimulation;
import SuperPack.Panel.Intelligence;
import SuperPack.Panel.Point2;
import SuperPack.Panel.Robot;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;

/**
 * Created by watariMac on 2017/03/31.
 */
public class ArPsoRobot extends Robot {
    Intelligence oldPI;
    //alpha>beta それぞれ[0,1]
    private final static double alpha = 0.4;
    private final static double beta = 0.8;
    private int time = 0;//前回捕まえてからの時間


    protected ArPsoRobot(SimulationPanel s) {
        super(s);
        oldPI = new Intelligence(PI);
        time = 0;

    }

    protected ArPsoRobot(Point2 point, SimulationPanel s) {
        super(s);
        oldPI = new Intelligence(PI);
        time = 0;
    }

    protected ArPsoRobot(SimulationPanel s, String robotString) {
        super(s);
        oldPI = new Intelligence(PI);
        time = 0;
    }

    public void reset(Point2 p) {
        super.reset(p);
        time = 0;
    }

    /**
     * A-RDPSOによって速度を決定する
     *
     * @return
     */
    @Override
    protected Point2 calVelocity() {

        //ARPSOに必要な式を計算
        double evolSpeed = 1.0 - (PI.getFitnessValue() / oldPI.getFitnessValue());
        double degree = getDegree();
        double maxTime = (double) field.maxCount / (double) field.targetList.size();
        double weightT = time / maxTime > 1 ? 1.0 : time / maxTime;

        double inertiaWeight = PI.getFitnessValue() <= CI.getFitnessValue() ?
                2.0 - (2.0 - 1.0 / 2.0) * weightT : 1.0;

        //omega,c2の決定
        double omega = inertiaWeight * (1 - alpha * evolSpeed + beta * degree);
        c2 = field.robotsNum * 0.4 + c1 < 2 * c1 ? field.robotsNum * 0.4 + c1 : c1 * 2;

        //ARPSOに従って動かす
        Point2 v = new Point2(this.v.x * omega + c1 * Math.random() * (PI.x - p.x) + c2 * Math.random() * (CI.x - p.x),
                this.v.y * omega + c1 * Math.random() * (PI.y - p.y) + c2 * Math.random() * (CI.y - p.y));
        oldPI = new Intelligence(PI);

        if (id == field.debugMode) {
            System.out.println("----------id: " + id + "------------");
            System.out.println("v( " + v.x + ", " + v.y + ")");
            System.out.println("oldPI= " + oldPI.getFitnessValue());
            System.out.println("omega " + omega + "/C1 " + c1 + "/c2 " + c2);
            System.out.println("PI " + PI.getFitnessValue() + "/CI " + CI.getFitnessValue() + "/Fitness " + fitnessFunction(field.targetList));
            System.out.println("21 /evolS " + evolSpeed + "/degree " + degree + "/y " + inertiaWeight + "/omega " + omega);
        }

        time++;
        return v;
    }

    private double getDegree() {
        double sum = 0.0;
        double min = Double.MAX_VALUE;

        for (int i = 0; i < field.robotsNum; i++) {
            double fitness = new Intelligence(field.robot[i].p.x, field.robot[i].p.y, field.targetList).getFitnessValue();
            sum += fitness;
            if (fitness < min)
                min = fitness;
        }

        return min / (sum / field.robotsNum);
    }

    @Override
    protected Point getSwingPoint() {
        return (new Point((int) (p.x * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startX),
                (int) ((PsoSimulation.size - p.y) * PsoSimulation.length / PsoSimulation.size + PsoSimulation.startY)));
    }

    @Override
    public void setCI() {
        if (CI == null)
            CI = new Intelligence(p.x, p.y, field.targetList);

        if (fitnessFunction(field.targetList) < CI.getFitnessValue()) {
            CI = new Intelligence(p.x, p.y, field.targetList);
            for (int i = 0; i < field.robotsNum; i++) {
                if (this != field.robot[i]) {
                    field.robot[i].CI = new Intelligence(CI);
                    field.communication_num++;
                }
            }
        }
    }

}
