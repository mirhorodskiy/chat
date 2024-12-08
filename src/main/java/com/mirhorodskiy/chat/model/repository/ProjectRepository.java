package com.mirhorodskiy.chat.model.repository;

import com.mirhorodskiy.chat.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
