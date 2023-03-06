package com.fdmgroup.projectmanagment.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Notification {

	@Id
	@GeneratedValue
	private Long notif_id;

	@ManyToOne
	@JoinTable(name = "Notification_Receiver", 
	joinColumns = @JoinColumn(name = "FK_NOTIFICATION_ID"), 
	inverseJoinColumns = @JoinColumn(name = "FK_USER_ID"))
	private User owner;
	
	@ManyToOne
	@JoinTable(name = "Notification_Sender", 
	joinColumns = @JoinColumn(name = "FK_NOTIFICATION_ID"), 
	inverseJoinColumns = @JoinColumn(name = "FK_USER_ID"))
	private User sender;
	
	@Column(nullable = false)
	private String notif_title;

	@Column(nullable = false, length = 10000)
	private String notif_text;

	public Notification(String notif_title, String notif_text) {
		super();
		this.notif_title = notif_title;
		this.notif_text = notif_text;
	}

}
