package com.example.util;

import com.example.model.Employee;
import com.example.model.Department;
import com.example.repository.GenericRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomDataGenerator {

    private static final List<String> FIRST_NAMES = Arrays.asList(
            "Ali", "Ayşe", "Mehmet", "Elif", "Can", "Burcu", "Kemal", "Hülya"
    );

    private static final List<String> LAST_NAMES = Arrays.asList(
            "Yılmaz", "Kara", "Demir", "Aydın", "Öztürk", "Tekin", "Kurt", "Arslan"
    );

    private static final List<String> EMAIL_DOMAINS = Arrays.asList(
            "example.com", "mail.com", "company.com"
    );

    private static final List<String> DEPARTMENT_NAMES = Arrays.asList(
            "IT", "Sales", "Finance", "HR", "Management"
    );

    private static final Random RANDOM = new Random();

    /**
     * Random bir Employee oluşturur ve DB’ye kaydeder.
     * Departman mevcutsa bulur, yoksa yeni oluşturur.
     */
    public static Employee createAndSaveRandomEmployee(GenericRepository repo) {
        // Random bilgiler
        String firstName = FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size()));
        String lastName = LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size()));
        String email = (firstName + "." + lastName + "@" +
                EMAIL_DOMAINS.get(RANDOM.nextInt(EMAIL_DOMAINS.size()))).toLowerCase();
        double salary = 4000 + RANDOM.nextInt(4000);

        Employee employee = new Employee(firstName, lastName, email, salary);

        // Random departman seç
        String depName = DEPARTMENT_NAMES.get(RANDOM.nextInt(DEPARTMENT_NAMES.size()));

        // DB’den departmanı bul ya da yarat
        Department dep = repo.findOneBy(Department.class, "name", depName);
        if (dep == null) {
            dep = new Department(depName);
            repo.save(dep);
        }

        // Employee’yi departmana ekle
        dep.addEmployee(employee);

        // Kaydet (cascade ile employee de kaydolur)
        repo.save(dep);

        return employee;
    }
}
