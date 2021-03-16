package com.pmc.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.pmc.user.model.User;

public interface UserRepository extends CrudRepository<User, Long>
{
}
