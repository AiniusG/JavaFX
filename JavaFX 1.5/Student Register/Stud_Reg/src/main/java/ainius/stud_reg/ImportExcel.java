package ainius.stud_reg;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class ImportExcel extends Excel{

    BufferedReader reader;
    StringBuilder stringBuilder;

    String name;
    String surname;
    String group;
    String attendance;
    String line = null;
    String ls = System.getProperty("line.separator");
    int rows = 0;
    String content;

    ObservableList<Student> list = FXCollections.observableArrayList();

    public void importer(ObservableList<Student> list, ObservableList<String> date) throws IOException {
        int start = rows;
        int i = 0;
        while(rows != 0){

            stringBuilder = new StringBuilder();
            while ((content.charAt(i) != ';') && (content.charAt(i) != '\n')){
                stringBuilder.append(content.charAt(i));
                ++i;
            }
            name = stringBuilder.toString();
            ++i;


            stringBuilder = new StringBuilder();
            while ((content.charAt(i) != ';') && (content.charAt(i) != '\n')){
                stringBuilder.append(content.charAt(i));
                ++i;
            }
            surname = stringBuilder.toString();
            ++i;


            stringBuilder = new StringBuilder();
            while ((content.charAt(i) != ';') && (content.charAt(i) != '\n')){
                stringBuilder.append(content.charAt(i));
                ++i;
            }
            group = stringBuilder.toString();
            ++i;

            stringBuilder = new StringBuilder();
            while ((content.charAt(i) != ';') && (content.charAt(i) != '\n')){
                stringBuilder.append(content.charAt(i));
                ++i;
            }
            attendance = stringBuilder.toString();

            if (rows != 1)
                ++i;

            if(rows != start ) {
                Student student = new Student(name, surname, group, attendance);
                list.add(student);
            }
            rows--;
        }
    }

    public void getCSV(String fileName) throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
        stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
            rows++;
        };
        reader.close();

        content = stringBuilder.toString();
    }

    public int amount(){
        return rows - 1;
    }
}