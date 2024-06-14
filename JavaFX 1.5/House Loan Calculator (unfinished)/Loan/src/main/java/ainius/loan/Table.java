package ainius.loan;

public class Table {
    private double monthlyPayment;
    private double leftTotal;

    public Table(double monthlyPayment, double leftTotal){
        this.monthlyPayment = monthlyPayment;
        this.leftTotal = leftTotal;

    }
    public double getMonthlyPayment(){
        return monthlyPayment;
    }
    public double getLeftTotal(){
        return  leftTotal;
    }
    public void setMonthlyPayment(double monthlyPayment){
        this.monthlyPayment = monthlyPayment;
    }
    public void setLeftTotal(double leftTotal){
        this.leftTotal = leftTotal;
    }
}
