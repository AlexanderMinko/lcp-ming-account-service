package com.lenovo.accountservice.consumer;

import static com.lenovo.utils.EventUtils.deserialize;

import com.lenovo.accountservice.entity.model.UserAuthenticatedEvent;
import com.lenovo.accountservice.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticatedConsumer {

  private final AccountService accountService;

  @KafkaListener(topics = {"lcp-ming-user-authenticated-event"})
  public void consumeUserAuthenticatedEvent(String event) {
    log.debug("Consumed event: {}", event);
    var userAuthenticatedEvent = deserialize(event, UserAuthenticatedEvent.class);
    accountService.activateAccount(userAuthenticatedEvent.getId(), userAuthenticatedEvent.getRealmName());
  }
}
