package Bat.Evaluation;

import AfsPanel.AfsSimulation;
import AgentPanel.MultiAgentSimulation2;
import ArpsoPanal.ArpsoSimulation;
import GsoPanel.GsoSimulation;
import SuperPack.Panel.SimulationPanel;

/**
 * Created by watariMac on 2016/11/29.
 */
public class EvaluationMain {

    private static final String BR = System.getProperty("line.separator");//改行コード

    public static void main(String args[]) {
        SimulationPanel[] simulations = new SimulationPanel[4];

        String resultString = "";

        for (int robotNum = 10; robotNum < 100; robotNum += 10) {
            simulations[0] = new GsoSimulation();
            simulations[1] = new AfsSimulation();
            simulations[2] = new MultiAgentSimulation2(10);
            simulations[3] = new ArpsoSimulation(10);

            for(SimulationPanel sp:simulations){
                resultString+=sp.getClass().getName()+",";
            }
            resultString+=BR;

            //100回実行平均をとる
            for (int count = 0; count < 100; count++) {
                double[] results = new double[simulations.length];

                for (int i = 0; i < simulations.length; i++) {
                    simulations[i].setRobotNum(robotNum);
                    results[i] += simulations[i].getCapturedStep();
                    System.out.printf(".");
                }

                System.out.println("[" + count + "]");
                for (int i = 0; i < results.length; i++)
                    resultString += results[i] / 100.0 + ",";

                resultString += BR;
            }
        }

        System.out.printf(resultString);

    }
}
