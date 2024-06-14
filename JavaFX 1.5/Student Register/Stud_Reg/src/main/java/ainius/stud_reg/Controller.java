package ainius.stud_reg;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;




public class Controller implements Initializable{

    @FXML
    private TextField groupField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField excelField;

    @FXML
    private ChoiceBox<String> selectGroup;

    @FXML
    private TableView<Student> tableFXML;

    @FXML
    private TableColumn<Student, String> nameTable;

    @FXML
    private TableColumn<Student, String> surnameTable;

    @FXML
    private TableColumn<Student, String> attendanceTable;

    @FXML
    private ChoiceBox<String> selectDate;


    int totalStudentNumber = 0;
    ObservableList<Student> allStudents = FXCollections.observableArrayList();
    ObservableList<Student> students = FXCollections.observableArrayList();

    String[] datesForStudents = new String[28];


    public void NewGroup(ActionEvent event){

        students.clear();
        tableFXML.setItems(students);

        String groupName = groupField.getText();
        selectGroup.getItems().add(groupName);

        groupField.clear();

    }

    public void newStudent(ActionEvent event){

        totalStudentNumber++;
        String name = nameField.getText();
        String surName = surnameField.getText();
        String attendance = "No";

        students = tableFXML.getItems();
        Student student = new Student(name, surName, selectGroup.getValue(), attendance);
        students.add(student);
        tableFXML.setItems(students);

        nameField.clear();
        surnameField.clear();

    }

    public void export(ActionEvent event) throws IOException {
        ExportExcel Excel = new ExportExcel();
        Excel.getTable(tableFXML);
        Excel.writeExcel(Excel.getCSVName(excelField.getText()));
    }

    public void importer(ActionEvent event) throws IOException {
        ImportExcel Excel = new ImportExcel();
        Excel.getCSV(Excel.getCSVName(excelField.getText()));
        totalStudentNumber += Excel.amount();
        students.clear();
        Excel.importer(students, selectDate.getItems());
        allStudents.addAll(students);
        tableFXML.setItems(students);
    }

    public void load(ActionEvent event) {

        students.clear();
        tableFXML.setItems(students);
        for (int i = 0; i < totalStudentNumber; ++i) {
            if (allStudents.get(i).getGroupName().equals(selectGroup.getValue()) && !allStudents.isEmpty()) {
                students.add(allStudents.get(i));
            }
        }
        tableFXML.setItems(students);
    }

    public void save(ActionEvent event){
        students = tableFXML.getItems();
        allStudents.addAll(students);
        students.clear();
        tableFXML.setItems(students);
        for(int i = 0; i < totalStudentNumber; ++i){
            for(int j = i + 1; j < totalStudentNumber; ++j){
                if(allStudents.get(i).equals(allStudents.get(j)) && !allStudents.isEmpty()){
                    allStudents.remove(i);
                    totalStudentNumber--;
                }
            }
        }
    }

    public void removeStudent(ActionEvent event){

        int selectID = tableFXML.getSelectionModel().getSelectedIndex();
        tableFXML.getItems().remove(selectID);

        Student selectStudent = tableFXML.getSelectionModel().getSelectedItem();
        for(int i = 0; i < totalStudentNumber; ++i){
            if(allStudents.get(i).equals(selectStudent)){
                allStudents.remove(i);
            }
        }
        totalStudentNumber--;
    }


    @FXML
    public void attendanceCheck(MouseEvent event){
        Student student = tableFXML.getSelectionModel().getSelectedItem();
        int SelectID = tableFXML.getSelectionModel().getSelectedIndex();
        if(student.getAttendance().equals("No"))
            student.setAttendance("Yes");
        else
            student.setAttendance("No");
        students.set(SelectID, student);
        tableFXML.setItems(students);

    }


    public void createPDF(ActionEvent event) throws DocumentException, FileNotFoundException {


        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Students.pdf"));
        document.open();
        String name, surname, group, attendance;
        document.add(new Paragraph("Vardas Pavarde Grupe Lankomumas "));
        for(int i = 0; i < totalStudentNumber; ++i){
            name = allStudents.get(i).getName();
            surname = allStudents.get(i).getSurname();
            group = allStudents.get(i).getGroupName();
            attendance = allStudents.get(i).getAttendance();
            document.add(new Paragraph(name+" "+surname+" "+group+" "+attendance));
        }
        document.close();
        writer.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTable.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        surnameTable.setCellValueFactory(new PropertyValueFactory<Student, String>("surname"));
        attendanceTable.setCellValueFactory(new PropertyValueFactory<Student, String>("attendance"));

        for(int i = 0; i < 28; ++i) {
            LocalDate date = LocalDate.now().plusDays(i);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd");
            String text = date.format(formatter);
            selectDate.getItems().add(text);
            datesForStudents[i] = text;
        }



    }
}