package com.roomMicroservice.roomMicroservice.repositories;

import com.roomMicroservice.roomMicroservice.modes.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
