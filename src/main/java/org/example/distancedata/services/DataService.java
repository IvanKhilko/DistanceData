package org.example.distancedata.services;

import java.util.List;
import org.example.distancedata.exception.BadRequestException;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface DataService<T, D> {
    T create(T entity) throws BadRequestException;

    List<T> read();

    T getByName(String name) throws ResourceNotFoundException;

    T getByID(Long id) throws ResourceNotFoundException;

    void update(T entity) throws ResourceNotFoundException;

    void delete(Long id) throws ResourceNotFoundException;

    void createBulk(List<D> list) throws BadRequestException;
}