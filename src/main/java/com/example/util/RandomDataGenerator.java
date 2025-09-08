package com.example.util;

import com.example.model.Department;
import com.example.model.Employee;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Utility class to generate random Employee and Department data
 * for testing/demo purposes.
 */
public class RandomDataGenerator {

    private static final List<String> FIRST_NAMES = Arrays.asList(
            "Ali", "Ayşe", "Mehmet", "Elif", "Burcu", "Kemal", "Cem", "Hülya", "Hasan", "Can"
    );

    private static final List<String> LAST_NAMES = Arrays.asList(
            "Yılmaz", "Kara", "Demir", "Aydın", "Tekin", "Arslan", "Doğan", "Kurt", "Demirci", "Öztürk"
    );

    private static final List<String> DEPARTMENT_NAMES = Arrays.asList(
            "IT", "HR", "Finance", "Marketing", "Sales"
    );

    private static final Random RANDOM = new Random();

    /** Generates a random Department */
    public static Department randomDepartment() {
        String depName = DEPARTMENT_NAMES.get(RANDOM.nextInt(DEPARTMENT_NAMES.size()));
        return new Department(depName);
    }

    /** Generates a random Employee with random fields */
    public static Employee randomEmployee() {
        String first = FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size()));
        String last = LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size()));
        String email = (first + "." + last + RANDOM.nextInt(1000) + "@example.com").toLowerCase();
        double salary = 4000 + RANDOM.nextInt(5000); // between 4000 and 9000

        return new Employee(first, last, email, salary);
    }
}
