import com.epam.brest.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectDAO {

    List<Project> findAll();

    Optional<Project> findById(Integer projectId);

    Project create(Project project);

    Project update(Project project);

    void delete(Integer projectId);

}
