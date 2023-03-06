package com.fdmgroup.projectmanagment.Model;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TraineeSkill 
{
	@Id
	@SequenceGenerator(name="trainee_skill_id", initialValue=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private User trainee;

	@ManyToOne
	private Skill skill;
	
	private SkillLevel skillLevel;

	public TraineeSkill(User trainee, Skill skill, SkillLevel skillLevel) {
		super();
		this.trainee = trainee;
		this.skill = skill;
		this.skillLevel = skillLevel;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, skillLevel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TraineeSkill other = (TraineeSkill) obj;
		return Objects.equals(id, other.id) && skillLevel == other.skillLevel;
	}
	
}