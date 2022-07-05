package kots.repository;

import kots.model.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {

}
