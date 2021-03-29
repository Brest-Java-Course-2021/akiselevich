import com.epam.brest.model.dto.ProjectDTO;

import java.util.List;

public interface ProjectDTODAO {

    List<ProjectDTO> findAllProjectWithEmployeeCount();

}
