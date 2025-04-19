package com.userMicroservice.UserMicroservice.exceptions;

import org.springframework.http.HttpStatus;
import java.util.Date;

public class Conflict extends BaseException{
  public Conflict(String message){
    super(message, HttpStatus.CONFLICT, new Date());
  }
}