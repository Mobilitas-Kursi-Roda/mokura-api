package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.Reply;
import com.mokura.mokura_api.entity.Topic;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.ReplyRepository;
import com.mokura.mokura_api.repository.TopicRepository;
import com.mokura.mokura_api.repository.UserRepository;
import com.mokura.mokura_api.service.NotificationService;
import com.mokura.mokura_api.service.TopicService;
import com.mokura.mokura_api.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TopicServiceImpl implements TopicService {

    final TopicRepository topicRepository;
    final UserRepository userRepository;
    final ReplyRepository replyRepository;
    final NotificationService notificationService;

    final JWTUtil jwtUtil;

    @Autowired
    HttpServletRequest request;

    public TopicServiceImpl(TopicRepository topicRepository, UserRepository userRepository, ReplyRepository replyRepository, NotificationService notificationService, JWTUtil jwtUtil) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
        this.notificationService = notificationService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<BaseResponseDto<List<Topic>>> getAllTopics() {
        List<Topic> topics = topicRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
        topics.forEach((topic -> {
            if(topic.getImage() != null){
                topic.setImage(baseUrl +"/"+ topic.getImage());
            }
        }));
        return ResponseEntity.ok(
                new BaseResponseDto<>("success", topics)
        );
    }

    @Override
    public ResponseEntity<BaseResponseDto<Map<String, Object>>> getTopicById(Long id) {
        Optional<Topic> topic = topicRepository.findById(id);
        if (topic.isEmpty()) {
            throw new CustomBadRequestException("Topic not found");
        }
        String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
        if(topic.get().getImage() != null){
            topic.get().setImage(baseUrl +"/"+ topic.get().getImage());
        }
        List<Reply> replies = replyRepository.getRepliesByTopic(topic.get());
        replies.forEach((reply -> {
            if (reply.getImage() != null){
                reply.setImage(baseUrl +"/"+ reply.getImage());
            }
        }));
        Map<String, Object> response = new HashMap<>();
        response.put("topic", topic.get());
        response.put("replies", replies);

        return ResponseEntity.ok(
                new BaseResponseDto<>("success", response)
        );
    }

    @Override
    public ResponseEntity<BaseResponseDto<Topic>> addTopic(String title, String text, String image) {

        String authorization = request.getHeader("Authorization");
        String jwtToken = authorization.substring("Bearer ".length());
        jwtUtil.parseToken(jwtToken);
        Claims claims = jwtUtil.parseToken(jwtToken);
        String email = claims.get("email", String.class);

        Optional<User> user = userRepository.findByEmail(email);

        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setText(text);
        topic.setUser(user.orElse(null));
        topic.setImage(image);

        topicRepository.save(topic);

        return ResponseEntity.ok(new BaseResponseDto<>("success", topic));
    }

    @Override
    public ResponseEntity<BaseResponseDto<Reply>> reply(Long id, String text, String pathImage) {
        Optional<Topic> topic = topicRepository.findById(id);
        if (topic.isEmpty()) {
            throw new CustomBadRequestException("Topic not found");
        }

        String authorization = request.getHeader("Authorization");
        String jwtToken = authorization.substring("Bearer ".length());
        jwtUtil.parseToken(jwtToken);
        Claims claims = jwtUtil.parseToken(jwtToken);
        String email = claims.get("email", String.class);

        Optional<User> user = userRepository.findByEmail(email);

        Reply reply = new Reply();
        reply.setText(text);
        reply.setImage(pathImage);
        reply.setTopic(topic.get());
        reply.setUser(user.orElse(null));

        replyRepository.save(reply);

        List<Reply> replys = replyRepository.getRepliesByTopic(topic.get());
        replys.forEach(r -> {
            User u = r.getUser();
            notificationService.sendNotificationToUser(u, u.getUsername()+" reply "+topic.get().getTitle(), reply.getText(), String.valueOf(topic.get().getId_topic()));
        });
        notificationService.sendNotificationToUser(topic.get().getUser(), user.get().getUsername()+" reply "+topic.get().getTitle(), reply.getText(), String.valueOf(topic.get().getId_topic()));
        return ResponseEntity.ok(new BaseResponseDto<>("success", reply));
    }

    @Override
    public void deleteTopic(Long id) {

    }
}
