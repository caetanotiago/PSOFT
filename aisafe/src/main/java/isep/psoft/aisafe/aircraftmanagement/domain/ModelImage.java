package isep.psoft.aisafe.aircraftmanagement.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class ModelImage {

    private String imageData; 

    protected ModelImage() {}

    public ModelImage(String imageData) {
        if (imageData == null || imageData.trim().isEmpty()) {
            throw new IllegalArgumentException("Image data cannot be null or empty.");
        }
        this.imageData = imageData.trim();
    }

    public String getImageData() { return imageData; }
}