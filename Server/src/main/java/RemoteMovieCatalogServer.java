import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public class RemoteMovieCatalogServer implements MovieCatalog{
    private final static String JSON_FILE_NAME = "Server/moviecatalog.json";
    MySQLClass sql = new MySQLClass();
    List<Integer> listUsers;
    int countAuthorization;

    private static List<Movie> jsonToMovie(String fileName) throws FileNotFoundException {
        
        return Arrays.asList(new Gson().fromJson(new FileReader(fileName), Movie[].class));
    }

    private static Gson jsonToTags(String fileName) throws FileNotFoundException{
        Gson gson = new GsonBuilder().registerTypeAdapter(Tags.class, new Custom()).create();
        return gson;
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
