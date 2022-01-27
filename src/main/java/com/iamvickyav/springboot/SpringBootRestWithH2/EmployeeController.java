package com.iamvickyav.springboot.SpringBootRestWithH2;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.iamvickyav.springboot.SpringBootRestWithH2.model.Employee;
import com.iamvickyav.springboot.SpringBootRestWithH2.service.EmployeeRepository;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;



    // Select, Insert, Delete, Update Operations for an Employee
    @RequestMapping(value = "/pof", method = RequestMethod.GET)
    public void proofOfConecpt(){

        List<Employee> employeeList  = employeeRepository.findAll();
        employeeList.stream().forEach(employee -> {
            System.out.println(ReflectionToStringBuilder.toString(employee));
        });

        Employee employeeToSearch = employeeRepository.findByName("tom");
        if (employeeToSearch!=null) {
            System.out.println("non existing employee" +
                    ReflectionToStringBuilder.toString(employeeToSearch));
        } else{
            System.out.println("non existing employee: not found");
        }

        try {
            System.out.println("non existing employee2" +
                    ReflectionToStringBuilder.toString(employeeRepository.findByName("tom")));
        } catch (Exception e) {
            System.out.println("non existing employee2 failed exception"+e.getClass().getSimpleName());
        }

        Employee newEmployee = new Employee();
        newEmployee.setId(3);
        newEmployee.setName("Mary");
        employeeRepository.save(newEmployee);

        employeeList.stream().forEach(employee -> {
            System.out.println(ReflectionToStringBuilder.toString(employee));
        });

/*        System.out.println("Duplicate employee"+
                ReflectionToStringBuilder.toString(employeeRepository.findByName("Mary")));

 */

        try {
            System.out.println("Duplicate employee" +
                    ReflectionToStringBuilder.toString(employeeRepository.findOneByName("Mary")));
        } catch (NonUniqueResultException e ){
          System.out.println("a:"+e);
        } catch (IncorrectResultSizeDataAccessException e) {
            System.out.println("b:"+e);
        } catch (Exception e) {
            System.out.println("c:"+e);

        }

    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
    Employee getEmployee(@PathVariable Integer id){
        return  employeeRepository.findById(id).get();
    }

    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    String addEmployee(@RequestBody Employee employee){
        Employee savedEmployee = employeeRepository.save(employee);
        return "SUCCESS";
    }

    @RequestMapping(value = "/employee", method = RequestMethod.PUT)
    Employee updateEmployee(@RequestBody Employee employee){
        Employee updatedEmployee = employeeRepository.save(employee);
        return updatedEmployee;
    }

    @RequestMapping(value = "/employee", method = RequestMethod.DELETE)
    Map<String, String> deleteEmployee(@RequestParam Integer id){
        Map<String, String> status = new HashMap<>();
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isPresent()) {
            employeeRepository.delete(employee.get());
            status.put("Status", "Employee deleted successfully");
        }
        else {
            status.put("Status", "Employee not exist");
        }
        return status;
    }

    // Select, Insert, Delete for List of Employees

    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }

    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    String addAllEmployees(@RequestBody List<Employee> employeeList){
        employeeRepository.saveAll(employeeList);
        return "SUCCESS";
    }

    @RequestMapping(value = "/employees", method = RequestMethod.DELETE)
    String addAllEmployees(){
        employeeRepository.deleteAll();
        return "SUCCESS";
    }
}
