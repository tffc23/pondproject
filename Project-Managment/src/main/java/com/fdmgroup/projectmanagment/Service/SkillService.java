package com.fdmgroup.projectmanagment.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Repository.SkillRepository;

@Service
public class SkillService 
{
	private final SkillRepository skillRepository;

	public SkillService(SkillRepository skillRepository) {
		super();
		this.skillRepository = skillRepository;
	}

	public List<Skill> getAllSkills(){
		return this.skillRepository.findAll();
	}
	
	public List<Skill> getAllApprovedSkills()
	{
		return this.skillRepository.findAllByStatus(true);
	}
	
	public List<Skill> getAllUnapprovedSkills()
	{
		return this.skillRepository.findAllByStatus(false);
	}
	
	public Skill getSkillByTitle(String title)
	{
        return this.skillRepository.findByTitle(title) ;
	}
	
	public Skill getSkillById(Long id)
	{
        return this.skillRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No Skill with such id " + id) ) ;
	}
	
	public void delete(Long id)
	{
		this.skillRepository.deleteById(id);
	}
	
	public void save(Skill skill)
	{
		this.skillRepository.save(skill);
	}
	
	public Integer countSkills() {
		return this.skillRepository.countSkills();
	}
	
	public List<Skill> getAllSkillsByProjectRole(Long projectRoleId)
	{
		List<Skill> skills = new ArrayList<Skill>();
		
		for (Long id: skillRepository.findSkillIdsByProjectRole(projectRoleId + ""))
		{
			skills.add(skillRepository.findById(id).get());
		}
		return skills;
	}
}
