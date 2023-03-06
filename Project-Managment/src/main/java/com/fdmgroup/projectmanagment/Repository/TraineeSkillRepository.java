package com.fdmgroup.projectmanagment.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fdmgroup.projectmanagment.Model.Skill;
import com.fdmgroup.projectmanagment.Model.TraineeSkill;
import com.fdmgroup.projectmanagment.Model.User;

@Repository
public interface TraineeSkillRepository extends JpaRepository<TraineeSkill, Long>
{
	
	List<TraineeSkill> findBySkill(Skill skill);
	
	List<TraineeSkill> findByTrainee(User trainee);
	
	Optional<TraineeSkill> findById(Long id);
	
	@Query(value="SELECT skill_id FROM trainee_skill WHERE trainee_id = :traineeId", nativeQuery = true )
	List<Long> findSkillIdsByTraineeId(@Param("traineeId") String traineeId);
}
