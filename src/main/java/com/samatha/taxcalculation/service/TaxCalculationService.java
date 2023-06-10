package com.samatha.taxcalculation.service;

import com.samatha.taxcalculation.dto.Employee;
import com.samatha.taxcalculation.dto.TaxCalculationResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaxCalculationService {
    private static final double TAX_SLAB_1 = 0.05;
    private static final double TAX_SLAB_2 = 0.10;
    private static final double TAX_SLAB_3 = 0.20;
    private static final double CESS_RATE = 0.02;
    private static final double TAX_FREE_LIMIT = 250000;
    private static final double TAX_SLAB_1_LIMIT = 500000;
    private static final double TAX_SLAB_2_LIMIT = 1000000;

    public TaxCalculationResult calculateTax(Employee employee) {
        double totalSalary = calculateTotalSalary(employee);
        double taxAmount = calculateTaxAmount(totalSalary);
        double cessAmount = calculateCessAmount(totalSalary);
        double yearlySalary = totalSalary * 12;

        return new TaxCalculationResult(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                yearlySalary,
                taxAmount,
                cessAmount
        );
    }

    private double calculateTotalSalary(Employee employee) {
        LocalDate now = LocalDate.now();
        LocalDate doj = employee.getDoj();

        int monthsOfWork = 12 * (now.getYear() - doj.getYear()) + now.getMonthValue() - doj.getMonthValue();
        double totalSalary = monthsOfWork * employee.getSalary();

        int daysInMonth = doj.lengthOfMonth();
        int daysOfJoining = daysInMonth - doj.getDayOfMonth() + 1;
        double lossOfPayPerDay = employee.getSalary() / daysInMonth;
        double lossOfPay = lossOfPayPerDay * daysOfJoining;

        return totalSalary - lossOfPay;
    }

    private double calculateTaxAmount(double totalSalary) {
        if (totalSalary <= TAX_FREE_LIMIT) {
            return 0;
        } else if (totalSalary <= TAX_SLAB_1_LIMIT) {
            return (totalSalary - TAX_FREE_LIMIT) * TAX_SLAB_1;
        } else if (totalSalary <= TAX_SLAB_2_LIMIT) {
            double taxSlab1Amount = (TAX_SLAB_1_LIMIT - TAX_FREE_LIMIT) * TAX_SLAB_1;
            double taxSlab2Amount = (totalSalary - TAX_SLAB_1_LIMIT) * TAX_SLAB_2;
            return taxSlab1Amount + taxSlab2Amount;
        } else {
            double taxSlab1Amount = (TAX_SLAB_1_LIMIT - TAX_FREE_LIMIT) * TAX_SLAB_1;
            double taxSlab2Amount = (TAX_SLAB_2_LIMIT - TAX_SLAB_1_LIMIT) * TAX_SLAB_2;
            double taxSlab3Amount = (totalSalary - TAX_SLAB_2_LIMIT) * TAX_SLAB_3;
            return taxSlab1Amount + taxSlab2Amount + taxSlab3Amount;
        }
    }

    private double calculateCessAmount(double totalSalary) {
        if (totalSalary > 2500000) {
            double cessableAmount = totalSalary - 2500000;
            return cessableAmount * CESS_RATE;
        }
        return 0;
    }
}
