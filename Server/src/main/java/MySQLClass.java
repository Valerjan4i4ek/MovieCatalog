import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class MySQLClass {

    public MySQLClass(){
        baseCreate();
        tableAuthorizationCreate();
        tableMovieMarkCreate();
        tableMoviesAndTagsCreate();
    }

    public Connection getConnection(String dbName) throws SQLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String url = "jdbc:mysql://localhost/" + ((dbName != null)? (dbName) : (""));
        String username = "root";
        String password = "1234";
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

        return DriverManager.getConnection(url, username, password);
    }

    public void baseCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection(null);
                st = conn.createStatement();
                st.executeUpdate("CREATE DATABASE IF NOT EXISTS MovieCatalog");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tableMoviesAndTagsCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("MovieCatalog");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS MovieCatalog.MoviesAndTags " +
                        "(id INT NOT NULL, userName VARCHAR(20) NOT NULL, movieName VARCHAR(20) NOT NULL, " +
                        "tag VARCHAR(20) NOT NULL, tagCount INT NOT NULL)");
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tableMovieMarkCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("MovieCatalog");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS MovieCatalog.MovieMark " +
                        "(id INT NOT NULL, userName VARCHAR(20) NOT NULL, movieName VARCHAR(20) NOT NULL, " +
                        "mark INT NOT NULL, averageMark DOUBLE NOT NULL)");
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void tableAuthorizationCreate(){
        try{
            Connection conn = null;
            Statement st = null;

            try{
                conn = getConnection("MovieCatalog");
                st = conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS MovieCatalog.authorization " +
                        "(id INT NOT NULL, login VARCHAR(20) NOT NULL, password VARCHAR(20) NOT NULL)");
            }
            finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(st != null){
                        st.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addMovieAndTags(MoviesAndTags moviesAndTag){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("MovieCatalog");
                ps = conn.prepareStatement("INSERT INTO MoviesAndTags (id, userName, movieName, tag, tagCount) VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, moviesAndTag.getId());
                ps.setString(2, moviesAndTag.getUserName());
                ps.setString(3, moviesAndTag.getMovieName());
                ps.setString(4, moviesAndTag.getTag());
                ps.setInt(5, moviesAndTag.getTagCount());
                ps.executeUpdate();
            }finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addMovieMark(MovieMark movieMark){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("MovieCatalog");
                ps = conn.prepareStatement("INSERT INTO MovieMark (id, userName, movieName, mark, averageMark) VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, movieMark.getId());
                ps.setString(2, movieMark.getUserName());
                ps.setString(3, movieMark.getMovieName());
                ps.setInt(4, movieMark.getMark());
                ps.setDouble(5, movieMark.getAverageMark());
                ps.executeUpdate();
            }finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addAuthorization(User user){
        try{
            Connection conn = null;
            PreparedStatement ps = null;

            try{
                conn = getConnection("MovieCatalog");
                ps = conn.prepareStatement("INSERT INTO authorization (id, login, password) VALUES (?, ?, ?)");
                ps.setInt(1, user.getId());
                ps.setString(2, user.getUserName());
                ps.setString(3, user.getUserPassword());
                ps.executeUpdate();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getTagFromMoviesAndTags(){
        List<String> list = new ArrayList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("MovieCatalog");
                String query = "SELECT tag FROM MoviesAndTags";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    String tag = rs.getString("tag");
                    list.add(tag);
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public List<Integer> getMovieMarks(String movieName){
        List<Integer> list = new ArrayList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("MovieCatalog");
                String query = "SELECT mark FROM MovieMark where movieName = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, movieName);
                rs = ps.executeQuery();

                while (rs.next()){
                    int mark = rs.getInt("mark");
                    list.add(mark);
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public List<String> getMoviesFromSimilarUserByLovelyTags(List<String> lovelyTags, List<String> topMoviesFromSimilarUsers){
        List<String> list = new ArrayList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            for (int i = 0; i < topMoviesFromSimilarUsers.size(); i++) {
                try{
                    conn = getConnection("MovieCatalog");
                    String query = "SELECT tag FROM MoviesAndTags WHERE movieName = ?";
                    ps = conn.prepareStatement(query);
                    ps.setString(1, topMoviesFromSimilarUsers.get(i));
                    rs = ps.executeQuery();

                    while (rs.next()){
                        String tag = rs.getString("tag");
                        if(lovelyTags.contains(tag) && !list.contains(topMoviesFromSimilarUsers.get(i))){
                            list.add(topMoviesFromSimilarUsers.get(i));
                        }
                    }
                }finally {
                    try{
                        if(conn != null){
                            conn.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(ps != null){
                            ps.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(rs != null){
                            rs.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getTopMoviesFromSimilarUsers(List<String> similarUsers){
        List<String> list = new ArrayList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            for (int i = 0; i < similarUsers.size(); i++) {
                try{
                    conn = getConnection("MovieCatalog");
                    String query = "SELECT * FROM MovieMark WHERE userName = ?";
                    ps = conn.prepareStatement(query);
                    ps.setString(1, similarUsers.get(i));
                    rs = ps.executeQuery();

                    while (rs.next()){
                        String movieName = rs.getString("movieName");
                        int mark = rs.getInt("mark");
                        if(mark >= 4 && !list.contains(movieName)){
                            list.add(movieName);
                        }
                    }
                } finally {
                    try{
                        if(conn != null){
                            conn.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(ps != null){
                            ps.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(rs != null){
                            rs.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

//    public List<String> getLovelyTags(List<String> tagsName){
//        List<String> list = new ArrayList<>();
//        Map<String, Integer> map = new HashMap<>();
//        int j = 0;
//
//        try{
//            Connection conn = null;
//            PreparedStatement ps = null;
//            ResultSet rs = null;
//
//            for (int i = 0; i < tagsName.size(); i++) {
//                try{
//                    conn = getConnection("MovieCatalog");
//                    String query = "SELECT tagCount FROM MoviesAndTags WHERE tag = ? ORDER BY id DESC LIMIT 1";
//                    ps = conn.prepareStatement(query);
//                    ps.setString(1, tagsName.get(i));
//                    rs = ps.executeQuery();
//
//                    while (rs.next()){
//                        int tagCount = rs.getInt("tagCount");
//                        map.put(tagsName.get(i), tagCount);
//                    }
//                } finally {
//                    try{
//                        if(conn != null){
//                            conn.close();
//                        }
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    try{
//                        if(ps != null){
//                            ps.close();
//                        }
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    try{
//                        if(rs != null){
//                            rs.close();
//                        }
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        map.entrySet().stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (a, b) -> a,
//                        LinkedHashMap::new
//                ));
//        while (j < 3){
//            for(Map.Entry<String, Integer> entry : map.entrySet()){
//                list.add(entry.getKey());
//                j++;
//            }
//        }
//        return list;
//    }

    public List<String> getLovelyTags(List<String> moviesName){
        List<String> list = new LinkedList<>();
        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            for (int i = 0; i < moviesName.size(); i++) {
                try{
                    conn = getConnection("MovieCatalog");
                    String query = "SELECT tag FROM MoviesAndTags WHERE movieName = ?";
                    ps = conn.prepareStatement(query);
                    ps.setString(1, moviesName.get(i));
                    rs = ps.executeQuery();

                    while (rs.next()){
                        String tag = rs.getString("tag");
                        if(!list.contains(tag)){
                            list.add(tag);
                        }

                    }
                } finally {
                    try{
                        if(conn != null){
                            conn.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(ps != null){
                            ps.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(rs != null){
                            rs.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public List<String> getUsersWithHighMarksToSimilarMovies(List<String> moviesName, String mainUser){
        List<String> list = new ArrayList<>();
        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            for (int i = 0; i < moviesName.size(); i++) {
                try{
                    conn = getConnection("MovieCatalog");
                    String query = "SELECT userName, mark FROM MovieMark WHERE movieName = ? ORDER BY id DESC";
                    ps = conn.prepareStatement(query);
                    ps.setString(1, moviesName.get(i));
                    rs = ps.executeQuery();

                    while (rs.next()){
                        String userName = rs.getString("userName");
                        int mark = rs.getInt("mark");
                        if(mark >= 4 && !list.contains(userName) && !userName.equals(mainUser)){
                            list.add(userName);
                        }
                    }
                } finally {
                    try{
                        if(conn != null){
                            conn.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(ps != null){
                            ps.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(rs != null){
                            rs.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getMovieByUserWithHighMark(String userName){
        List<String> list = new LinkedList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("MovieCatalog");
                String query = "SELECT mark, movieName FROM MovieMark WHERE userName = ? ORDER BY id DESC";
                ps = conn.prepareStatement(query);
                ps.setString(1, userName);
                rs = ps.executeQuery();

                while (rs.next()){
                    int mark = rs.getInt("mark");
                    String movieName = rs.getString("movieName");
                    if(mark >= 4){
                        if(!list.contains(movieName)){
                            list.add(movieName);
                        }
                        else{
                            list.set(list.indexOf(movieName), movieName);
                        }
                    }
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public Integer getTagCountFromMoviesAndTags(String tag){
        int i = 0;

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("MovieCatalog");
                String query = "SELECT tagCount FROM MoviesAndTags WHERE tag = ? ORDER BY id DESC LIMIT 1";
                ps = conn.prepareStatement(query);
                ps.setString(1, tag);
                rs = ps.executeQuery();

                while (rs.next()){
                    i = rs.getInt("tagCount");
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return i;
    }

    public Map<String, Double> getAverageMarkByMovieName(List<String> movieNameList){
        Map<String, Double> map = new LinkedHashMap<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            for (int i = 0; i < movieNameList.size(); i++) {
                try{
                    conn = getConnection("MovieCatalog");
                    String query = "SELECT averageMark FROM MovieMark WHERE movieName = ? ORDER BY id DESC LIMIT 1";
                    ps = conn.prepareStatement(query);
                    ps.setString(1, movieNameList.get(i));
                    rs = ps.executeQuery();

                    while (rs.next()){
                        double averageMark = rs.getDouble("averageMark");
                        map.put(movieNameList.get(i), averageMark);
                    }
                } finally {
                    try{
                        if(conn != null){
                            conn.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(ps != null){
                            ps.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        if(rs != null){
                            rs.close();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }


        } catch (Exception e){
            e.printStackTrace();
        }
//        map.entrySet().stream()
//                .sorted(Map.Entry.<String, Double>comparingByValue().reversed());

        return map;
    }

    public List<String> getMovieNameFromMovieMarkTable(String userName){
        List<String> list = new ArrayList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("MovieCatalog");
                String query = "SELECT movieName FROM MovieMark where userName = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, userName);
                rs = ps.executeQuery();

                while (rs.next()){
                    String movieName = rs.getString("movieName");
                    list.add(movieName);
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public List<Integer> getMoviesAndTagsId(){
        List<Integer> list = new LinkedList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("MovieCatalog");
                String query = "SELECT id FROM MoviesAndTags";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    int id = rs.getInt("id");
                    list.add(id);
                }
            }finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public List<Integer> checkMovieMarkId(){
        List<Integer> list = new LinkedList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("MovieCatalog");
                String query = "SELECT id FROM MovieMark";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next()){
                    int id = rs.getInt("id");
                    list.add(id);
                }
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public String checkUser(String login, String password) {
        try {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                conn = getConnection("MovieCatalog");
                String query = "SELECT * FROM authorization WHERE login = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, login);
                rs = ps.executeQuery();

                if (rs.next()) {
                    if(password.equals(rs.getString("password"))){
                        return  "AUTHORIZATION IS OK";
                    }
                    else{
                        return  "INCORRECT PASSWORD";
                    }
                }
                else{
                    return "NEW REGISTRATION";
                }
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public List<Integer> checkUserId(){
        List<Integer> list = new LinkedList<>();

        try{
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try{
                conn = getConnection("MovieCatalog");
                String query = "SELECT id FROM authorization";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();
            } finally {
                try{
                    if(conn != null){
                        conn.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(ps != null){
                        ps.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(rs != null){
                        rs.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
