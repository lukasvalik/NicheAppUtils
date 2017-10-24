package eu.valics.library.FloatingIcon;

/**
 * Created by L on 10/24/2017.
 */

public class FloatingIconConfig {
    private int floatingIconResId;
    private String title;
    private String description;
    private String restartPhrase;

    public FloatingIconConfig(int floatingIconResId, String title, String description, String restartPhrase) {
        this.floatingIconResId = floatingIconResId;
        this.title = title;
        this.description = description;
        this.restartPhrase = restartPhrase;
    }

    public int getFloatingIconResId() {
        return floatingIconResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRestartPhrase() {
        return restartPhrase;
    }
}
