package tim.hku.comp3330.DataClass;

public class Project {
    private int projectID;
    private String projectName;
    private String projectDescription;
    private String projectPic;
    private int ownerID;
    public Project(){}

    public Project(int projectID, String projectName, String projectDescription, String projectPic, int ownerID) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectPic = projectPic;
        this.ownerID = ownerID;
    }

// getters and setters


    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectPic() {
        return projectPic;
    }

    public void setProjectPic(String projectPic) {
        this.projectPic = projectPic;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
}
