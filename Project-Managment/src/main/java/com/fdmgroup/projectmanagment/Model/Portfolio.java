package com.fdmgroup.projectmanagment.Model;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Portfolio {

	@Id
	@SequenceGenerator(name = "portfolio_seq", initialValue = 1)
	@GeneratedValue
	private Long id;

	private String title;

	private String description;

	private LocalDate createdDate;

	@ManyToOne
	private User creator;

	@ManyToMany
	private List<Skill> skills;

	@ManyToMany
	private List<Project> projects;

	public Portfolio(User creator, String title, String description) {
		super();
		this.creator = creator;
		this.createdDate = LocalDate.now();
		this.title = title;
		this.description = description;
	}

	public Portfolio(User creator) {
		super();
		this.creator = creator;
	}

	public Portfolio(String title) {
		super();
		this.title = title;
	}

}
