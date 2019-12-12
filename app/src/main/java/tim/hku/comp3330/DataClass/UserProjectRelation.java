package tim.hku.comp3330.DataClass;

public class UserProjectRelation {
    private String userID;
    private int projectID;


    public UserProjectRelation() {
    }

    public UserProjectRelation(String userID, int projectID) {
        this.userID = userID;
        this.projectID = projectID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}
