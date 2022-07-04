import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Movie implements Serializable {
    private String name;
    private String[] tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
