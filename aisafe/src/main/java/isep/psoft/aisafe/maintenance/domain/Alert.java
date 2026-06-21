package isep.psoft.aisafe.maintenance.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private boolean isRead;

    protected Alert() {}

    public Alert(String message) {
        if (message == null || message.trim().isEmpty()) throw new IllegalArgumentException("Message cannot be empty.");
        this.message = message;
        this.createdAt = LocalDate.now();
        this.isRead = false;
    }

    public Long getId() { return id; }
    public String getMessage() { return message; }
    public LocalDate getCreatedAt() { return createdAt; }
    public boolean isRead() { return isRead; }
    public void markAsRead() { this.isRead = true; }
}