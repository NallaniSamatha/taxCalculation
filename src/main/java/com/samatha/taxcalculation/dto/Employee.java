package com.samatha.taxcalculation.dto;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Employee ID is mandatory")
    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @NotBlank(message = "First name is mandatory")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false)
    private String email;

    @NotEmpty(message = "Phone numbers are mandatory")
    @ElementCollection
    private List<@NotBlank(message = "Phone number is mandatory") String> phoneNumbers;

    @NotNull(message = "Date of joining is mandatory")
    @Column(name = "doj", nullable = false)
    private LocalDate doj;

    @NotNull(message = "Salary is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than zero")
    @Column(name = "salary", nullable = false)
    private Double salary;

}