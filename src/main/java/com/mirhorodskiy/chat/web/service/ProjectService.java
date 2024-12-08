package com.mirhorodskiy.chat.web.service;

import com.mirhorodskiy.chat.model.dto.ProjectDto;
import com.mirhorodskiy.chat.model.entity.Department;
import com.mirhorodskiy.chat.model.entity.Project;
import com.mirhorodskiy.chat.model.entity.User;
import com.mirhorodskiy.chat.model.repository.DepartmentRepository;
import com.mirhorodskiy.chat.model.repository.ProjectRepository;
import com.mirhorodskiy.chat.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final DepartmentRepository departmentRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    // Створення проекту
    public ProjectDto createProject(ProjectDto projectDto) {
        User manager = userRepository.findById(projectDto.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        Department department = manager.getDepartment();
        if (department == null) {
            throw new IllegalArgumentException("Manager is not assigned to a department");
        }

        Project project = Project.builder()
                .name(projectDto.getName())
                .department(department) // Департамент із менеджера
                .manager(manager)
                .build();

        Project savedProject = projectRepository.save(project);
        return convertToDto(savedProject);
    }

    // Отримання всіх проектів
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Отримання проекту за ID
    public ProjectDto getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        return convertToDto(project);
    }

    // Перетворення Entity в DTO
    private ProjectDto convertToDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .departmentId(project.getDepartment().getId())
                .managerId(project.getManager().getId())
                .build();
    }
}
