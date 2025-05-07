package com.example.webrise.service;

import com.example.webrise.dto.SubscriptionDto;
import com.example.webrise.model.Subscription;
import com.example.webrise.model.User;
import com.example.webrise.repository.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserService userService;
    private final SubscriptionRepository subscriptionRepository;

    public List<Subscription> findSubscriptionByUserId(Integer userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    public Subscription findSubscriptionById(Integer subId) {
        return subscriptionRepository.findById(subId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
    }

    public void addSubscription(Integer userId, SubscriptionDto dto) {
        User user = userService.findUserById(userId);
        Subscription subscription = new Subscription();
        subscription.setServiceName(dto.serviceName());
        subscription.setUser(user);
        subscriptionRepository.save(subscription);
        log.info("Added subscription {} to user {}", dto.serviceName(), userId);
    }

    public List<SubscriptionDto> getSubscriptions(Integer userId) {
        List<Subscription> subscriptions = findSubscriptionByUserId(userId);
        return subscriptions.stream()
                .map(sub -> new SubscriptionDto(sub.getId(), sub.getServiceName()))
                .toList();
    }

    public void deleteSubscription(Integer userId, Integer subId) {
        Subscription sub = findSubscriptionById(subId);
        if (!sub.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Subscription does not belong to user");
        }
        subscriptionRepository.delete(sub);
        log.info("Deleted subscription {} from user {}", subId, userId);
    }

    public List<String> getTopSubscriptions(Integer countTop) {
        List<Object[]> results = subscriptionRepository.findTopSubscriptions(PageRequest.of(0, countTop));
        return results.stream()
                .map(row -> (String) row[0])
                .toList();
    }
}
