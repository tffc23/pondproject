package com.fdmgroup.projectmanagment.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
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

import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest 
{
	
	@Mock
	SkillRepository mockSkillRepo;
	
	SkillService skillService;
	
	private Skill skill;
	
	@BeforeEach
	public void setUp()
	{
		skillService = new SkillService(mockSkillRepo);
		skill = new Skill("Italian", "Language");
	}
	
	@Test
	public void test_save()
	{
		skillService.save(skill);
		
		verify(mockSkillRepo, times(1)).save(skill);
	}
	
	@Test
	public void test_delete()
	{
		skillService.delete(1L);
		
		verify(mockSkillRepo, times(1)).deleteById(1L);
	}
	
	@Test
	public void test_getSkillById()
	{
		when(mockSkillRepo.findById(1L)).thenReturn(Optional.of(skill));
		
		Skill foundSkill = skillService.getSkillById(1L);
		
		assertEquals(skill, foundSkill);
	}
	
	@Test
	public void test_getSkillById_when_there_is_no_skill()
	{
		when(mockSkillRepo.findById(1L)).thenReturn(Optional.empty());
				
		Throwable e = assertThrows(RuntimeException.class, () -> skillService.getSkillById(1L));
		assertEquals("No Skill with such id " + 1L, e.getMessage());
	}
	
	@Test
	public void test_getSkillByTitle()
	{
		when(mockSkillRepo.findByTitle("Japanese")).thenReturn(skill);

		Skill foundSkill = skillService.getSkillByTitle("Japanese");

		assertEquals(skill, foundSkill);
	}
		
	@Test
	public void test_getAllApprovedSkills()
	{
		List<Skill> approvedSkills = new ArrayList<Skill>();
		when(mockSkillRepo.findAllByStatus(true)).thenReturn(approvedSkills);
		
		List<Skill> foundSkills = skillService.getAllApprovedSkills();
		
		assertEquals(approvedSkills, foundSkills);
	}
	
	@Test
	public void test_getAllUnapprovedSkills()
	{
		List<Skill> unapprovedSkills = new ArrayList<Skill>();
		when(mockSkillRepo.findAllByStatus(false)).thenReturn(unapprovedSkills);
		
		List<Skill> foundSkills = skillService.getAllUnapprovedSkills();
		
		assertEquals(unapprovedSkills, foundSkills);
	}
	
	@Test
	public void test_getAllSkills()
	{
		List<Skill> allSkills = new ArrayList<Skill>();
		when(mockSkillRepo.findAll()).thenReturn(allSkills);
				
		assertEquals(allSkills, skillService.getAllSkills());
	}
	
	@Test
	public void test_countSkills()
	{
		when(mockSkillRepo.countSkills()).thenReturn(5);
		assertEquals(5, skillService.countSkills());
	}
	
	@Test
	public void test_getAllSkillsByProjectRole()
	{
		List<Long> skillIds = List.of(1L, 2L);
		Skill japanese = new Skill("Japanese", "Language");
		japanese.setId(1L);
		Skill python = new Skill("Python", "Programming Language");
		japanese.setId(2L);
		
		when(mockSkillRepo.findSkillIdsByProjectRole("1")).thenReturn(skillIds);
		when(mockSkillRepo.findById(1L)).thenReturn(Optional.of(japanese));
		when(mockSkillRepo.findById(2L)).thenReturn(Optional.of(python));
		
		List<Skill> foundSkills = skillService.getAllSkillsByProjectRole(1L);
		
		assertNotNull(foundSkills);
		assertEquals(japanese, foundSkills.get(0));
		assertEquals(python, foundSkills.get(1));	
	}

}
