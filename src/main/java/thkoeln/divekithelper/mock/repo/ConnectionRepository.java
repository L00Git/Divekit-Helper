package thkoeln.divekithelper.mock.repo;

import org.springframework.data.repository.CrudRepository;
import thkoeln.divekithelper.mock.repo.entities.Connection;

import java.util.UUID;

public interface ConnectionRepository extends CrudRepository<Connection, UUID> {
}
