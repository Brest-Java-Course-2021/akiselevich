import com.epam.brest.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDAO {

    List<Employee> findAll();

    Optional<Employee> findById(Integer employeeId);

    Employee create(Employee employee);

    Employee update(Employee employee);

    void delete(Integer employeeId);

}
