package com.mirhorodskiy.chat.model.repository;

import com.mirhorodskiy.chat.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByDepartment(String department);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
