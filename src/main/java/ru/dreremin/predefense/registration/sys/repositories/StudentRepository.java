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

import ru.dreremin.predefense.registration.sys.models.Student;
import ru.dreremin.predefense.registration.sys.models.Teacher;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
	
	Optional<Student> findByPersonId(Long id);
	
	default boolean existsByPersonId(Long id) {
		return findByPersonId(id).isPresent();
	}
	
	@Query("select new Student("
			+ "s.id, "
			+ "s.personId, "
			+ "s.groupNumber, "
			+ "s.studyDirection, "
			+ "s.studyType) "
			+ "from Actor a "
			+ "join Person p "
				+ "on a.id = p.actorId "
			+ "join Student s "
				+ "on p.id = s.personId "
			+ "where a.id = :id")
	Optional<Student> findByActorId(@Param("id") long id);
	
	@Query("select new Student("
			+ "s.id, "
			+ "s.personId, "
			+ "s.groupNumber, "
			+ "s.studyDirection, "
			+ "s.studyType) "
			+ "from Actor a "
			+ "join Person p "
				+ "on a.id = p.actorId "
			+ "join Student s "
				+ "on p.id = s.personId "
			+ "where a.login = :username")
	Optional<Student> findByActorLogin(@Param("username") String login);
	
	@Query("select new Student("
			+ "s.id, "
			+ "s.personId, "
			+ "s.groupNumber, "
			+ "s.studyDirection, "
			+ "s.studyType) "
			+ "from Student s "
			+ "join Person p "
				+ "on s.personId = p.id "
			+ "order by p.lastName asc")
	Page<Student> findAllOrderByLastName(Pageable pageable);
	
	@Query("select new Student("
			+ "s.id, "
			+ "s.personId, "
			+ "s.groupNumber, "
			+ "s.studyDirection, "
			+ "s.studyType) "
			+ "from Commission c "
			+ "join StudentCommission sc "
				+ "on c.id = sc.commissionId "
			+ "join Student s "
				+ "on sc.studentId = s.id "
			+ "join Person p "
				+ "on s.personId = p.id "
			+ "where c.id = :id")
	List<Student> findAllByCommissionId(@Param("id") int id, Sort sort);
}
