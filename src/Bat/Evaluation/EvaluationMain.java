package Bat.Evaluation;

import AfsPanel.AfsSimulation;
import AgentPanel.MultiAgentSimulation2;
import ArpsoPanal.ArpsoSimulation;
import GsoPanel.GsoSimulation;
import SuperPack.AbstractAgentPanel.AgentSimulation;
import SuperPack.Panel.Point2;
import SuperPack.Panel.SimulationPanel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by watariMac on 2016/11/29.
 */
public class EvaluationMain {

    private static final String BR = System.getProperty("line.separator");//改行コード

    public static void main(String args[]) {
        SimulationPanel[] simulations = new SimulationPanel[4];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
        String resultString = sdf.format(Calendar.getInstance().getTime()) + BR;
        resultString += "target :" + SimulationPanel.targetNum + " field size :" + SimulationPanel.size + " maxCount" + SimulationPanel.maxCount + BR;

        simulations[0] = new AfsSimulation();
        simulations[1] = new GsoSimulation();
        simulations[2] = new MultiAgentSimulation2(30);
        simulations[3] = new ArpsoSimulation(30);

        resultString += evalTargetNum(simulations,100);

        System.out.printf(resultString);

    }

    static String evalRobotNum(SimulationPanel[] simulations, int maxcount) {
        //ロボットの台数を変化させて比較
        String resultString = "robotNUM, ";
        for (SimulationPanel sp : simulations) {
            resultString += sp.getClass().getName() + ",";
        }
        resultString += BR;
        for (int robotNum = 10; robotNum < 110; robotNum += 10) {

            double[] results = new double[simulations.length];

            //100回実行平均をとる
            for (int count = 0; count < maxcount; count++) {

                simulations[0].setRobotNum(robotNum);
                simulations[1].copy(simulations[0]);
                simulations[2].copy(simulations[0]);
                ((AgentSimulation) (simulations[2])).setAgentNum(robotNum);
                simulations[3].copy(simulations[0]);
                ((AgentSimulation) (simulations[3])).setAgentNum(robotNum);

                for (int i = 0; i < simulations.length; i++) {
                    results[i] += simulations[i].getCapturedStep();
                }
                System.out.printf(".");
            }

            System.out.println("[" + robotNum + "]");
            resultString += robotNum + ", ";
            for (int i = 0; i < results.length; i++)
                resultString += results[i] / maxcount + ", ";
            resultString += BR;
        }
        return resultString;
    }

    static String evalTargetNum(SimulationPanel[] simulations, int maxcount) {
        //ターゲットの台数を変化させて比較
        String resultString = "targetNUM, ";
        for (SimulationPanel sp : simulations) {
            resultString += sp.getClass().getName() + ",";
            sp.setRobotNum(50);
        }
        resultString += BR;
        for (int targetNum = 10; targetNum < 110; targetNum += 10) {

            double[] results = new double[simulations.length];

            //100回実行平均をとる
            for (int count = 0; count < maxcount; count++) {

                simulations[0].setTarget(targetNum);
                simulations[1].copy(simulations[0]);
                simulations[2].copy(simulations[0]);
                ((AgentSimulation) (simulations[2])).setAgentNum(targetNum);
                simulations[3].copy(simulations[0]);
                ((AgentSimulation) (simulations[3])).setAgentNum(targetNum);

                for (int i = 0; i < simulations.length; i++) {
                    results[i] += simulations[i].getCapturedStep();
                }
                System.out.printf(".");
            }

            System.out.println("[" + targetNum + "]");
            resultString += targetNum + ", ";
            for (int i = 0; i < results.length; i++)
                resultString += results[i] / maxcount + ", ";
            resultString += BR;
        }
        return resultString;
    }

    static String evalFieldSize(SimulationPanel[] simulations, int maxcount) {
        //ターゲットの台数を変化させて比較
        String resultString = "targetNUM, ";
        for (SimulationPanel sp : simulations) {
            resultString += sp.getClass().getName() + ",";
            sp.setRobotNum(50);
            sp.setTarget(10);
        }
        resultString += BR;
        for (int fieldSize = 300; fieldSize < 1000; fieldSize += 100) {

            double[] results = new double[simulations.length];

            //100回実行平均をとる
            for (int count = 0; count < maxcount; count++) {

                simulations[0].setFieldSize(fieldSize);
                simulations[1].copy(simulations[0]);
                simulations[2].copy(simulations[0]);
                ((AgentSimulation) (simulations[2])).setAgentNum(fieldSize);
                simulations[3].copy(simulations[0]);
                ((AgentSimulation) (simulations[3])).setAgentNum(fieldSize);

                for (int i = 0; i < simulations.length; i++) {
                    results[i] += simulations[i].getCapturedStep();
                }
                System.out.printf(".");
            }

            System.out.println("[" + fieldSize + "]");
            resultString += fieldSize + ", ";
            for (int i = 0; i < results.length; i++)
                resultString += results[i] / maxcount + ", ";
            resultString += BR;
        }
        return resultString;
    }

    /**
     * ターゲットの配置（分散を変えて評価）
     * @param simulations
     * @param maxcount
     * @return
     */
    static String evalTargetPoints(SimulationPanel[] simulations, int maxcount) {
        //ターゲットの分散を変化させて比較
        String resultString = "taeget sigma, ";
        for (SimulationPanel sp : simulations) {
            resultString += sp.getClass().getName() + ",";
            sp.setRobotNum(50);
            sp.setTarget(10);
        }
        resultString += BR;
        for (int sigma = 100; sigma < 500; sigma += 100) {

            double[] results = new double[simulations.length];

            //100回実行平均をとる
            for (int count = 0; count < maxcount; count++) {

                simulations[0].setTarget(getGaussianPoints(1000,sigma,10));
                simulations[1].copy(simulations[0]);
                simulations[2].copy(simulations[0]);
                ((AgentSimulation) (simulations[2])).setAgentNum(50);
                simulations[3].copy(simulations[0]);
                ((AgentSimulation) (simulations[3])).setAgentNum(50);

                for (int i = 0; i < simulations.length; i++) {
                    results[i] += simulations[i].getCapturedStep();
                }
                System.out.printf(".");
            }

            System.out.println("[" + sigma + "]");
            resultString += sigma + ", ";
            for (int i = 0; i < results.length; i++)
                resultString += results[i] / maxcount + ", ";
            resultString += BR;
        }
        return resultString;
    }


    static ArrayList<Point2> getGaussianPoints(int max, double sigma, double num) {
        Random randX = new Random();
        Random randY = new Random();
        ArrayList<Point2> points=new ArrayList();

        for (int n = 0; n < num; ) {
            double x = sigma * randX.nextGaussian() + max / 2;
            double y = sigma * randY.nextGaussian() + max / 2;
            if (x > 0 && x < max && y > 0 && y < max) {
                points.add(new Point2(x,y));
                n++;
            }
        }
        return points;
    }
}
