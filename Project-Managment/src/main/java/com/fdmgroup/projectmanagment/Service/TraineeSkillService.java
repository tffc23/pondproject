package com.fdmgroup.projectmanagment.Service;

import java.util.ArrayList;

import java.util.List;
import org.springframework.stereotype.Service;

import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.TraineeSkill;
import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.TraineeSkillRepository;

@Service
public class TraineeSkillService 
{

	private final TraineeSkillRepository traineeSkillRepository;
	
	public TraineeSkillService(TraineeSkillRepository traineeSkillRepository) {
		super();
		this.traineeSkillRepository = traineeSkillRepository;
	}
	
	public TraineeSkill getTraineeSkillById(Long id)
	{
        return this.traineeSkillRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No TraineeSkill with such id " + id)
        );
	}
	
	public void save(TraineeSkill traineeSkill)
	{
		this.traineeSkillRepository.save(traineeSkill);
	}
	
	public void delete(TraineeSkill traineeSkill)
	{
		this.traineeSkillRepository.delete(traineeSkill);
	}
	
	
	public List<User> getAllTraineesWithSkill(Skill skill)
	{
		List<User> users = new ArrayList<User>();
		
		for (TraineeSkill ts: traineeSkillRepository.findBySkill(skill))
		{
			users.add(ts.getTrainee());
		}
		
		return users;
	}
	
	public List<TraineeSkill> getAllTraineeSkillsWithTrainee(User trainee)
	{
		return this.traineeSkillRepository.findByTrainee(trainee);
	}
	
	public List<Long> getAllSkillIdsByTrainee(User trainee)
	{
		return this.traineeSkillRepository.findSkillIdsByTraineeId(trainee.getId().toString());
	}
}
