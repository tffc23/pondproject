package com.fdmgroup.projectmanagment.Controller;

import com.fdmgroup.projectmanagment.Model.*;
import com.fdmgroup.projectmanagment.Service.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class UserController {
	private final UserService userService;
	private final ProjectService projectService;
	private final SkillService skillService;
	private final NotificationService notificationService;


	@Autowired
	public UserController(UserService userService, ProjectService projectService, SkillService skillService,
			NotificationService notificationService) {
		super();
		this.userService = userService;
		this.projectService = projectService;
		this.skillService = skillService;
		this.notificationService = notificationService;
	}

	/**
	 * If no one is logged in the homepage is presented, id admin the admin dashboard is presented. If trainee, trainer or sales is logged in they are taken to the home page
	 * @param httpSession
	 * @param model
	 * @return
	 */

	@GetMapping("/")
    public String getHomePage(HttpSession httpSession, Model model) {
        User user;
        if ((user = (User) httpSession.getAttribute("user")) != null) {
            if (user.getRole() == Role.Admin) {
                return "redirect:/dashboard";
            } else {
                model.addAttribute("projects", projectService.getAllProjects());
                model.addAttribute("hasNotif", notificationService.countNotifactionsByUser(user));
                return "Home/home";
            }
        }
        return "Landing/index";
    }

	/**
	 *
	 * @param projectType
	 * @param model
	 * @return
	 */
	@GetMapping("/{projectType}")
	public String filterMockProjects(@PathVariable String projectType, Model model) {
		if (projectType.equals("mock"))
			model.addAttribute("projects", projectService.findByProjectType(ProjectType.Mock));
		if (projectType.equals("client"))
			model.addAttribute("projects", projectService.findByProjectType(ProjectType.Client));
		return "Home/home";
	}

	/**
	 * Get all the users in the database
	 * @param model
	 * @return
	 */
	@GetMapping("/users")
	public String getUsers(Model model) {
		List<User> users = userService.getAllUsers();
		model.addAttribute("listOfUsers", users);

		return "Dashboard/User/allUsers";
	}

	/**
	 * Deletes a user, based on their id
	 * @param id
	 * @return
	 */
	@GetMapping("/user/{id}/delete")
	public String deleteUser(@PathVariable Long id) {
		this.userService.delete(id);

		return "redirect:/users";
	}


	/**
	 * Deletes the profile of the currently logged in user and redirects to login page
	 * @param id
	 * @param httpSession
	 * @return
	 */
	@GetMapping("/userProfile/{id}/delete")
	public String deleteUserProfile(@PathVariable Long id, HttpSession httpSession) {
		this.userService.delete(id);
		httpSession.removeAttribute("user");

		return "redirect:/login";
	}


	/**
	 * Admin - Used to create a new user
	 * @param model
	 * @return
	 */
	@GetMapping("/create/user")
	public String getCreatedUser(Model model) {
		model.addAttribute("user", new User());

		return "Auth/register";
	}

	/**
	 * Admin - Used to create a new user
	 * @param user
	 * @return
	 */
	@PostMapping("/save")
	public String processCreateUser(@ModelAttribute User user) {
		this.userService.save(user);

		return "redirect:/login";
	}


	/**
	 * Admin - Allows the admin to update a specific user based on id
	 * @param id
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@GetMapping("/updateAll/user/{id}")
	public String editAlleUser(@PathVariable Long id, Model model, HttpSession httpSession) {
		User user = userService.findById(id);
		model.addAttribute("user", user);

		return "Dashboard/User/editAllUser";
	}

	/**
	 * Admin - POST Used to save changes to user
	 * @param user
	 * @return
	 */
	@PostMapping("/saveAllUser")
	public String saveAllUser(@ModelAttribute User user) {
		this.userService.save(user);

		return "redirect:/users";
	}

	/**
	 * Updates a user based on its id
	 * @param id
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@GetMapping("/update/user/{id}")
	public String editUser(@PathVariable Long id, Model model, HttpSession httpSession) {
		User user = userService.findById(id);
		if (user.isEnabled()) {
			user.setEnabled(true);
		}
		model.addAttribute("user", user);

		return "Dashboard/User/editUser";
	}

	/**
	 * Method used to update the currently logged in user. Based on their role they are then redirected to either admin profile page or trainee,trainer, sales profile page
	 *
	 * @param updatedUser
	 * @param httpSession
	 * @return
	 */
	@PostMapping("/saveUpdateUser")
	public String processUpdatedUser(@ModelAttribute User updatedUser, HttpSession httpSession, Model model) {
		User currentUser = (User) httpSession.getAttribute("user");

		currentUser.setFirstName(updatedUser.getFirstName());
		currentUser.setLastName(updatedUser.getLastName());
		currentUser.setUsername(updatedUser.getUsername());
		currentUser.setEmail(updatedUser.getEmail());
		currentUser.setPassword(updatedUser.getPassword());
		currentUser.setRegion(updatedUser.getRegion());
		currentUser.setRole(updatedUser.getRole());
		currentUser.setStream(updatedUser.getStream());

		if (currentUser.isEnabled()) {
			updatedUser.setEnabled(true);
		}

		httpSession.setAttribute("user", updatedUser);
		this.userService.save(updatedUser);

		if (currentUser.getRole() == Role.Admin) {
			return "redirect:/profile/" + currentUser.getId();
		}

		return "redirect:/profile/" + updatedUser.getId();
	}

	/**
	 * Displays the dashboard for ADMIN user, and displays total users, projects and skills currently in the database
	 * @param model
	 * @return
	 */
	@GetMapping("dashboard")
	public String getDashboard(Model model) {
		int countUsers = this.userService.countUsers();
		model.addAttribute("countUsers", countUsers);

		int countProjects = this.projectService.countProjects();
		model.addAttribute("countProjects", countProjects);

		int countSkills = this.skillService.countSkills();
		model.addAttribute("countSkills", countSkills);

		return "Dashboard/index";
	}


	/**
	 * Gets the login page
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@GetMapping("login")
	public String getLoginPage(Model model, HttpSession httpSession) {
		if (httpSession.getAttribute("user") != null) {
			return "Dashboard/User/profile";
		}
		model.addAttribute("user", new User());

		return "Auth/login";
	}

	/**
	 * Validates whether the user logging in is in the database and if their account is ENABLED if so they are logged in and sent to dashboard
	 * If login is unsuccessful an error message is show and they are promted back to the login page
	 * @param httpSession
	 * @param user
	 * @param model
	 * @return
	 */

	@RequestMapping("validateLogin")
	public String validateLogin(HttpSession httpSession, @ModelAttribute User user, Model model) {
		User loggedInUser = userService.checkLogin(user.getUsername(), user.getPassword());

		if (loggedInUser != null) {
			httpSession.setAttribute("user", loggedInUser);
			log.info("Login Successful, for user: " + loggedInUser.getUsername());

			return "redirect:/";
		}
		model.addAttribute("error", "INCORRECT_LOGIN_DETAILS");
		log.info("Failed Login");
		return "Auth/failedLogin";
	}

	/**
	 * Gets the register page
	 * @param model
	 * @return
	 */
	@GetMapping("register")
	public String getRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "Auth/register";
	}

	/**
	 * Ends the session for the current user and redirects them back to the login page
	 * @param httpSession
	 * @return
	 */
	@GetMapping("/logout")
	public String endingSessionToLogout(HttpSession httpSession) {
		log.info("Logout Successful");
		httpSession.removeAttribute("passenger");
		httpSession.invalidate();
		return "redirect:/login";
	}


	/**
	 * Allows admin to create a user
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/createUser")
	public String adminCreateUser(Model model) {
		model.addAttribute("user", new User());

		return "Dashboard/User/adminCreateU";
	}

	/**
	 * Allows admin to create a user - POST
	 * @param user
	 * @return
	 */
	@PostMapping("/saveAdminCreate")
	public String saveAdminUser(@ModelAttribute User user) {
		this.userService.save(user);

		return "redirect:/users";
	}
}
