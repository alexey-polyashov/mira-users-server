package mira.users.ms.repositories;

import mira.users.ms.entity.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {

    Set<RoleModel> findByNameIn(String[] roles);
    Optional<RoleModel> findByName(String roleName);

}
