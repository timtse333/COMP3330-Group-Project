package tim.hku.comp3330.DataClass;

public class BlogPost {
    private int BlogPostID;
    private int projectId;
    private String ownerID;
    private String content;
    private String blogPostPic;
    private String created;

    public BlogPost() {
        // empty constructor
    }

    public BlogPost(int blogPostID, int projectId, String ownerID, String content, String blogPostPic, String created) {
        BlogPostID = blogPostID;
        this.projectId = projectId;
        this.ownerID = ownerID;
        this.content = content;
        this.blogPostPic = blogPostPic;
        this.created = created;
    }

    public int getBlogPostID() {
        return BlogPostID;
    }

    public void setBlogPostID(int blogPostID) {
        BlogPostID = blogPostID;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBlogPostPic() {
        return blogPostPic;
    }

    public void setBlogPostPic(String blogPostPic) {
        this.blogPostPic = blogPostPic;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}




