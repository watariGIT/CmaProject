package Bat.OptimizeParam;

import AfsPanel.AfsSimulation;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by watariMac on 2016/11/28.
 *ABC用のパラメータ推定用クラス
 *
 */
public class OptimizeMain {
    public static void main(String args[]) {
        ArrayList<Particle> particles = new ArrayList<>();
        Particle globalBest;
        int experiencedNum = 20;
        int onlookerNum = 10;
        int scoutNum = 10;


        for (int i = 0; i < experiencedNum + onlookerNum + scoutNum; i++) {
            particles.add(new Particle(4));
            if (i % 10 != 0) {
                System.out.printf(" ");
            } else {
                System.out.printf("|");
            }
        }
        System.out.println("|");
        calsFitness(particles);

        Collections.sort(particles);
        globalBest = new Particle(particles.get(0));
        System.out.println(globalBest.toString());

        for (int count = 0; count < 100; count++) {
            //ABCに従った位置の決定
            Particle eBee = particles.get((int) (Math.random() * 40.0));
            for (int i = 0; i < particles.size(); i++) {
                Particle p = particles.get(i);
                if (i < experiencedNum) {
                    //評価値の高いやつ
                    p.experiencedBee(globalBest);
                } else if (i < experiencedNum + onlookerNum) {
                    //中位
                    p.onlookerBee(eBee);
                } else {
                    //低いやつ
                    p.scoutBee();
                }
            }

            calsFitness(particles);
            Collections.sort(particles);
            if (particles.get(0).fittness < globalBest.fittness) {
                globalBest = new Particle(particles.get(0));
            }
            System.out.println("count :" + count);
            System.out.println(globalBest.toString());
            if (count % 10 == 0) {
                for (Particle p : particles)
                    System.out.println(p);
            }
        }

    }

    private static void calsFitness(ArrayList<Particle> particles) {
        for (Particle p : particles) {
            double sum = 0;
            for (int i = 0; i < 10; i++) {
                AfsSimulation As = new AfsSimulation(100, p.xlist.get(0), p.xlist.get(1), p.xlist.get(2), p.xlist.get(3));
                sum += As.getCapturedStep();
            }
            p.setFitness(sum / 10);
            System.out.print(":");
        }
        System.out.println("END");
    }

}
