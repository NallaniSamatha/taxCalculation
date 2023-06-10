package com.samatha.taxcalculation.controller;

import com.samatha.taxcalculation.dto.Employee;
import com.samatha.taxcalculation.dto.TaxCalculationResult;
import com.samatha.taxcalculation.repository.EmployeeRepository;
import com.samatha.taxcalculation.service.TaxCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private TaxCalculationService taxCalculationService;
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(TaxCalculationService taxCalculationService, EmployeeRepository employeeRepository) {
        this.taxCalculationService = taxCalculationService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<String> storeEmployeeDetails(@Valid @RequestBody Employee employee) {
        employeeRepository.save(employee);
        return ResponseEntity.ok("Employee details stored successfully");
    }

    @GetMapping("/{employeeId}/tax-deduction")
    public ResponseEntity<TaxCalculationResult> getTaxDeduction(@PathVariable String employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmployeeId(employeeId);

        if (employeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Employee employee = employeeOptional.get();
        TaxCalculationResult taxCalculationResult = taxCalculationService.calculateTax(employee);
        return ResponseEntity.ok(taxCalculationResult);
    }
}
