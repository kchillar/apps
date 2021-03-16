package com.pmc.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmc.user.model.User;

@Service
public class UserService
{
  @Autowired
  private UserBO bo;

  public User addUser(User userRecord)
  {
    return bo.addUser(userRecord);    
  }

  public Optional<User> findById(long id)
  {
    Optional<User> record = bo.findById(id);
    return record;
  }

  public User update(User user)
  {
    User data = bo.update(user);
    return data;
  }
  
  public boolean deleteById(long id)
  {
    return bo.deleteById(id);
  }
  
  public List<User> getAll()
  {
    List<User> userRecords = bo.getAllUsers();
    return userRecords;
  }

}