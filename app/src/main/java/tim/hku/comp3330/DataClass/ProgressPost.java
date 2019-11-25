package tim.hku.comp3330.DataClass;

public class ProgressPost {
    private int ProgressPostID;
    private int projectId;
    private int ownerID;
    private String title;
    private String content;
    private String created;

    public ProgressPost() {
        // empty constructor is required
    }

    public ProgressPost(int progressPostID, int projectId, int ownerID, String title, String content, String created) {
        ProgressPostID = progressPostID;
        this.projectId = projectId;
        this.ownerID = ownerID;
        this.title = title;
        this.content = content;
        this.created = created;
    }

    public int getProgressPostID() {
        return ProgressPostID;
    }

    public void setProgressPostID(int progressPostID) {
        ProgressPostID = progressPostID;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}


