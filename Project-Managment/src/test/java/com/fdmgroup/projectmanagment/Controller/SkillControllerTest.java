package com.fdmgroup.projectmanagment.Controller;

import static org.junit.jupiter.api.Assertions.*;


import com.fdmgroup.projectmanagment.Service.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.TraineeSkill;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {

	@Mock
	SkillService mockSkillService;
	@Mock
	UserService mockUserService;
	@Mock
	TraineeSkillService traineeSkillService;
	@Mock
	PortfolioService portfolioService;

	@Mock
	HttpSession mockSession;

	@Mock
	Model mockModel;

	@Mock
	User mockUser;

	@Mock
	List<Skill> mockSkills;

	@Mock
	List<User> mockUsers;

	@Mock
	Skill mockSkill;
	
	@Mock
	TraineeSkill mockTraineeSkill;

	SkillController controller;

	@BeforeEach
	void setUp() {
		controller = new SkillController(mockSkillService, mockUserService, traineeSkillService, portfolioService);
	}

	@Test
	void getProjects() {

		when(mockSkillService.getAllSkills()).thenReturn(mockSkills);

		assertEquals("Dashboard/Skill/allSkills", controller.getProjects(mockModel));

		verify(mockModel).addAttribute("listOfSkills", mockSkills);
	}

	@Test
	void adminCreateSkill() {

		assertEquals("Dashboard/Skill/adminCreateSkill", controller.adminCreateSkill(mockModel));

		verify(mockModel).addAttribute("skill", new Skill());
	}

	@Test
	void saveAdminProject() {

		assertEquals("redirect:/skills", controller.saveAdminProject(mockSkill));

		verify(mockSkillService).save(mockSkill);
	}

	@Test
	void deleteSkill() {

		assertEquals("redirect:/skills", controller.deleteSkill((long) 1));

		verify(mockSkillService).delete((long) 1);
	}

	@Test
	void editAlleUser() {

		when(mockSkillService.getSkillById((long) 1)).thenReturn(mockSkill);

		assertEquals("Dashboard/Skill/editAllSkills", controller.editAlleUser((long) 1, mockModel, mockSession));

		verify(mockModel).addAttribute("skill", mockSkill);
	}

	@Test
	void saveAllSkill() {

		assertEquals("redirect:/skills", controller.saveAllSkill(mockSkill));

		verify(mockSkillService).save(mockSkill);
	}

	@Test
	void getSingleUsersUser() {

		when(mockUserService.getAllUsers()).thenReturn(mockUsers);

		assertEquals("Dashboard/User/userSingle", controller.getSingleUsersUser(1L, mockModel, mockSession));

	}
	
	@Test
	void getAddSkill() {
		
		List<Long> skillIds = new ArrayList<>();
		skillIds.add(1L);
		
		when(mockSession.getAttribute("user")).thenReturn(mockUser);
		
		when(mockSkillService.getAllApprovedSkills()).thenReturn(mockSkills);
		when(traineeSkillService.getAllSkillIdsByTrainee(mockUser)).thenReturn(skillIds);
		
		assertEquals("Dashboard/User/createSkill", controller.getAddSkill(mockSession, mockModel, "1"));
	}
	
	@Test
	void deleteSkillAdmin() {
		
		when(traineeSkillService.getTraineeSkillById(1L)).thenReturn(mockTraineeSkill);
		
		assertEquals("redirect:/users", controller.deleteSkillAdmin(1L, mockSession, mockModel));
		
		verify(traineeSkillService).delete(mockTraineeSkill);
		
	}

}
