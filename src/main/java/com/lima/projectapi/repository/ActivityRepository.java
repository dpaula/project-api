package com.lima.projectapi.repository;

import com.lima.projectapi.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Fernando de Lima on 08/09/23
 */
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}
