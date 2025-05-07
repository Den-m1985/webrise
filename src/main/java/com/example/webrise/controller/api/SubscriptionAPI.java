package com.example.webrise.controller.api;

import com.example.webrise.dto.SubscriptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Subscription Controller")
public interface SubscriptionAPI {

    @Operation(summary = "Subscription add")
    ResponseEntity<?> addSubscription(@PathVariable Integer userId, @RequestBody SubscriptionDto dto);

    @Operation(summary = "Subscription get")
    List<SubscriptionDto> getSubscriptions(@PathVariable Integer userId);

    @Operation(summary = "Subscription delete")
    void deleteSubscription(@PathVariable Integer userId, @PathVariable Integer subId);

    @Operation(summary = "Subscriptions top")
    List<String> getTopSubscriptions(Integer countTop);
}
