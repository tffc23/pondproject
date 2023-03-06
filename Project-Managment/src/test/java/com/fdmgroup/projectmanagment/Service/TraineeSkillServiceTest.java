package com.fdmgroup.projectmanagment.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.projectmanagment.Model.Region;
import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.SkillLevel;
import com.fdmgroup.projectmanagment.Model.TraineeSkill;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.TraineeSkillRepository;

@ExtendWith(MockitoExtension.class)
public class TraineeSkillServiceTest 
{
	
	private TraineeSkillService traineeSkillService;
	
	@Mock
	TraineeSkillRepository mockTraineeSkillRepo;
	
	private List<Skill> skills;
	
	private List<User> trainees;
	
	@BeforeEach
	public void set_up()
	{
		traineeSkillService = new TraineeSkillService(mockTraineeSkillRepo);
		
		User tiffany = new User("Tiffany", "C", "tiff", "tiff@email.com", "password", Region.AU, Role.Trainee, true);
		User lisa = new User("Lisa", "S", "lisa", "lisa@gmail.com", "ilovedogs", Region.AU, Role.Trainee, true);
		
		Skill python = new Skill("Python", "Programing Language");
		Skill java = new Skill("Java", "Programming Language");
		Skill italian = new Skill("Italian", "Language");
		
		skills = new ArrayList<Skill>();
		trainees = new ArrayList<User>();
		
		skills.add(python);
		skills.add(java);
		skills.add(italian);
		
		trainees.add(tiffany);
		trainees.add(lisa);
	}

	@Test
	public void test_save()
	{
		TraineeSkill tiffany_java = new TraineeSkill(trainees.get(0), skills.get(1), SkillLevel.Advanced);		
		traineeSkillService.save(tiffany_java);
		verify(mockTraineeSkillRepo).save(tiffany_java);
	}
	
	@Test
	public void test_getTraineeSkillById_when_the_traineeskill_exists()
	{
		TraineeSkill tiffany_java = new TraineeSkill(trainees.get(0), skills.get(1), SkillLevel.Advanced);		
		Optional<TraineeSkill> optionalSkill = Optional.ofNullable(tiffany_java);

		when(mockTraineeSkillRepo.findById(1L)).thenReturn(optionalSkill);
		
		TraineeSkill foundTraineeSkill = traineeSkillService.getTraineeSkillById(1L);

		assertNotNull(foundTraineeSkill);
		assertEquals(tiffany_java, foundTraineeSkill);
	}
	
	@Test
	public void test_getTraineeSkillById_when_the_traineeskill_does_not_exist()
	{
		when(mockTraineeSkillRepo.findById(1L)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(RuntimeException.class, () -> traineeSkillService.getTraineeSkillById(1L));
		assertEquals("No TraineeSkill with such id " + 1L, e.getMessage());
	}
	
	@Test
	public void test_delete()
	{
		TraineeSkill tiffany_java = new TraineeSkill(trainees.get(0), skills.get(1), SkillLevel.Advanced);		
		traineeSkillService.delete(tiffany_java);
		
		verify(mockTraineeSkillRepo).delete(tiffany_java);
	}
	
	@Test
	public void test_getAllTraineesWithSkill_return_correct_list_trainees()
	{
		List<TraineeSkill> javaSkills = new ArrayList<TraineeSkill>();
		javaSkills.add(new TraineeSkill(trainees.get(0), skills.get(1), SkillLevel.Advanced));
		javaSkills.add(new TraineeSkill(trainees.get(1), skills.get(1), SkillLevel.Experienced));
		
		when(mockTraineeSkillRepo.findBySkill(skills.get(1))).thenReturn(javaSkills);
		
		List<User> traineesKnowJava = traineeSkillService.getAllTraineesWithSkill(skills.get(1));
		
		assertNotNull(traineesKnowJava);
		assertEquals(traineesKnowJava.get(0), trainees.get(0));
		assertEquals(traineesKnowJava.get(1), trainees.get(1));		
	}
	
	@Test
	public void test_getAllTraineeSkillsWithTrainee()
	{
		List<TraineeSkill> userSkills = new ArrayList<TraineeSkill>();
		userSkills.add(new TraineeSkill(trainees.get(0), skills.get(0), SkillLevel.Advanced));
		userSkills.add(new TraineeSkill(trainees.get(0), skills.get(1), SkillLevel.Advanced));
		
		when(mockTraineeSkillRepo.findByTrainee(trainees.get(0))).thenReturn(userSkills);
		
		List<TraineeSkill> foundTraineeSkills = traineeSkillService.getAllTraineeSkillsWithTrainee(trainees.get(0));
		
		assertNotNull(foundTraineeSkills);
		assertEquals(foundTraineeSkills, userSkills);
		assertEquals(foundTraineeSkills.get(0), userSkills.get(0));
		assertEquals(foundTraineeSkills.get(1), userSkills.get(1));
		
	}
	
	@Test
	public void test_getAllSkillIdsByTrainee()
	{
		List<Long> skillIds = List.of(0L, 1L);
		User u = trainees.get(0);
		u.setId(1L);
		
		when(mockTraineeSkillRepo.findSkillIdsByTraineeId("1")).thenReturn(skillIds);
		
		List<Long> foundSkillIds = traineeSkillService.getAllSkillIdsByTrainee(u);
		
		assertNotNull(foundSkillIds);
		assertEquals(skillIds, foundSkillIds);
	}
	
	
	

}
