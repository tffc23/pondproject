package com.fdmgroup.projectmanagment.Model;

import java.util.List;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {
	
	@Id
	@SequenceGenerator(name="project_seq", initialValue=1)
	@GeneratedValue
	private Long projectId ; 
	
	@ManyToOne
	@JoinTable(name="Project_Creator",
	joinColumns=
	@JoinColumn(name="FK_PROJECT_ID"),
	inverseJoinColumns=
	@JoinColumn(name="FK_USER_ID")
	)
	private User creator;
	
	@OneToMany(mappedBy = "project")
	private List<ProjectRole> projectRoles;
	
	private String title ;
	private ProjectType type ;
	
	@Column(length=10000)
	private String description ; 
	
	@OneToMany(mappedBy = "project")  // project files 
    private List<ProjectFile> projectFiles;
	
	@OneToMany(mappedBy = "project")  // tasks 
    private List<Task> tasks;

	public Project(User creator, String title, ProjectType type, String description) {
		super();
		this.creator = creator;
		this.title = title;
		this.type = type;
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(creator, description, projectId, title, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		return Objects.equals(creator, other.creator) && Objects.equals(description, other.description)
				&& Objects.equals(projectId, other.projectId) && Objects.equals(title, other.title)
				&& type == other.type;
	}
	
	
}
