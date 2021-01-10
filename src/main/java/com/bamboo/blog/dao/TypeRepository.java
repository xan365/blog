package com.bamboo.blog.dao;

import com.bamboo.blog.entity.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {

    Type findByName(String name);

    @Query(value = "SELECT * FROM t_Type", nativeQuery = true)
    List<Type> findTop(Pageable pageable);
}
