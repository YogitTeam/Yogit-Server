package com.yogit.server.config.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    private String createdAt;

    @LastModifiedDate
    private String updatedAt;

    @Enumerated(EnumType.STRING)
    private BaseStatus status;

    protected void setStatus(BaseStatus status) {
        this.status = status;
    }

    @PrePersist
    public void onPrePersist(){
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updatedAt = this.createdAt;
        this.setStatus(BaseStatus.ACTIVE);
    }

    @PreUpdate
    public void onPreUpdate(){
        this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
