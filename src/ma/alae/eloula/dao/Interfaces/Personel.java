package ma.alae.eloula.dao.Interfaces;

import ma.alae.eloula.classes.Employee;

import java.util.List;
import java.util.Optional;

public interface Personel {
    Optional<Employee> ajouterEmployee(Employee employee);
    int SupprimerEmpl(int i);
    Optional<Employee> getEmployeeById(int id);
    Employee findEmployeeById(int employeeId);
    Optional<List<Employee>> findAllEmployees();
    boolean modifierEmployee(Employee employee);
    boolean employeeExists(int employeeId);


}
