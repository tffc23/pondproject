package com.fdmgroup.projectmanagment.Model;

import java.util.List;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Skill {

	@Id
	@SequenceGenerator(name="skill_seq", initialValue=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	@ManyToMany
	private List<ProjectRole> projectRoles;
	
	@OneToMany(mappedBy = "skill")
	private List<TraineeSkill> traineeSkills;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private boolean status = false;

	public Skill(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}	
}