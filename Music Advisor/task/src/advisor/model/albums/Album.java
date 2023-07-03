package advisor.model.albums;

import java.util.List;

public class Album {
    private String id;
    private String name;
    private List<String> artists;
    private String link;

    public Album(String id, String name, List<String> artists, String link) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getArtists() {
        return artists;
    }

    public String getLink() {
        return link;
    }
}
