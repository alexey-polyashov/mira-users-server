package mira.users.ms.repositories;

import mira.users.ms.entity.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {

    List<RoleModel> findByNameIn(String[] roles);
    Optional<RoleModel> findByName(String roleName);

}
