package com.pmc.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pmc.user.model.User;
import com.pmc.user.service.UserService;

import java.util.List;
import java.util.Optional;

//@CrossOrigin is for configuring allowed origins.
//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/usersrvcs")
public class UserController
{
  @Autowired
  private UserService service;

  @RequestMapping(value = "/create-user", method = RequestMethod.POST)
  public  ResponseEntity<User> createUser(@RequestBody User userRecord)
  {
    User fromStore = service.addUser(userRecord);
    return new ResponseEntity<>(fromStore, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getTutorialById(@PathVariable("id") long id)
  {
    Optional<User> data = service.findById(id);

    if (data.isPresent())
    {
      return new ResponseEntity<>(data.get(), HttpStatus.OK);
    } else
    {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateTutorial(@PathVariable("id") long id, @RequestBody User requestData)
  {
    Optional<User> tutorialData = service.findById(id);

    if (tutorialData.isPresent())
    {
      User fromStore = tutorialData.get();
      fromStore.setFirstName(requestData.getFirstName());
      fromStore.setLastName(requestData.getLastName());
      fromStore.setEmail(requestData.getEmail());
      fromStore = service.update(fromStore);
      return new ResponseEntity<>(fromStore, HttpStatus.OK);
    } else
    {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id)
  {
    try
    {
      service.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e)
    {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping("/")
  public List<User> getAll()
  {
    return service.getAll();
  }
}
