package com.catis.model.configuration;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass 
public class JournalData {

	
	@Column(name = "created_date", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;
 
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(columnDefinition = "bit default 1")
    private boolean activeStatus;
    
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}


	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}


	

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public String getActiveStatus() {
		if(this.activeStatus) {
			return "Activé";
		}
		else
			return "Désactivé";
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
    
    
}
