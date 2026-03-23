package com.chatapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ChatRoomFullException extends RuntimeException {

    public ChatRoomFullException(String message) {
        super(message);
    }

    public ChatRoomFullException(String roomName, int maxCapacity) {
        super(String.format("Chat room '%s' is full. Maximum capacity is %d.", roomName, maxCapacity));
    }
}
