package com.fdmgroup.projectmanagment.Service;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Model.Notification;
import com.fdmgroup.projectmanagment.Repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

	@Mock
	NotificationRepository mockNotifRepo;

	@Mock
	Notification mockNotif;

	@Mock
	User mockUser;

	@Mock
	List<Notification> mockNotifs;

	NotificationService service;

	@BeforeEach
	void setUp() throws Exception {
		service = new NotificationService(mockNotifRepo);
	}

	@Test
	void save() {

		service.save(mockNotif);

		verify(mockNotifRepo, times(1)).save(mockNotif);
	}

	@Test
	void findByUser() {

		when(mockNotifRepo.findByOwner(mockUser)).thenReturn(mockNotifs);

		assertEquals(mockNotifs, service.findByUser(mockUser));

		verify(mockNotifRepo, times(1)).findByOwner(mockUser);
	}

	@Test
	void countNotificationByUser() {

		when(mockNotifRepo.findByOwner(mockUser)).thenReturn(mockNotifs);
		when(mockNotifs.size()).thenReturn(2);

		assertEquals(2, service.countNotifactionsByUser(mockUser));

		verify(mockNotifRepo, times(1)).findByOwner(mockUser);
	}

	@Test
	void findById() {

		when(mockNotifRepo.findById((long) 1)).thenReturn(Optional.of(mockNotif));

		assertEquals(mockNotif, service.findById((long) 1));

		verify(mockNotifRepo, times(1)).findById((long) 1);
	}

	@Test
	void deleteById() {

		service.deleteById((long) 1);

		verify(mockNotifRepo, times(1)).deleteById((long) 1);
	}

}
