package com.mokura.mokura_api.repository;

import com.mokura.mokura_api.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
