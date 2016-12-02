package Bat.Evaluation;

import AfsPanel.AfsSimulation;
import AgentPanel.MultiAgentSimulation2;
import ArpsoPanal.ArpsoSimulation;
import GsoPanel.GsoSimulation;
import SuperPack.AbstractAgentPanel.AgentSimulation;
import SuperPack.Panel.SimulationPanel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

        resultString += evalFieldSize(simulations, 100);

        System.out.printf(resultString);

    }

    /**
     * ロボットの数を変化させて評価
     *
     * @param simulations
     * @param maxcount
     * @return
     */
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
                if (count % 10 == 0) System.out.printf(".");
            }

            System.out.println("[" + robotNum + "]");
            resultString += robotNum + ", ";
            for (int i = 0; i < results.length; i++)
                resultString += results[i] / maxcount + ", ";
            resultString += BR;
        }
        return resultString;
    }

    /**
     * ターゲットの数を変化させて評価を取る」
     * @param simulations
     * @param maxcount
     * @return
     */
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

                simulations[0].setTargetNum(targetNum);
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

            System.out.println("[" + targetNum + "]");
            resultString += targetNum + ", ";
            for (int i = 0; i < results.length; i++)
                resultString += results[i] / maxcount + ", ";
            resultString += BR;
        }
        return resultString;
    }

    /**
     * フィールドのサイズを変更させて比較
     * @param simulations
     * @param maxcount
     * @return
     */
    static String evalFieldSize(SimulationPanel[] simulations, int maxcount) {
        String resultString = "FieldSize, ";
        for (SimulationPanel sp : simulations) {
            resultString += sp.getClass().getName() + ",";
            sp.setRobotNum(50);
            sp.setTargetNum(10);
        }
        resultString += BR;
        for (int fieldSize = 300; fieldSize <= 1000; fieldSize += 100) {

            double[] results = new double[simulations.length];

            //100回実行平均をとる
            for (int count = 0; count < maxcount; count++) {

                simulations[0].setFieldSize(fieldSize);
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

            System.out.println("[" + fieldSize + "]");
            resultString += fieldSize + ", ";
            for (int i = 0; i < results.length; i++)
                resultString += results[i] / maxcount + ", ";
            resultString += BR;
        }
        return resultString;
    }
}
