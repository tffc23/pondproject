package com.fdmgroup.projectmanagment.Model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "project_tasks")
public class Task {
	
	@Id
    @GeneratedValue
    private Long id;
	
    private String title;
    private String brief;
    private Date dueDate;
    
    @ManyToOne
    private Project project;

	public Task(String title, String brief, Date dueDate) {
		super();
		this.title = title;
		this.brief = brief;
		this.dueDate = dueDate;
	}
    
}
