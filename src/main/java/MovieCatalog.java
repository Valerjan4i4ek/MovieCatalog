import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MovieCatalog extends Remote {
    String checkAuthorization(String login, String password) throws RemoteException;
    List<Movie> getMovieAndTagName() throws RemoteException, FileNotFoundException;
}

