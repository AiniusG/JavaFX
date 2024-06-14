package ainius.loan;

public class Graph {
    private double x;
    private double y;

    public Graph(double x, double y, int amount){
        this.x = x;
        this.y = y;
    }
    public double getX(){
        return x;
    }
    public double getY() {
        return y;
    }

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

}
