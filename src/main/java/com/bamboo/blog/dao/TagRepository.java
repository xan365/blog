package com.bamboo.blog.dao;

import com.bamboo.blog.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    @Query(value = "select * from t_Tag", nativeQuery = true)
    List<Tag> findTop(Pageable pageable);
}
