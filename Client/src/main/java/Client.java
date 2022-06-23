import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static final String UNIQUE_BINDING_NAME = "server.MovieCatalog";
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static User user;
    static Registry registry;
    static MovieCatalog movieCatalog;

    static {
        try {
            registry = LocateRegistry.getRegistry("127.0.0.1", 2732);
            movieCatalog = (MovieCatalog) registry.lookup(UNIQUE_BINDING_NAME);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, NotBoundException, RemoteException{
        authorization();
    }

    public static void authorization() throws IOException, NotBoundException, RemoteException{

        System.out.println("Enter you login and password");
        String login = reader.readLine();
        String password = reader.readLine();
        int option = 0;

        user = new User(login, password);
        String result = movieCatalog.checkAuthorization(login, password);
        System.out.println(result);
        if(result.equals("INCORRECT PASSWORD")){
            authorization();
        }
        else{

            System.out.println();
            System.out.println("Choose option:");
            System.out.println("1. Find Movie");
            System.out.println("2. Show all tags");
            System.out.println("3. Select Movie by tag");
            System.out.println("4. Recommendation");
            System.out.println("5. Top 10");
            option = Integer.parseInt(reader.readLine());
            if(option == 1){
                findTheMovie();
            }
            else if(option == 2){
                showAllTags();
            }
            else if(option == 3){
//                tag = reader.readLine();
                selectMovieByTag(user.getUserName());
            }
            else if(option == 4){
                recommendation(user.getUserName());
            }
            else if(option == 5){
                topTenMovies();
            }
        }

    }

    public static void topTenMovies() throws FileNotFoundException, RemoteException {
        List<String> list = movieCatalog.topTenMovies();
        int i = 0;
        for (String s : list) {
            if(i < 10){
                System.out.println(s);
                i++;
            }
            else{
                break;
            }
        }
    }

    public static void recommendation(String userName) throws IOException{
        List<String> list = movieCatalog.recommendation(userName);
        for (String s : list) {
            System.out.println(s);
        }
    }

    public static void selectMovieByTag(String userName) throws IOException {
        List<String> tagListFromUser = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose your tags. Press F to complete");

        while (scanner.hasNext()){
            String s = scanner.next();
            if(s.equals("F")){
                break;
            }
            else{
                tagListFromUser.add(s);
            }

        }
        List<String> listWithMoviesByTags = movieCatalog.selectMovieByTag(tagListFromUser, userName);

//        for (int i = listWithMoviesByTags.size()-1; i >=0 ; i--) {
//            System.out.println(listWithMoviesByTags.get(i));
//        }
        for (String listWithMoviesByTag : listWithMoviesByTags) {
            System.out.println(listWithMoviesByTag);
            for (String s : tagListFromUser) {
                movieCatalog.addMoviesAndTag(userName, listWithMoviesByTag, s);
            }
        }
    }

    public static void findTheMovie() throws IOException, NotBoundException, RemoteException{
        System.out.println("Find something:");
        String repeat = "";
        String askMark = "";
        int mark = 0;
        String s = movieCatalog.findTheMovie(reader.readLine());
        if (!s.isEmpty()){
            System.out.println(s);
            System.out.println("Ocinka? Y or N");
            askMark = reader.readLine();
            if(askMark.equals("Y")){
                System.out.println("from 1 to 5 mark");
                mark = Integer.parseInt(reader.readLine());
                if(mark >= 1 && mark <= 5){
                    addMovieMark(user.getUserName(), s, mark);
                }
                else{
                    System.out.println("from 1 to 5 mark");
                }
            }
            else if(askMark.equals("N")){
                System.exit(1);
            }
        }
        else{
            System.out.println("Takogo nema. Repeat? Y or N");
            repeat = reader.readLine();
            if(repeat.equals("Y")){
                findTheMovie();
            }
            else if(repeat.equals("N")){
                System.exit(1);
            }
        }
    }

    public static void addMovieMark(String userName, String movieName, int mark) throws IOException, NotBoundException, RemoteException{
        movieCatalog.addMovieMark(userName, movieName, mark);
    }

    public static void showAllTags() throws IOException, NotBoundException, RemoteException{
        List<String> list = movieCatalog.showAllTags();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    public static void getMovieAndTagName() throws IOException, NotBoundException, RemoteException {
        List<Movie> list = movieCatalog.getMovieAndTagName();
        if(list != null && !list.isEmpty()){
            for (int i = 0; i < list.size(); i++) {

                System.out.println((i+1) + " " + list.get(i).getName());
                System.out.println();

                System.out.println("tags:");
                String[] array = list.get(i).getTags();
                for (int j = 0; j <= array.length-1; j++) {
                    System.out.println(array[j]);
                }
            }
        }
    }
}
