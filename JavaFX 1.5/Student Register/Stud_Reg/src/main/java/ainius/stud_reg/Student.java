package ainius.stud_reg;

import javafx.collections.ObservableList;

public class Student extends Human{

    private String name;
    private String surname;
    private String groupName;
    private String attendance;


    public Student(String name, String surname, String groupName, String attendance) {
        this.name = name;
        this.surname = surname;
        this.groupName = groupName;
        this.attendance = attendance;

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getAttendance() {
        return attendance;
    }


    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

}
