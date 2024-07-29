package com.mokura.mokura_api.service;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.Reply;
import com.mokura.mokura_api.entity.Topic;
import com.mokura.mokura_api.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TopicService {
    ResponseEntity<BaseResponseDto<List<Topic>>> getAllTopics();
    ResponseEntity<BaseResponseDto<Map<String, Object>>> getTopicById(Long id);
    ResponseEntity<BaseResponseDto<Topic>> addTopic(String title, String text, String image);
    ResponseEntity<BaseResponseDto<Reply>> reply(Long id, String text, String pathImage);
    void deleteTopic(Long id);
}
