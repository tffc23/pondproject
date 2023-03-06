package com.fdmgroup.projectmanagment.Controller;

import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.SkillLevel;
import com.fdmgroup.projectmanagment.Model.TraineeSkill;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Service.PortfolioService;
import com.fdmgroup.projectmanagment.Service.SkillService;
import com.fdmgroup.projectmanagment.Service.TraineeSkillService;
import com.fdmgroup.projectmanagment.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SkillController {
	
    private SkillService skillService;
	private UserService userService;
	private TraineeSkillService traineeSkillService;
	private PortfolioService portfolioService;

	@Autowired
	public SkillController(SkillService skillService, UserService userService, TraineeSkillService traineeSkillService, PortfolioService portfolioService) {
		this.skillService = skillService;
		this.userService = userService;
		this.traineeSkillService = traineeSkillService;
		this.portfolioService = portfolioService;
	}


	@GetMapping("/skills")
	public String getProjects(Model model) {
		List<Skill> skills = skillService.getAllSkills();
		model.addAttribute("listOfSkills", skills);

		return "Dashboard/Skill/allSkills";
	}

	@GetMapping("/admin/createSkill")
	public String adminCreateSkill(Model model) {
		model.addAttribute("skill", new Skill());

		return "Dashboard/Skill/adminCreateSkill";
	}

	@PostMapping("/saveAdminCreateSkill")
	public String saveAdminProject(@ModelAttribute Skill skill) {
		this.skillService.save(skill);

		return "redirect:/skills";
	}

	@GetMapping("/skill/{id}/delete")
	public String deleteSkill(@PathVariable Long id) {
		this.skillService.delete(id);

		return "redirect:/skills";
	}

	@GetMapping("/updateAll/skill/{id}")
	public String editAlleUser(@PathVariable Long id, Model model, HttpSession httpSession) {
		Skill skill = this.skillService.getSkillById(id);
		model.addAttribute("skill", skill);

		return "Dashboard/Skill/editAllSkills";
	}

	@PostMapping("/saveAllSkill")
	public String saveAllSkill(@ModelAttribute Skill skill) {
		this.skillService.save(skill);

		return "redirect:/skills";
	}

	@GetMapping("/users/skills/{id}")
	public String getSingleUsersUser(@PathVariable Long id, Model model, HttpSession httpSession) {
		User user = userService.findById(id);
		List<User> users = userService.getAllUsers();

		model.addAttribute("user", user);
		model.addAttribute("listOfUsers", users);

		model.addAttribute("listOfTraineeSkills", traineeSkillService.getAllTraineeSkillsWithTrainee(user));
		model.addAttribute("portfolios", portfolioService.findByCreator(user));


		return "Dashboard/User/userSingle";
	}

	@GetMapping("/users/skills/{id}/addSkill")
	public String getAddSkill(HttpSession httpSession, Model model, @PathVariable String id) {
		User user = (User) httpSession.getAttribute("user");

		List<Skill> skillsTraineeNotHave = skillService.getAllApprovedSkills();
		List<Long> skillsTraineeHave = traineeSkillService.getAllSkillIdsByTrainee(user);

		for (Long skillId : skillsTraineeHave) {
			skillsTraineeNotHave.remove(skillService.getSkillById(skillId));
		}

		model.addAttribute("listOfApprovedSkills", skillsTraineeNotHave);
		httpSession.setAttribute("listOfApprovedSkills", skillsTraineeNotHave);
		model.addAttribute("skillLevels", SkillLevel.values());
		httpSession.setAttribute("skillLevels", SkillLevel.values());

		return "Dashboard/User/createSkill";
	}

	@GetMapping("/users/skills/delete/{tid}")
	public String deleteSkillAdmin(@PathVariable Long tid,HttpSession httpSession, Model model) {

		TraineeSkill skill = traineeSkillService.getTraineeSkillById(tid);
		traineeSkillService.delete(skill);

		return "redirect:/users";
	}
}