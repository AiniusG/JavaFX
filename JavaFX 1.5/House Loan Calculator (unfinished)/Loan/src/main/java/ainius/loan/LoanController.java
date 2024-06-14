package ainius.loan;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;

public class LoanController implements Initializable {


    @FXML
    private TextField loanAmount;

    @FXML
    private TextField loanYears;

    @FXML
    private TextField loanMonths;
    @FXML
    private TextField interest;

    @FXML
    private ChoiceBox<String> loanPayment;
    private final String[] loanPaymentTypes = {"Anuiteto", "Linijinis"};

    @FXML
    private Button loanCalculate;

    @FXML
    private Text errorMessage;

    @FXML
    private Text filterErrorMessage;

    @FXML
    private TableView<Table> loanTable;

    @FXML
    private TableColumn<Table, Double> loanLeftPayment;

    @FXML
    private TableColumn<Table, Double> loanMonthPayment;


    @FXML
    private TextField filterMonth1;

    @FXML
    private TextField filterMonth2;

    @FXML
    private TextField filterYear1;

    @FXML
    private TextField filterYear2;
    @FXML
    private TextField month10;

    @FXML
    private TextField month11;

    @FXML
    private TextField year10;

    @FXML
    private TextField year11;

    @FXML
    private LineChart<Graph, Double> chart;
    ObservableList<Table> list = FXCollections.observableArrayList();

    int years, months, year1, year2, month1, month2, y10, y11, m10, m11;
    double sum, koef, anui, payment;
    public static double PERCENTAGE;
    public static final double PERIOD = 12;


