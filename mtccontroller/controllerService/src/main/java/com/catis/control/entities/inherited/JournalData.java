package com.catis.control.entities.inherited;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.catis.control.entities.Organisation;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public class JournalData {

	@Column(name = "created_date", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;
 
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    
    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    @Column(columnDefinition = "bit default 1")
    private boolean activeStatus;


    @ManyToOne
    private Organisation organisation;
}
