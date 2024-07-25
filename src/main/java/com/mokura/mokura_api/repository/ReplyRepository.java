package com.mokura.mokura_api.repository;

import com.mokura.mokura_api.entity.Reply;
import com.mokura.mokura_api.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> getRepliesByTopic(Topic topic);
}
