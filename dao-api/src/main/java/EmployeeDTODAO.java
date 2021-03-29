import com.epam.brest.model.dto.EmployeeDTO;
import java.util.List;

public interface EmployeeDTODAO {

    List<EmployeeDTO> findAllEmployeeWithRoles();

}
