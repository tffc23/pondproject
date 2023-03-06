package com.fdmgroup.projectmanagment.Service;

import com.fdmgroup.projectmanagment.Model.Role;

import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Gets all users from the database
	 * @return
	 */
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	/**
	 * Get user based on a given id
	 * @param id
	 * @return
	 */
	public User findById(Long id) {
		return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("No User with such id " + id));
	}

	/**
	 * Returns all users from the database who have the given role Trainee
	 * @return
	 */
	public List<User> getAllTrainees() {
		return this.userRepository.findByRole(Role.Trainee);
	}


	/**
	 * Get all users from database except the stated one
	 * @param id
	 * @return
	 */
	public List<User> getAllTraineesExcept(Long id) {

		List<User> trainees = this.userRepository.findByRole(Role.Trainee);

		for (User t : trainees) {
			if (t.getId() == id) {
				trainees.remove(t);
				break;
			}
		}
		return trainees;
	}

	/**
	 * Delete a user from the database based on a given id
	 * @param id
	 */
	public void delete(Long id) {
		this.userRepository.deleteById(id);
	}

	/**
	 * Save user to the database
	 * @param user
	 */
	public void save(User user) {
		this.userRepository.save(user);
	}

	/**
	 * Checks user login based on username and password
	 * @param username
	 * @param password
	 * @return
	 */
	public User checkLogin(String username, String password) {
		return this.userRepository.checkLogin(username, password);
	}

	/**
	 * Get the total number of users in the database
	 * @return
	 */
	public Integer countUsers() {
		return this.userRepository.countUsers();
	}

	/**
	 * Finds a list of users based on their last name
	 * @param lastName
	 * @return
	 */
	public List<User> findByLastName(String lastName) {
		return this.userRepository.findByLastName(lastName);
	}
}
