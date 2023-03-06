package com.fdmgroup.projectmanagment.Repository;

import com.fdmgroup.projectmanagment.Model.Role;
import com.fdmgroup.projectmanagment.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password AND u.isEnabled = true")
    User checkLogin(String username, String password);

    Optional<User> findById(Long id);

    @Query("SELECT COUNT(*) FROM User")
    Integer countUsers();
    
    
    List<User> findByLastName(String lastName) ;
    
    List<User> findByRole(Role role);
}
