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

        simulations[0] = new GsoSimulation();
        simulations[1] = new AfsSimulation();
        simulations[2] = new MultiAgentSimulation2(30);
        simulations[3] = new ArpsoSimulation(30);




        //ロボットの台数を変化させて比較
        resultString+="robotNUM, ";
        for(SimulationPanel sp:simulations){
            resultString+=sp.getClass().getName()+",";
        }
        resultString+=BR;
        for (int robotNum = 30; robotNum < 100; robotNum += 10) {

            double[] results = new double[simulations.length];

            //100回実行平均をとる
            for (int count = 0; count < 100; count++) {

                simulations[0].setRobotNum(robotNum);
                simulations[1].copy(simulations[0]);
                simulations[2].copy(simulations[0]);
                simulations[3].copy(simulations[0]);

                for (int i = 0; i < simulations.length; i++) {
                    results[i] += simulations[i].getCapturedStep();
                }
                System.out.printf(".");
            }

            System.out.println("[" + robotNum + "]");
            resultString+=robotNum+", ";
            for (int i = 0; i < results.length; i++)
                resultString += results[i] / 100.0 + ", ";
            resultString += BR;
        }

        System.out.printf(resultString);

    }
}
