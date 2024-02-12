package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Find the employee ID and number of reporting employees
     *
     * @param id - The ID of the root employee
     * @return ReportingStructure of root employee and number of reports
     */
    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Getting employee reports for id [{}]", id);

        ReportingStructure structure = new ReportingStructure();
        //Find the employee based on the id passed in
        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        structure.setEmployee(employee);

        List<Employee> reportsList = new ArrayList<>();
        //Find the number of reports by generating a list of all reporting employees
        structure.setNumberOfReports(getAllReportingEmployees(reportsList, employee).size());

        return structure;
    }

    /**
     * Recursively generate a flattened list of all employees that report to the employee id in the request.
     *
     * @param reportsList List used to flatten the tree of employees and their direct reports
     * @param employee Current employee we are checking in the tree
     * @return Flattened list of all employees that report to the root employee
     */
    private List<Employee> getAllReportingEmployees(List<Employee> reportsList, Employee employee) {
        List<Employee> directReports = employee.getDirectReports();

        //If no direct reports, return or continue to the next child.
        if(directReports != null) {
            for(Employee childEmployee : directReports) {
                //Find the child employee so we have the full object and can check its direct reports,
                // add this child to the list, and then recursively check its children.
                childEmployee = employeeRepository.findByEmployeeId(childEmployee.getEmployeeId());
                reportsList.add(childEmployee);
                getAllReportingEmployees(reportsList, childEmployee);
            }
        }

        return reportsList;
    }
}