    public void calculate(ActionEvent event) {
        try {
            this.sum = Double.parseDouble(loanAmount.getText());
            this.months = Integer.parseInt(loanMonths.getText());
            this.years = Integer.parseInt(loanYears.getText());
            this.PERCENTAGE = Double.parseDouble(interest.getText()) * 0.01;
            String type = loanPayment.getValue();

            int timePeriod = this.months + (this.years * 12);
            double interest = PERCENTAGE / PERIOD;
            if (this.sum > 0 && ((this.years >= 0 && this.months > 0 && this.months < 12) || (this.years > 0 && this.months >= 0 && this.months < 12))) {
                if(year10.getText().trim().isEmpty() || year11.getText().trim().isEmpty() || month10.getText().trim().isEmpty() || month11.getText().trim().isEmpty() ) {
                    loanTable.getItems().clear();
                    if (type.equals("Anuiteto")) {
                        System.out.println(type);
                        koef = (interest * (Math.pow(1 + interest, timePeriod))) / ((Math.pow(1 + interest, timePeriod)) - 1);
                        anui = Math.floor(koef * this.sum * 100) / 100;
                        payment = Math.floor(anui * timePeriod * 100) / 100;
                        double[] graphInfo = new double[timePeriod];
                        for (int i = 0; i < timePeriod; ++i) {
                            payment -= anui;
                            payment = Math.floor(payment * 100) / 100;
                            Table table = new Table(anui, payment);
                            ObservableList<Table> tables = loanTable.getItems();
                            tables.add(table);
                            loanTable.setItems(tables);
                            graphInfo[i] = anui;
                        }
                        draw(timePeriod, graphInfo);

                    } else if (type.equals("Linijinis")) {
                        System.out.println(type);
                        payment = this.sum;
                        double kosntanta = this.sum / timePeriod;
                        double[] graphInfo = new double[timePeriod];
                        for (int i = 0; i < timePeriod; ++i) {
                            double temp = payment * (interest + 1) - payment + kosntanta;
                            System.out.println(kosntanta);
                            System.out.println(temp);
                            payment -= temp;
                            payment = Math.floor(payment * 100) / 100;
                            temp = Math.floor(temp * 100) / 100;
                            Table table = new Table(temp, payment);
                            ObservableList<Table> tables = loanTable.getItems();
                            tables.add(table);
                            loanTable.setItems(tables);
                            graphInfo[i] = temp;
                        }
                        draw(timePeriod, graphInfo);
                    }
                }
                else{
                    this.y10 = Integer.parseInt(year10.getText());
                    this.y11 = Integer.parseInt(year11.getText());
                    this.m10 = Integer.parseInt(month10.getText());
                    this.m11 = Integer.parseInt(month11.getText());
                    loanTable.getItems().clear();

                    timePeriod += ((y11 * 12 + m11) -(y10 * 12 + m10)) + 2;
                    int timePeriod1 = y10 * 12 + m10;
                    int timePeriod2 = y11 * 12 + m11;
                    if (type.equals("Anuiteto")) {
                        System.out.println(type);
                        koef = (interest * (Math.pow(1 + interest, timePeriod))) / ((Math.pow(1 + interest, timePeriod)) - 1);
                        anui = Math.floor(koef * this.sum * 100) / 100;
                        payment = Math.floor(anui * timePeriod * 100) / 100;
                        double[] graphInfo = new double[timePeriod];
                        for (int i = 0; i < timePeriod; ++i) {
                            if(i <= timePeriod1 || i >= timePeriod2 ) {
                                payment -= anui;
                                payment = Math.floor(payment * 100) / 100;
                                Table table = new Table(anui, payment);
                                ObservableList<Table> tables = loanTable.getItems();
                                tables.add(table);
                                loanTable.setItems(tables);
                                graphInfo[i] = anui;
                            }
                            else{
                                double temp = anui/timePeriod;
                                Table table = new Table(temp, payment);
                                ObservableList<Table> tables = loanTable.getItems();
                                tables.add(table);
                                loanTable.setItems(tables);
                                graphInfo[i] = anui;
                            }
                        }
                        draw(timePeriod, graphInfo);
                    }

                }
            } else if (this.months >= 12) {
                errorMessage.setText("ERROR: months can not exceed 12");
            } else {
                errorMessage.setText("ERROR: number values are negative or dont exist");
            }
        } catch (NumberFormatException e) {
            errorMessage.setText("ERROR: number values are negative or dont exist");
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void filter(ActionEvent event) {
        try {
            this.year1 = Integer.parseInt(filterYear1.getText());
            this.year2 = Integer.parseInt(filterYear2.getText());
            this.month1 = Integer.parseInt(filterMonth1.getText());
            this.month2 = Integer.parseInt(filterMonth2.getText());
            String type = loanPayment.getValue();

            int timePeriod = months + years * 12, timePeriod1 = year1 * 12 + month1, timePeriod2 = year2 * 12 + month2;
            double interest = PERCENTAGE / PERIOD;
            //System.out.println(years);
            if (true) {//check for bad year inputs
                loanTable.getItems().clear();
                if (type.equals("Anuiteto")) {
                    koef = (interest * (Math.pow(1 + interest, timePeriod))) / ((Math.pow(1 + interest, timePeriod)) - 1);
                    anui = Math.floor(koef * this.sum * 100) / 100;
                    payment = Math.floor(anui * timePeriod * 100) / 100;
                    for (int i = 0; i < timePeriod1; ++i) {
                        payment -= anui;
                        payment = Math.floor(payment * 100) / 100;
                    }
                    for (int i = timePeriod1; i < timePeriod2; ++i) {
                        payment -= anui;
                        payment = Math.floor(payment * 100) / 100;
                        Table table = new Table(anui, payment);
                        ObservableList<Table> tables = loanTable.getItems();
                        tables.add(table);
                        loanTable.setItems(tables);
                    }
                } else if (type.equals("Linijinis")) {
                    payment = this.sum;
                    double kosntanta = this.sum / timePeriod;
                    for (int i = 0; i < timePeriod1; ++i) {
                        double temp = payment * (interest + 1) - payment + kosntanta;
                        System.out.println(kosntanta);
                        System.out.println(temp);
                        payment -= temp;
                    }
                    for (int i = timePeriod1; i < timePeriod2; ++i) {
                        double temp = payment * (interest + 1) - payment + kosntanta;
                        System.out.println(kosntanta);
                        System.out.println(temp);
                        payment -= temp;
                        temp = Math.floor(temp * 100) / 100;
                        payment = Math.floor(payment * 100) / 100;
                        Table table = new Table(temp, payment);
                        ObservableList<Table> tables = loanTable.getItems();
                        tables.add(table);
                        loanTable.setItems(tables);
                    }
                }
            }
        } catch (NumberFormatException e) {
            filterErrorMessage.setText("ERROR: number values are negative or dont exist");
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void draw(int x, double[] y) {
        System.out.println(x);
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < x; ++i) {
            series.getData().add(new XYChart.Data(i + "", y[i]));
        }
        chart.getData().add(series);

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanPayment.getItems().addAll(loanPaymentTypes);
        loanLeftPayment.setCellValueFactory(new PropertyValueFactory<Table, Double>("leftTotal"));
        loanMonthPayment.setCellValueFactory(new PropertyValueFactory<Table, Double>("monthlyPayment"));
        loanTable.setItems(list);

    }

    public void txt(ActionEvent event) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("ataskaita.txt"));
            for (Table list : list) {
                writer.write(list.getLeftTotal() + "," + list.getMonthlyPayment() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
