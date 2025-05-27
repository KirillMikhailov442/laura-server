package com.roomMicroservice.roomMicroservice.repositories;

import com.roomMicroservice.roomMicroservice.modes.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
