package hu.gde.runnersdemo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoeRepository extends JpaRepository<ShoeEntity, Long> {

}
