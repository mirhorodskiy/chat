package com.mirhorodskiy.chat.web.service;

import com.mirhorodskiy.chat.model.dto.DepartmentDto;
import com.mirhorodskiy.chat.model.entity.Department;
import com.mirhorodskiy.chat.model.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Отримати всі департаменти
    public List<DepartmentDto> getAllDepartments() {
        Iterable<Department> departmentsIterable = departmentRepository.findAll();
        List<Department> departmentsList = StreamSupport.stream(departmentsIterable.spliterator(), false)
                .toList();

        return departmentsList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    // Отримати департамент за ID
    public DepartmentDto getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return convertToDto(department);
    }

    // Створити новий департамент
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setName(departmentDto.getName());

        Department savedDepartment = departmentRepository.save(department);
        return convertToDto(savedDepartment);
    }

    // Перетворення Entity в DTO
    private DepartmentDto convertToDto(Department department) {
        return new DepartmentDto(
                department.getId(),
                department.getName()
        );
    }
}
