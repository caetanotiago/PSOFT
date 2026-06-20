package isep.psoft.aisafe.airports.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// US207: optional photo (URL + caption) illustrating the airport.
@Embeddable
public class Photo {

    @Column(name = "photo_url")
    private String url;

    @Column(name = "photo_caption")
    private String caption;

    protected Photo() {}

    public Photo(String url, String caption) {
        if (url == null || url.isBlank())
            throw new IllegalArgumentException("Photo URL is required");
        this.url = url;
        this.caption = caption;
    }

    public String getUrl() { return url; }
    public String getCaption() { return caption; }
}