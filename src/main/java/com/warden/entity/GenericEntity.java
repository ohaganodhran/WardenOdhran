package com.warden.entity;

import lombok.Data;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Date;

@Data
@MappedSuperclass
public class GenericEntity {

    private Date created;
    private Date modified;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @PreUpdate
    protected void onModify() {
        modified = new Date();
    }
}
