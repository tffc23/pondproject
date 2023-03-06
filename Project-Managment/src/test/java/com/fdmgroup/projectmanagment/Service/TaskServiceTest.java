package com.fdmgroup.projectmanagment.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.projectmanagment.Model.Task;
import com.fdmgroup.projectmanagment.Repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

	@Mock
	TaskRepository mockTaskRepository;
	
	@InjectMocks
	TaskService taskService;
	
	private Task task1;
	
	List<Task> tasks = Arrays.asList(task1);
	
	@SuppressWarnings("deprecation")
	@BeforeEach
	void setUp() {
		task1 = new Task("Sprint 3 Review", "Review sprint 3.", new Date(123, 0, 12));
	}
	
	@Test
	void test_findTaskById() {
		
		Task foundTask;
		
		when(mockTaskRepository.findTaskById(1L)).thenReturn(task1);
		
		foundTask = taskService.findTaskById(1L);
		
		assertEquals(foundTask, task1);
		verify(mockTaskRepository, times(1)).findTaskById(1L);
	}
	
	@Test
	void test_findAllTasks() {
		
		List<Task> foundTasks;
		
		when(mockTaskRepository.findAll()).thenReturn(tasks);
		
		foundTasks = taskService.findAllTasks();
		
		assertEquals(foundTasks, tasks);
		verify(mockTaskRepository, times(1)).findAll();
	}
	
	@Test
	void test_save() {
		
		taskService.save(task1);
		
		verify(mockTaskRepository, times(1)).save(task1);
	}
	
	@Test
	void test_delete() {
		
		taskService.deleteTask(task1);
		
		verify(mockTaskRepository, times(1)).delete(task1);
	}
}
