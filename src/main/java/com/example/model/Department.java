package com.example.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    public Department() {}

    public Department(String name) {
        this.name = name;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Employee> getEmployees() { return employees; }

    public void addEmployee(Employee e) {
        employees.add(e);
        e.setDepartment(this);
    }

    public void removeEmployee(Employee e) {
        employees.remove(e);
        e.setDepartment(null);
    }
}
