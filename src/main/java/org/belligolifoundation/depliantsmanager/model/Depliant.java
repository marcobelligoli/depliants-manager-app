package org.belligolifoundation.depliantsmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Depliant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String eventName;

    @Column
    private String notes;

    @Column
    private String language;

    @Column(nullable = false, updatable = false)
    private LocalDateTime insertDate;

    @Column
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.insertDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
