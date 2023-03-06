package com.fdmgroup.projectmanagment.Model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
	
    @Id
	@SequenceGenerator(name="user_seq", initialValue=1)
	@GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Region region;
    
    private String description;

    @Column(nullable = false)
    private Role role;
    
    @OneToMany(mappedBy="owner")
    private List<Notification> notifications;
    
    @OneToMany(mappedBy="sender")
    private List<Notification> notificationsSent;
    
    @OneToMany(mappedBy = "creator")
    private List<Project> projects;
    
    @OneToMany(mappedBy = "participant")
    private List<ProjectRole> projectRoles;
    
    private Stream stream;
    
    @OneToMany(mappedBy = "trainee")
    private List<TraineeSkill> traineeSkills;
    
    @OneToMany(mappedBy = "creator")
    private Set<Portfolio> portfolios;

    private boolean isEnabled;

    public User(String firstName, String lastName, String username, String email, String password, Region region, Role role, boolean isEnabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.region = region;
        this.role = role;
        this.isEnabled = isEnabled;
    }

    public User(String firstName, String lastName, String username, String email, String password, Region region, Role role, Stream stream) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.region = region;
        this.role = role;
    }

	@Override
	public int hashCode() {
		return Objects.hash(description, email, firstName, id, isEnabled, lastName, password, region, role, stream,
				username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(description, other.description) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
				&& isEnabled == other.isEnabled && Objects.equals(lastName, other.lastName)
				&& Objects.equals(password, other.password) && region == other.region && role == other.role
				&& stream == other.stream && Objects.equals(username, other.username);
	}
}