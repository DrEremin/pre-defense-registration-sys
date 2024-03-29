package ru.dreremin.predefense.registration.sys.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.dreremin.predefense.registration.sys.models.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
	
	Optional<Teacher> findByPersonId(Long id);
	
	default boolean existsByPersonId(Long id) {
		return findByPersonId(id).isPresent();
	}
	
	@Query("select new Teacher("
			+ "t.id, "
			+ "t.personId, "
			+ "t.jobTitle) "
			+ "from Actor a "
			+ "join Person p "
				+ "on a.id = p.actorId "
			+ "join Teacher t "
				+ "on p.id = t.personId "
			+ "where a.login = :username")
	Optional<Teacher> findByActorLogin(@Param("username") String login);
	
	@Query("select new Teacher("
			+ "t.id, "
			+ "t.personId, "
			+ "t.jobTitle) "
			+ "from Actor a "
			+ "join Person p "
				+ "on a.id = p.actorId "
			+ "join Teacher t "
				+ "on p.id = t.personId "
			+ "where a.id = :id")
	Optional<Teacher> findByActorId(@Param("id") long id);
	
	@Query("select new Teacher("
			+ "t.id, "
			+ "t.personId, "
			+ "t.jobTitle) "
			+ "from Teacher t join Person p "
				+ "on t.personId = p.id "
			+ "order by p.lastName")
	Page<Teacher> findAllOrderByLastName(Pageable pageable);
	
	@Query("select new Teacher("
			+ "t.id, "
			+ "t.personId, "
			+ "t.jobTitle) "
			+ "from Commission c "
			+ "join TeacherCommission tc "
				+ "on c.id = tc.commissionId "
			+ "join Teacher t "
				+ "on tc.teacherId = t.id "
			+ "join Person p "
				+ "on t.personId = p.id "
			+ "where c.id = :id")
	List<Teacher> findAllByCommissionId(@Param("id") int id, Sort sort);
}
