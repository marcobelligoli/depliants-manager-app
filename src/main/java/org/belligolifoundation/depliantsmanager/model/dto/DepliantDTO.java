package org.belligolifoundation.depliantsmanager.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepliantDTO {

    private Long id;
    private Integer number;
    private String description;
    private String eventName;
    private String notes;
    private String language;
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;
    private Long userId;
}
