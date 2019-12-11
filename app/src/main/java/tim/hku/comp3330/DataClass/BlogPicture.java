package tim.hku.comp3330.DataClass;

public class BlogPicture {
    private int blogId;
    private String imageUrl;

    public BlogPicture() {
    }

    public BlogPicture(int blogId, String imageUrl) {
        this.blogId = blogId;
        this.imageUrl = imageUrl;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
