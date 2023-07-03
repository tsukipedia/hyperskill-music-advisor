package advisor.model.playlists;

public class Playlist {
    private String id;
    private String name;
    private String link;

    public Playlist(String id, String name, String link) {
        this.id = id;
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
