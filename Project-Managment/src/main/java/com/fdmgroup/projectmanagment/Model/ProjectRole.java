package com.fdmgroup.projectmanagment.Model;

import java.util.List;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
public class ProjectRole 
{
	@Id
	@SequenceGenerator(name="project_role_seq", initialValue=1)
	@GeneratedValue
	private Long id;
	
	private String title;
	
	@Column(length=10000)
	private String description;
	
	@ManyToOne
	@JoinColumn(name="FK_PROJECT_ID")
	private Project project;
	
	@ManyToOne
	@JoinColumn(name="FK_USER_ID")
	private User participant;
	
	@ManyToMany(mappedBy = "projectRoles")
	private List<Skill> skills;
	
	@OneToMany
	private Set<User> applicants;
	
	public ProjectRole(Project project, String title, String description) {
		super();
		this.project = project;
		this.title = title;
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectRole other = (ProjectRole) obj;
		return Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(title, other.title);
	}
	
	public void addApplicants(User user) {
		applicants.add(user);
	}
	
}
