package isep.psoft.aisafe.airports.dto;

import isep.psoft.aisafe.airports.domain.Photo;

public record PhotoDTO(String url, String caption) {

    public static PhotoDTO from(Photo photo) {
        return new PhotoDTO(photo.getUrl(), photo.getCaption());
    }
}
