package com.roomMicroservice.roomMicroservice.services;

import com.roomMicroservice.roomMicroservice.exceptions.NotFound;
import com.roomMicroservice.roomMicroservice.interfaces.RoomType;
import com.roomMicroservice.roomMicroservice.modes.Room;
import com.roomMicroservice.roomMicroservice.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms(){
        List<Room> rooms = roomRepository.findAll();
        if(rooms.isEmpty()) throw new NotFound("Rooms not found");
        return rooms;
    }

    public Room findRoomById(long id){
        return roomRepository.findById(id).orElseThrow(()-> new NotFound("Room not found by id"));
    }

    public Room createRoom(){
        Room newRoom = Room.builder().roomType(RoomType.PRIVATE).build();
        return roomRepository.save(newRoom);
    }

    public void deleteRoomById(long id){
        Room room = findRoomById(id);
        roomRepository.deleteById(id);
    }
}
