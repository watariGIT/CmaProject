package GsoPanel;

import SuperPack.Panel.Point2;
import SuperPack.Panel.Robot;
import SuperPack.Panel.SimulationPanel;

import java.awt.*;

/**
 * Created by watariMac on 2016/11/10.
 */
public class GsoRobot extends Robot {
    //TODO 各パラメータをメンバに追加



    protected GsoRobot(SimulationPanel s) {
        //TODO パラメータの初期化
        super(s);
    }

    protected GsoRobot(Point2 point, SimulationPanel s) {
        //TODO パラメータの初期化
        super(point, s);

    }

    protected GsoRobot(SimulationPanel s, String robotString) {
        super(s, robotString);
        //TODO パラメータの初期化

    }

    @Override
    public void move(){

    }



    @Override
    protected  void captured(){
        //TODO 捕獲した時の処理
    }

    @Override
    public void copy(Robot robot){
        //TODO
    }

    @Override
    public  void  reset(){
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
