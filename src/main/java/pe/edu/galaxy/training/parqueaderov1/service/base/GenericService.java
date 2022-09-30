package pe.edu.galaxy.training.parqueaderov1.service.base;

import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceException;
import pe.edu.galaxy.training.parqueaderov1.service.exception.ServiceSqlException;

import java.util.List;
import java.util.Optional;

public interface GenericService<T> {
    List<T> findAll() throws ServiceException;
    Optional<T> findById(long id) throws ServiceException;
    List<T> findByObject(T t) throws ServiceException;
    T save(T t) throws ServiceException;
    T update(T t) throws ServiceException;
    void deleteLogic(Long id) throws ServiceException;
}
