package com.example.webrise.service;

import com.example.webrise.dto.SubscriptionDto;
import com.example.webrise.model.Subscription;
import com.example.webrise.model.User;
import com.example.webrise.model.enums.RoleEnum;
import com.example.webrise.repository.SubscriptionRepository;
import com.example.webrise.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class SubscriptionServiceTest {
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        subscriptionRepository.deleteAll();
        userRepository.deleteAll();
        user = new User();
        user.setFirstName("Test");
        user.setMiddleName("User");
        user.setLastName("One");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(RoleEnum.USER);
        user = userService.saveUser(user);
    }

    @Test
    void testAddSubscription() {
        SubscriptionDto dto = new SubscriptionDto(null, "YouTube Premium");
        subscriptionService.addSubscription(user.getId(), dto);

        List<SubscriptionDto> subscriptions = subscriptionService.getSubscriptions(user.getId());
        assertEquals(1, subscriptions.size());
        assertEquals("YouTube Premium", subscriptions.get(0).serviceName());
    }

    @Test
    void testGetSubscriptions() {
        subscriptionService.addSubscription(user.getId(), new SubscriptionDto(null, "Netflix"));
        subscriptionService.addSubscription(user.getId(), new SubscriptionDto(null, "VK Музыка"));
        List<SubscriptionDto> subscriptions = subscriptionService.getSubscriptions(user.getId());
        assertEquals(2, subscriptions.size());
    }

    @Test
    void testDeleteSubscription() {
        subscriptionService.addSubscription(user.getId(), new SubscriptionDto(null, "Яндекс.Плюс"));
        List<Subscription> subs = subscriptionRepository.findByUserId(user.getId());
        assertEquals(1, subs.size());

        subscriptionService.deleteSubscription(user.getId(), subs.get(0).getId());
        List<Subscription> remaining = subscriptionRepository.findByUserId(user.getId());
        assertTrue(remaining.isEmpty());
    }

    @Test
    void testDeleteSubscription_NotOwned() {
        User otherUser = new User();
        otherUser.setFirstName("Other");
        otherUser.setMiddleName("User");
        otherUser.setLastName("Test");
        otherUser.setEmail("other@example.com");
        otherUser.setPassword("password");
        otherUser.setRole(RoleEnum.USER);
        otherUser = userService.saveUser(otherUser);

        subscriptionService.addSubscription(user.getId(), new SubscriptionDto(null, "Netflix"));
        Subscription subscription = subscriptionRepository.findByUserId(user.getId()).get(0);

        Integer subId = subscription.getId();
        Integer otherUserId = otherUser.getId();

        assertThatThrownBy(() -> subscriptionService.deleteSubscription(otherUserId, subId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testAddSubscriptionToNonExistingUser() {
        assertThatThrownBy(() ->
                subscriptionService.addSubscription(user.getId() + 1, new SubscriptionDto(null, "Spotify"))
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testGetTopSubscriptions() {
        User user1 = user;
        User user2 = new User();
        user2.setFirstName("Second");
        user2.setMiddleName("User");
        user2.setLastName("Two");
        user2.setEmail("second@example.com");
        user2.setPassword("password");
        user2.setRole(RoleEnum.USER);
        user2 = userService.saveUser(user2);

        User user3 = new User();
        user3.setFirstName("Third");
        user3.setMiddleName("User");
        user3.setLastName("Three");
        user3.setEmail("third@example.com");
        user3.setPassword("password");
        user3.setRole(RoleEnum.USER);
        user3 = userService.saveUser(user3);

        // Добавляем подписки:
        // YouTube - 2 подписки
        // Netflix - 2 подписки
        // Spotify - 1 подписка

        subscriptionService.addSubscription(user1.getId(), new SubscriptionDto(null, "YouTube"));
        subscriptionService.addSubscription(user2.getId(), new SubscriptionDto(null, "YouTube"));

        subscriptionService.addSubscription(user1.getId(), new SubscriptionDto(null, "Netflix"));
        subscriptionService.addSubscription(user3.getId(), new SubscriptionDto(null, "Netflix"));

        subscriptionService.addSubscription(user2.getId(), new SubscriptionDto(null, "Spotify"));

        List<String> top = subscriptionService.getTopSubscriptions(2);

        assertEquals(2, top.size());
        assertTrue(top.contains("YouTube"));
        assertTrue(top.contains("Netflix"));
        assertFalse(top.contains("Spotify"));
    }
}
