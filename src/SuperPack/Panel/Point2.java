package SuperPack.Panel;/** * Created by Ishiwatari on 2016/08/13. */public class Point2 {    public double x;    public double y;    public Point2(double x, double y) {        this.x = x;        this.y = y;    }    public Point2(Point2 p2) {        this.x = p2.x;        this.y = p2.y;    }    public void add(Point2 p2){        x+=p2.x;        y+=p2.y;    }    public void setLocation(double x, double y) {        this.x = x;        this.y = y;    }    public void setLocation(Point2 p2) {        this.x = p2.x;        this.y = p2.y;    }    public double distance(double x, double y) {        return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));    }    public double distance(Point2 p2) {        return distance(p2.x, p2.y);    }}