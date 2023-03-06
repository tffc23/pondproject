package com.fdmgroup.projectmanagment.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fdmgroup.projectmanagment.Model.Notification;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.NotificationRepository;

@Service
public class NotificationService {

	private NotificationRepository notificationRepo;

	public NotificationService(NotificationRepository notificationRepo) {
		super();
		this.notificationRepo = notificationRepo;
	}

	public void save(Notification notification) {
		this.notificationRepo.save(notification);
	}

	public List<Notification> findByUser(User user) {

		List<Notification> notifications = notificationRepo.findByOwner(user);

		return notifications;

	}
	
	public int countNotifactionsByUser(User user) {
		return notificationRepo.findByOwner(user).size();
	}

	public Notification findById(Long id) {

		return notificationRepo.findById(id).orElse(null);

	}

	public void deleteById(Long id) {

		notificationRepo.deleteById(id);
	}

}