package ainius.stud_reg;

import javafx.scene.control.TableView;

import java.io.*;

public class ExportExcel extends Excel{

    String[] name;
    String[] surname;
    String[] group;
    String[] attendance;
    int rows;
    public void writeExcel(String title) throws IOException {


        Writer writer = null;
        try {
            File file = new File(title);
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("Names;Last Names;Group;Attendance\n");
            for (int i = 0; i < rows; i++) {
                String text = this.name[i] + ";" + this.surname[i] + ";" + this.group[i] + ";" + this.attendance[i] +"\n";

                writer.write(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            writer.flush();
            writer.close();
        }
    }

    public void getTable(TableView<Student> tableView){
        this.rows = tableView.getItems().size();
        name = new String[rows];
        surname = new String[rows];
        group = new String[rows];
        attendance = new String[rows];
        for(int i = 0; i < rows; ++i){
            this.name[i] = tableView.getItems().get(i).getName();
            this.surname[i] = tableView.getItems().get(i).getSurname();
            this.group[i] = tableView.getItems().get(i).getGroupName();
            this.attendance[i] = tableView.getItems().get(i).getAttendance();
        }
    }


}