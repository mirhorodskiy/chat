package com.mirhorodskiy.chat.model.repository;

import com.mirhorodskiy.chat.model.dto.DepartmentDto;
import com.mirhorodskiy.chat.model.entity.Department;
import com.mirhorodskiy.chat.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
    Optional<Department> findDepartmentByName(String name);
    Department findByName(String name);

}

