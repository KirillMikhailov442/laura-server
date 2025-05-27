package com.roomMicroservice.roomMicroservice.controllers;

import com.roomMicroservice.roomMicroservice.modes.Room;
import com.roomMicroservice.roomMicroservice.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms(){
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getOneRoom(@PathVariable long id){
        return new ResponseEntity<>(roomService.findRoomById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(){
        return new ResponseEntity<>(roomService.createRoom(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteRoom(@PathVariable long id){
        roomService.deleteRoomById(id);
        return new ResponseEntity<>("Room successfully deleted", HttpStatus.NO_CONTENT);
    }

}
