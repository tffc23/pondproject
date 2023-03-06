package com.fdmgroup.projectmanagment.Controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import static org.mockito.Mockito.times;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.fdmgroup.projectmanagment.Model.User;
import com.fdmgroup.projectmanagment.Model.Notification;
import com.fdmgroup.projectmanagment.Service.NotificationService;
import com.fdmgroup.projectmanagment.Service.UserService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

	@Mock
	NotificationService mockNotifService;

	@Mock
	UserService mockUserService;

	@Mock
	HttpSession mockSession;

	@Mock
	Model mockModel;

	@Mock
	User mockUser, mockUser2;

	@Mock
	Notification mockNotif;

	@Mock
	List<User> mockUsers;

	NotificationController controller;

	@BeforeEach
	void setUp() {
		controller = new NotificationController(mockNotifService, mockUserService);
	}

	@Test
	void getNotification_userNull() {

		when(mockSession.getAttribute("user")).thenReturn(null);

		assertEquals("redirect:/login", controller.getNotification(mockSession, mockModel));
	}

	@Test
	void getNotification_userLogin() {

		when(mockSession.getAttribute("user")).thenReturn(mockUser);

		assertEquals("Notification/notifications", controller.getNotification(mockSession, mockModel));

		verify(mockNotifService, times(1)).findByUser(mockUser);
	}

	@Test
	void getNotificationInfoPage_userNull() {

		when(mockSession.getAttribute("user")).thenReturn(null);

		assertEquals("redirect:/login", controller.getNotificationInfoPage(mockSession, mockModel, 1));
	}

	@Test
	void getNotificationInfoPage_userLogin() {

		when(mockSession.getAttribute("user")).thenReturn(mockUser);
		when(mockNotifService.findById((long) 1)).thenReturn(mockNotif);

		assertEquals("Notification/viewNotification", controller.getNotificationInfoPage(mockSession, mockModel, 1));
	}

	@Test
	void getSendNotificationToUser_userNull() {

		when(mockSession.getAttribute("user")).thenReturn(null);

		assertEquals("redirect:/login", controller.getSendNotificationToUser((long) 1, mockSession, mockModel));
	}

	@Test
	void getSendNotificationToUser_userLogin() {

		Notification notif = new Notification();
		notif.setSender(mockUser);
		notif.setOwner(mockUser2);

		when(mockSession.getAttribute("user")).thenReturn(mockUser);
		when(mockUserService.getAllTrainees()).thenReturn(mockUsers);
		when(mockUserService.findById((long) 1)).thenReturn(mockUser2);

		assertEquals("Notification/sendMessage",
				controller.getSendNotificationToUser((long) 1, mockSession, mockModel));
	}

	@Test
	void processNotification() {

		assertEquals("redirect:/", controller.processNotification(mockSession, mockNotif));
	}

	@Test
	void deleteNotification() {

		assertEquals("redirect:/notification", controller.deleteNotification(mockModel, 1));

		verify(mockNotifService, times(1)).deleteById((long) 1);
	}

}
