package com.example.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "department_employee_car",
            joinColumns = @JoinColumn(name = "department_id")
    )
    @MapKeyJoinColumn(name = "employee_id")
    private Map<Employee, Car> employeeCarMap = new HashMap<>();

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

    public Map<Employee, Car> getEmployeeCarMap() {
        return employeeCarMap;
    }

    public void assignCar(Employee employee, Car car) {
        employeeCarMap.put(employee, car);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employees=" + employees.size() +
                ", cars=" + employeeCarMap.size() +
                '}';
    }
}
