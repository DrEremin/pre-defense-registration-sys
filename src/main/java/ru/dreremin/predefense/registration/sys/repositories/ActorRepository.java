package ru.dreremin.predefense.registration.sys.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long>{
	
	Optional<Actor> findByLogin(String login);
	
	default boolean existsByLogin(String login) {
		return findByLogin(login).isPresent();
	}
	
	@Query("select new Actor("
			+ "a.id, "
			+ "a.login, "
			+ "a.password, "
			+ "a.role) "
			+ "from Student s "
			+ "join Person p on s.personId = p.id "
			+ "join Actor a on p.actorId = a.id "
			+ "where s.id = :studentId")
	Optional<Actor> findByStudentId(@Param("studentId") long id);
	
	@Query("select new Actor("
			+ "a.id, "
			+ "a.login, "
			+ "a.password, "
			+ "a.role) "
			+ "from Teacher t "
			+ "join Person p on t.personId = p.id "
			+ "join Actor a on p.actorId = a.id "
			+ "where t.id = :teacherId")
	Optional<Actor> findByTeacherId(@Param("teacherId") int id);
	
	@Query("select ac "
			+ "from Actor ac "
			+ "join Administrator ad "
				+ "on ac.id = ad.actorId")
	Page<Actor> findAllAdministrators(Pageable pageable);
}

 