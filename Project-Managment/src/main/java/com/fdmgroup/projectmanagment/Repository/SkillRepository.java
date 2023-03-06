package com.fdmgroup.projectmanagment.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.fdmgroup.projectmanagment.Model.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>
{
	Skill findByTitle(String title);
	
	List<Skill> findAllByStatus(boolean status);
	
	Optional<Skill> findById(Long id);

	@Query("SELECT COUNT(*) FROM Skill ")
	Integer countSkills();
	
	@Query(value="SELECT skills_id FROM skill_project_roles WHERE project_roles_id = :projectRoleId", nativeQuery = true)
	List<Long> findSkillIdsByProjectRole(@Param("projectRoleId") String projectRoleId);
	
	@Query(value="SELECT * FROM skill WHERE title LIKE :title", nativeQuery = true)
	List<Skill> findSkillsByTitle(String title);
}
