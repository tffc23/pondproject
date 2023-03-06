package com.fdmgroup.projectmanagment.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.NotificationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SearchBarController {
	private final NotificationService notificationService;

	@Autowired
	public SearchBarController(	NotificationService notificationService) {
		super();
		this.notificationService = notificationService;
	}

	public boolean renderUser(HttpSession session, Model model) {

		if (session.getAttribute("user") == null) {
			return false;
		}

		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		model.addAttribute("hasNotif", notificationService.countNotifactionsByUser(user));

		return true;
	}

	@GetMapping("/search")
	public String getSearchPage(HttpSession session, Model model) {

		if (!renderUser(session, model)) {
			return "redirect:/login";
		}

		// when user hasn't searched for anything => true; otherwise false
		model.addAttribute("hasQuery", false);

		// when search for trainee
		model.addAttribute("trainee", true);

		return "Search/search";
	}


	@RequestMapping(value = "/searchSomething", method = RequestMethod.GET)
	public String searchTraineesProjectsBySkill(@RequestParam("inline-name") String searchType,
			@RequestParam("field-name") String skillName, Model model) {

		

		if (searchType.equals("option 1")) { // OPTION 1 = SEARCH TRAINEES BY SKILL

			String url = "redirect:/searchTrainee/" + skillName;
			
			return url;

		} else if (searchType.equals("option 2")) { // OPTION 2 = SEARCH PROJECTS BY SKILL

			String url = "redirect:/searchProject/" + skillName;
			
			return url;

		} else {
			return "redirect:/search";
		}
	}

}
