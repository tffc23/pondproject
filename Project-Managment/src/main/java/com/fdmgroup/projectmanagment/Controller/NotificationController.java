package com.fdmgroup.projectmanagment.Controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fdmgroup.projectmanagment.Model.Notification;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class NotificationController {

	private NotificationService notificationService;
	private UserService userService;

	public NotificationController(NotificationService notificationService, UserService userService) {
		super();
		this.notificationService = notificationService;
		this.userService = userService;
	}

	@GetMapping("/notification")
	public String getNotification(HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		User user = (User) session.getAttribute("user");
		List<Notification> notifications = notificationService.findByUser(user);
		model.addAttribute("notifications", notifications);

		return "Notification/notifications";
	}

	@GetMapping("/notification/view/{id}")
	public String getNotificationInfoPage(HttpSession session, Model model, @PathVariable long id) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		Notification notification = notificationService.findById(id);
		model.addAttribute("notification", notification);

		return "Notification/viewNotification";
	}

	@GetMapping("/notification/send/{id}")
	public String getSendNotificationToUser(@PathVariable Long id, HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		List<User> users = userService.getAllTrainees();

		User user = (User) session.getAttribute("user");
		User receiver = userService.findById(id);

		Notification notification = new Notification();
		notification.setSender(user);
		notification.setOwner(receiver);
		model.addAttribute("notification", notification);
		model.addAttribute("receiver", receiver);

		model.addAttribute("users", users);

		return "Notification/sendMessage";
	}

	@PostMapping("/saveNotification")
	public String processNotification(HttpSession session, @ModelAttribute Notification notification) {

		notificationService.save(notification);

		return "redirect:/";
	}

	@GetMapping("/notification/delete/{id}")
	public String deleteNotification(Model model, @PathVariable long id) {

		notificationService.deleteById(id);

		return "redirect:/notification";
	}

	private boolean renderUser(HttpSession session, Model model) {

		if (session.getAttribute("user") == null) {
			return false;
		}

		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("hasNotif", notificationService.countNotifactionsByUser(user));
		return true;
	}

}