package ainius.stud_reg;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Group<Student> {

    private String groupName;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(){

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
