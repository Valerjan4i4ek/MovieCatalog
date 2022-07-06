import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface MovieCatalog extends Remote {
    String checkAuthorization(String login, String password) throws RemoteException;
    List<String> findTheMovie(String keyWord) throws RemoteException, FileNotFoundException;
    String addMovieMark(String userName, String movieName, int mark) throws RemoteException;
    String addMoviesAndTag(String userName, String movieName, String tag) throws RemoteException;
    List<String> showAllTags() throws RemoteException, FileNotFoundException;
    Map<String, Double> selectMovieByTag(List<String> tagListFromUser, String userName) throws RemoteException, FileNotFoundException;
    List<String> recommendation(String userName) throws RemoteException;
    Map<String, Double> topTenMovies() throws RemoteException, FileNotFoundException;

    List<Movie> getMovieAndTagName() throws RemoteException, FileNotFoundException;
}

