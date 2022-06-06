import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

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
        user = new User(login, password);
        String result = movieCatalog.checkAuthorization(login, password);
        System.out.println(result);
        if(result.equals("INCORRECT PASSWORD")){
            authorization();
        }
        else{
            getMovieAndTagName();
        }

    }

    public static void getMovieAndTagName() throws IOException, NotBoundException, RemoteException {
        List<Movie> list = movieCatalog.getMovieAndTagName();

        for (int i = 0; i < list.size(); i++) {

            System.out.println((i+1) + " " + list.get(i).getName());

//            Tags[] array = list.get(i).getTags();
//            for (int j = 0; j < array.length; j++) {
//                System.out.println(array[i].toString());
//            }
        }
    }
}
