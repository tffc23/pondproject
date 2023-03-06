package com.fdmgroup.projectmanagment.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fdmgroup.projectmanagment.Model.Project;
import com.fdmgroup.projectmanagment.Model.Task;
import com.fdmgroup.projectmanagment.Repository.TaskRepository;

@Service
public class TaskService {

	private TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		super();
		this.taskRepository = taskRepository;
	}

	public Task findTaskById(Long id) {
		return taskRepository.findTaskById(id);
	}

	public List<Task> findAllTasks() {
		return taskRepository.findAll();
	}

	public List<Task> findTasksByProject(Project project) {
		return taskRepository.findByProject(project);
	}

	public List<Task> findTasksByProjectId(Long id) {
		return taskRepository.findTasksByProjectId(id);
	}

	public void save(Task task) {
		this.taskRepository.save(task);
	}

	public void deleteTask(Task task) {
		this.taskRepository.delete(task);

	}
}
