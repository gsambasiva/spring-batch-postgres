package com.example.batch;

import org.springframework.batch.item.ItemProcessor;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {
    @Override
    public Employee process(Employee employee) {
        employee.setName(employee.getName().toUpperCase());
        return employee;
    }
}
