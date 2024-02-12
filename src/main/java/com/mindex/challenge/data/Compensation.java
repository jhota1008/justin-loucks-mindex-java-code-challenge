package com.mindex.challenge.data;


import java.math.BigDecimal;
import java.time.LocalDate;

public class Compensation {
    String employee;
    BigDecimal salary;
    LocalDate effectiveDate;

    public Compensation() {}

    public Compensation(String employee, BigDecimal salary, LocalDate effectiveDate) {
        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
