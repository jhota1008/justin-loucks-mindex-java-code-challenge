package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceTest {

    private String employeeUrl;
    private String reportingStructureEmployeeIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureEmployeeIdUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("Dave");
        testEmployee2.setLastName("Smith");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Developer");

        // Create checks
        Employee createdReportingEmployee= restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();

        //after creating the employee that reports to this one, we can set the direct report and create this employee
        testEmployee.setDirectReports(Collections.singletonList(createdReportingEmployee));
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertNotNull(createdReportingEmployee.getEmployeeId());

        // Read checks
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureEmployeeIdUrl,
                ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), reportingStructure.getEmployee().getEmployeeId());

        //should have 1 employee reporting
        assertEquals(1L, reportingStructure.getNumberOfReports());
    }
}
