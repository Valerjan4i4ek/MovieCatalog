import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class RemoteMovieCatalogServer implements MovieCatalog{
    private final static String JSON_FILE_NAME = "Server/moviecatalog.json";
    MySQLClass sql = new MySQLClass();
    List<Integer> listUsers;
    List<Integer> listMovieMark;
    List<Integer> listMoviesAndTag;
    int countAuthorization;
    int countMovieMark;
    int countMoviesAndTag;

    private static List<Movie> jsonToMovie(String fileName) throws FileNotFoundException {
        return Arrays.asList(new Gson().fromJson(new FileReader(fileName), Movie[].class));
    }

    @Override
    public List<String> findTheMovie(String keyWord) throws RemoteException, FileNotFoundException {
        List<Movie> list = getMovieAndTagName();
        List<String> listWithFindingMovies = new ArrayList<>();
        if(list != null && !list.isEmpty()){
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getName().contains(keyWord)){
                    listWithFindingMovies.add(list.get(i).getName());
                }
            }
        }
        return listWithFindingMovies;
    }

    @Override
    public String addMovieMark(String userName, String movieName, int mark) throws RemoteException {
        List<Integer> list = sql.getMovieMarks(movieName);
        incrementMovieMark();
        if(list != null && !list.isEmpty()){
            sql.addMovieMark(new MovieMark(countMovieMark, userName, movieName, mark, average((ArrayList<Integer>) list, mark)));
        }
        else{
            sql.addMovieMark(new MovieMark(countMovieMark, userName, movieName, mark, mark));
        }
        return "";
    }

    @Override
    public String addMoviesAndTag(String userName, String movieName, String tag) throws RemoteException {
        List<String> list = sql.getTagFromMoviesAndTags();
        int i = 0;
        incrementMoviesAndTag();
        if(list != null && !list.isEmpty()){
            if(list.contains(tag)){
                i = sql.getTagCountFromMoviesAndTags(tag);
                i++;
                sql.addMovieAndTags(new MoviesAndTags(countMoviesAndTag, userName, movieName, tag, i));
            }
            else{
                sql.addMovieAndTags(new MoviesAndTags(countMoviesAndTag, userName, movieName, tag, 1));
            }
        }else{
            sql.addMovieAndTags(new MoviesAndTags(countMoviesAndTag, userName, movieName, tag, 1));
        }

        return "";
    }

    @Override
    public List<String> showAllTags() throws RemoteException, FileNotFoundException {
        List<Movie> movieList = jsonToMovie(JSON_FILE_NAME);
        List<String> tagList = new ArrayList<>();
        if(movieList != null && !movieList.isEmpty()){
            for (int i = 0; i < movieList.size(); i++) {

                String[] array = movieList.get(i).getTags();
                for (int j = 0; j <= array.length-1; j++) {
//                    if(!tagList.contains(array[j])){
//                        tagList.add(array[j]);
//                    }
                    tagList.add(array[j]);
                }
            }
        }
        Set<String> set = new HashSet<>(tagList);
        tagList.clear();
        tagList.addAll(set);
        return tagList;
    }

    @Override
    public Map<String, Double> selectMovieByTag(List<String> tagListFromUser, String userName) throws RemoteException, FileNotFoundException {
        List<Movie> movieList = jsonToMovie(JSON_FILE_NAME);
        List<String> tagListFromJson = null;
        List<String> movieNameFromMovieMarkTable = sql.getMovieNameFromMovieMarkTable(userName);
        List<String> listWithMoviesByTags = new LinkedList<>();
        if(!movieList.isEmpty()){
            for (int i = 0; i < movieList.size(); i++){
                String[] array = movieList.get(i).getTags();
                tagListFromJson = new ArrayList<>();
                for (int j = 0; j <= array.length-1; j++){
                    tagListFromJson.add(array[j]);
                }
                if(tagListFromJson.containsAll(tagListFromUser)){
//                    movieNameFromMovieMarkTable = sql.getMovieNameFromMovieMarkTable(userName);
                    if(movieNameFromMovieMarkTable != null && !movieNameFromMovieMarkTable.isEmpty()){
                        if(!movieNameFromMovieMarkTable.contains(movieList.get(i).getName())){
                            listWithMoviesByTags.add(movieList.get(i).getName());
                        }
                    }
                    else{
                        listWithMoviesByTags.add(movieList.get(i).getName());
                    }
                }
            }
        }
        return getAverageMarkByMovieName(listWithMoviesByTags);
    }

    @Override
    public List<String> recommendation(String userName) throws RemoteException {
        List<String> movieByUserWithHighMark = sql.getMovieByUserWithHighMark(userName);
//        for (String s : movieByUserWithHighMark) {
//            System.out.println(s);
//        }
//        System.out.println();

        List<String> usersWithHighMarksToSimilarMovies = sql.getUsersWithHighMarksToSimilarMovies(movieByUserWithHighMark, userName);
//        for (String usersWithHighMarksToSimilarMovie : usersWithHighMarksToSimilarMovies) {
//            System.out.println(usersWithHighMarksToSimilarMovie);
//        }
//        System.out.println();

        List<String> lovelyTags = sql.getLovelyTags(movieByUserWithHighMark);
        List<String> lovelyTagsFinal = new LinkedList<>();
        int i = 0;
        for (String lovelyTag : lovelyTags) {
            if(i != 2){
                lovelyTagsFinal.add(lovelyTag);
                System.out.println(lovelyTag);
            }
            i++;
        }
        System.out.println();

//        List<String> lovelyTags = sql.getLovelyTags(tagsByMovieName);
//        System.out.println();

        List<String> topMoviesFromSimilarUsers = sql.getTopMoviesFromSimilarUsers(usersWithHighMarksToSimilarMovies);
//        for (String topMoviesFromSimilarUser : topMoviesFromSimilarUsers) {
//            System.out.println(topMoviesFromSimilarUser);
//        }
//        System.out.println();

//        List<String> moviesFromSimilarUserByLovelyTags = sql.getMoviesFromSimilarUserByLovelyTags(lovelyTags, topMoviesFromSimilarUsers);
//        for (String moviesFromSimilarUserByLovelyTag : moviesFromSimilarUserByLovelyTags) {
//            System.out.println(moviesFromSimilarUserByLovelyTag);
//        }

        return sql.getMoviesFromSimilarUserByLovelyTags(lovelyTagsFinal, topMoviesFromSimilarUsers);
    }

    @Override
    public Map<String, Double> topTenMovies() throws RemoteException, FileNotFoundException {
//        List<String> topTenLis = new LinkedList<>();
        List<Movie> jsonList = jsonToMovie(JSON_FILE_NAME);
        List<String> movieList = new ArrayList<>();
        for (Movie movie : jsonList) {
            movieList.add(movie.getName());
        }
        Map<String, Double> map = sql.getAverageMarkByMovieName(movieList).entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        for (String s : movieList) {
            if (!map.containsKey(s)) {
                map.put(s, 0.0);
            }
        }
//        if(!map.isEmpty()){
//
//            for(Map.Entry<String, Double> entry : map.entrySet()){
//                System.out.println(entry.getKey() + " " + entry.getValue());
//                topTenLis.add(entry.getKey());
//            }
//        }
        return map;
    }


    public Map<String, Double> getAverageMarkByMovieName(List<String> listWithMoviesByTags){
        List<String> averageMarkByMovieName = new LinkedList<>();
        Map<String, Double> map = sql.getAverageMarkByMovieName(listWithMoviesByTags).entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        for (int i = 0; i < listWithMoviesByTags.size(); i++) {
            if(!map.containsKey(listWithMoviesByTags.get(i))){
                map.put(listWithMoviesByTags.get(i), 0.0);
            }
        }
//        map.entrySet().stream()
//                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (a, b) -> a,
//                        LinkedHashMap::new
//                ));

//        if(!map.isEmpty()){
//
//            for(Map.Entry<String, Double> entry : map.entrySet()){
//                System.out.println(entry.getKey() + " " + entry.getValue());
//                averageMarkByMovieName.add(entry.getKey());
//            }
//        }

        return map;
    }

    @Override
    public List<Movie> getMovieAndTagName() throws RemoteException, FileNotFoundException {
        return jsonToMovie(JSON_FILE_NAME);
    }

    @Override
    public String checkAuthorization(String login, String password) throws RemoteException {
        String s = sql.checkUser(login, password);
        if(s != null && !s.isEmpty()){
            if(s.equals("NEW REGISTRATION")){
                System.out.println(s);
                incrementAuthorization();
                sql.addAuthorization(new User(countAuthorization, login, password));
                return s;
            }
            else{

                System.out.println(s);
                return s;
            }
        }
        else {
            incrementAuthorization();
            sql.addAuthorization(new User(countAuthorization, login, password));
            System.out.println("new registration");
            return "new registration";
        }
    }

    static double average (ArrayList<Integer> list, int newMark){
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += (double)list.get(i);
        }
        return (sum + newMark)/(list.size()+1);
    }

    public void incrementMoviesAndTag(){
        listMoviesAndTag = sql.getMoviesAndTagsId();
        if(listMoviesAndTag != null && !listMoviesAndTag.isEmpty()){
            countMoviesAndTag = listMoviesAndTag.get(listMoviesAndTag.size()-1);
            countMoviesAndTag++;
        }
        else{
            countMoviesAndTag++;
        }
    }

    public void incrementMovieMark(){
        listMovieMark = sql.checkMovieMarkId();
        if(listMovieMark != null && !listMovieMark.isEmpty()){
            countMovieMark = listMovieMark.get(listMovieMark.size()-1);
            countMovieMark++;
        }
        else{
            countMovieMark++;
        }
    }

    public void incrementAuthorization(){
        listUsers = sql.checkUserId();
        if(listUsers != null && !listUsers.isEmpty()){
            countAuthorization = listUsers.get(listUsers.size()-1);
            countAuthorization++;
        }
        else{
            countAuthorization++;
        }
    }
}
