package tim.hku.comp3330.DataClass;

public class Project {
    private int projectID;
    private String projectName;
    private String projectDescription;
    private int ownerID;
    public Project(){}
    public Project(int projectID, String projectName, String projectDescription, int ownerID){
        this.projectID = projectID;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.ownerID = ownerID;
    }

    // getters and setters

    public int getProjectID() {
        return projectID;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
}
