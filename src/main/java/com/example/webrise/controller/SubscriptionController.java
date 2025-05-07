package com.example.webrise.controller;

import com.example.webrise.controller.api.SubscriptionAPI;
import com.example.webrise.dto.SubscriptionDto;
import com.example.webrise.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController implements SubscriptionAPI {
    private final SubscriptionService subscriptionService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> addSubscription(@PathVariable Integer userId, @RequestBody SubscriptionDto dto) {
        subscriptionService.addSubscription(userId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public List<SubscriptionDto> getSubscriptions(@PathVariable Integer userId) {
        return subscriptionService.getSubscriptions(userId);
    }

    @DeleteMapping("/{userId}/{subId}")
    public void deleteSubscription(@PathVariable Integer userId, @PathVariable Integer subId) {
        subscriptionService.deleteSubscription(userId, subId);
    }

    @GetMapping("/top/{countTop}")
    public List<String> getTopSubscriptions(@PathVariable Integer countTop) {
        return subscriptionService.getTopSubscriptions(countTop);
    }
}
