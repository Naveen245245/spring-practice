package com.s360.springjsp.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.s360.springjsp.model.crudUser.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}