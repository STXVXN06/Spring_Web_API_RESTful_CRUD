package com.steven.curso.springboot.app.springboot_crud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.steven.curso.springboot.app.springboot_crud.entities.Route;

public interface RouteRepository extends CrudRepository<Route,Long>{

    List<Route> findByPathAndMethod(String path, String method);

    @Query("select r from Route r left join fetch r.roles")
    List<Route> findAllWithRoles();

}
