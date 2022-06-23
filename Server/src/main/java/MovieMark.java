public class MovieMark {
    private int id;
    private String userName;
    private String movieName;
    private int mark;
    private double averageMark;

    public MovieMark(int id, String userName, String movieName, int mark, double averageMark) {
        this.id = id;
        this.userName = userName;
        this.movieName = movieName;
        this.mark = mark;
        this.averageMark = averageMark;
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

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public double getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(double averageMark) {
        this.averageMark = averageMark;
    }
}
