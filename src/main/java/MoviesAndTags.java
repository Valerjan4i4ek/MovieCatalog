public class MoviesAndTags {
    private int id;
    private String userName;
    private String movieName;
    private String tag;
    private int tagCount;

    public MoviesAndTags(int id, String userName, String movieName, String tag, int tagCount) {
        this.id = id;
        this.userName = userName;
        this.movieName = movieName;
        this.tag = tag;
        this.tagCount = tagCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }
}
