package Bat.OptimizeParam;

import java.util.ArrayList;

/**
 * Created by watariMac on 2016/11/28.
 */
public class Particle implements Comparable {
    final static double X_MAX = 100;
    final static double X_MIN = 0.0000000000001;

    public ArrayList<Double> xlist = new ArrayList<>();
    public ArrayList<Double> pBest = new ArrayList<>(); //personalBest
    public double fittness;
    public double bestFittness;

    Particle(int length) {
        for (int i = 0; i < length; i++) {
            xlist.add(Math.random() * X_MAX);
            pBest.add(xlist.get(i));
        }
        fittness = Double.MAX_VALUE;
        bestFittness = Double.MAX_VALUE;
    }

    Particle(Particle p) {
        xlist = new ArrayList<>(p.xlist);
        pBest = new ArrayList<>(p.pBest);

        fittness = p.fittness;
        bestFittness = p.bestFittness;
    }

    public void setFitness(double val) {
        fittness = val;
        if (val < bestFittness) {
            bestFittness = val;
            pBest = new ArrayList<>(xlist);
        }
    }

    /**
     * 評価値の高い鉢の振る舞い
     */
    public void experiencedBee(Particle gBest) {
        for (int i = 0; i < xlist.size(); i++) {
            double gb = gBest.pBest.get(i);
            double pb = pBest.get(i);
            double x = xlist.get(i) + Math.random() * 2.0 * (gb - xlist.get(i)) + Math.random() * 2.0 * (pb - xlist.get(i));
            if (x < X_MIN) x = X_MIN;
            xlist.set(i, x);
        }
    }

    /**
     * 評価値の中位の鉢の振る舞い
     */
    public void onlookerBee(Particle eBee) {
        for (int i = 0; i < xlist.size(); i++) {
            double ep = eBee.pBest.get(i);
            double x = xlist.get(i) + Math.random() * 2.0 * (ep - xlist.get(i));
            if (x < X_MIN) x = X_MIN;
            xlist.set(i, x);
        }
    }

    /**
     * 評価値が低い鉢の振る舞い
     */
    public void scoutBee() {
        for (int i = 0; i < xlist.size(); i++) {
            double x = xlist.get(i) + 150.0 - Math.random() * 300.0;
            if (x < X_MIN) x = X_MIN;
            xlist.set(i, x);
        }
    }


    /**
     * 足し算
     *
     * @param p
     */
    void add(Particle p) {
        for (int i = 0; i < xlist.size(); i++) {
            double x = xlist.get(i) + p.xlist.get(i);
            if (x > X_MAX) x = X_MAX;
            if (x < X_MIN) x = X_MIN;
            xlist.set(i, x);
        }
    }

    /**
     * 引き算
     *
     * @param p
     */
    void sub(Particle p) {
        for (int i = 0; i < xlist.size(); i++) {
            double x = xlist.get(i) - p.xlist.get(i);
            if (x > X_MAX) x = X_MAX;
            if (x < X_MIN) x = X_MIN;
            xlist.set(i, x);
        }
    }

    void sub() {
        for (int i = 0; i < xlist.size(); i++) {
            double x = pBest.get(i) - xlist.get(i);
            if (x > X_MAX) x = X_MAX;
            if (x < X_MIN) x = X_MIN;
            xlist.set(i, x);
        }
    }

    /**
     * 掛け算
     *
     * @param r
     */
    void mul(double r) {
        for (int i = 0; i < xlist.size(); i++) {
            double x = xlist.get(i) * r;
            if (x > X_MAX) x = X_MAX;
            if (x < X_MIN) x = X_MIN;
            xlist.set(i, x);
        }
    }

    /**
     * 割り算
     *
     * @param r
     */
    void div(double r) {
        for (int i = 0; i < xlist.size(); i++) {
            double x = xlist.get(i) / r;
            if (x > X_MAX) x = X_MAX;
            if (x < X_MIN) x = X_MIN;
            xlist.set(i, x);
        }
    }

    @Override
    public int compareTo(Object o) {
        Particle p = (Particle) o;
        return (int) ((fittness - p.fittness) * 1000.0);
    }

    @Override
    public String toString() {
        String str = "x[ ";
        for (double x : xlist) {
            str += String.format("%1$.4f, ", x);
        }
        str += "] val " + fittness + " p[ ";

        for (double p : pBest) {
            str += String.format("%1$.4f, ", p);
        }
        str += "] val " + bestFittness;
        return str;
    }
}

