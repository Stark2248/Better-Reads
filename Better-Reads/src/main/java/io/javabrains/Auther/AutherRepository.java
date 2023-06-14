package io.javabrains.Auther;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutherRepository extends CassandraRepository<Auther, String> {

}
