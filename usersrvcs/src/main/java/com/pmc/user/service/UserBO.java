package com.pmc.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmc.user.model.User;
import com.pmc.user.repository.UserRepository;

@Service
public class UserBO
{
  @Autowired
  private UserRepository repository;

  public User addUser(User userRecord)
  {
    return repository.save(userRecord);
  }

  public Optional<User> findById(long id)
  {
    return repository.findById(id);
  }

  public User update(User user)
  {
    return repository.save(user);    
  }
  
  public boolean deleteById(long id)
  {
    try
    {
      repository.deleteById(id);
      return true;
    }
    catch(Exception exp)
    {
      exp.printStackTrace();
      return false;
    }
  }
  
  public List<User> getAllUsers()
  {
    List<User> userRecords = new ArrayList<>();
    repository.findAll().forEach(userRecords::add);
    return userRecords;
  }
  
}
