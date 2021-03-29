import com.epam.brest.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDAO {

    List<Role> findAll();

    Optional<Role> findById(Integer roleId);

    Role create(Role role);

    Role update(Role role);

    void delete(Integer roleId);

}
