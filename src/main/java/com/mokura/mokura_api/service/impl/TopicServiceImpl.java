package com.mokura.mokura_api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.Reply;
import com.mokura.mokura_api.entity.Topic;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.ReplyRepository;
import com.mokura.mokura_api.repository.TopicRepository;
import com.mokura.mokura_api.repository.UserRepository;
import com.mokura.mokura_api.service.TopicService;
import com.mokura.mokura_api.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TopicServiceImpl implements TopicService {

    private static final Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);
    final TopicRepository topicRepository;
    final UserRepository userRepository;
    final ReplyRepository replyRepository;

    final JWTUtil jwtUtil;

    @Autowired
    HttpServletRequest request;

    public TopicServiceImpl(TopicRepository topicRepository, UserRepository userRepository, ReplyRepository replyRepository, JWTUtil jwtUtil) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
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
        if (!topic.isPresent()) {
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
    public ResponseEntity<BaseResponseDto<Topic>> addTopic(String title, String text, MultipartFile file) {

        String authorization = request.getHeader("Authorization");
        String jwtToken = authorization.substring("Bearer ".length());
        jwtUtil.parseToken(jwtToken);
        Claims claims = jwtUtil.parseToken(jwtToken);
        String email = claims.get("email", String.class);

        Optional<User> user = userRepository.findByEmail(email);

        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setText(text);
        topic.setUser(user.get());

//        upload image
        if (file.isEmpty() || file == null) {
            log.info("File is empty");
        }else{
            try {
                String uploadFolder = "uploads";
                // Ensure the upload folder exists
                Path uploadPath = Paths.get(uploadFolder);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique filename using timestamp
                String originalFilename = file.getOriginalFilename();
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String uniqueFilename = timestamp + "-" + originalFilename;


                // Save the file
                byte[] bytes = file.getBytes();
                Path path = uploadPath.resolve(uniqueFilename);
                Files.write(path, bytes);

                topic.setImage(String.valueOf(path));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        topicRepository.save(topic);

        return ResponseEntity.ok(new BaseResponseDto<>("success", topic));
    }

    @Override
    public ResponseEntity<BaseResponseDto<Reply>> reply(Long id, String text, MultipartFile image) {
        Optional<Topic> topic = topicRepository.findById(id);
        if (topic.isEmpty()) {
            throw new CustomBadRequestException("Topic not found");
        }
        String pathImage = null;
        if (!image.isEmpty() || image != null) {
            pathImage = uploadFile(image);
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
        reply.setUser(user.get());

        replyRepository.save(reply);

        return ResponseEntity.ok(new BaseResponseDto<>("success", reply));
    }

    @Override
    public void deleteTopic(Long id) {

    }

    public String uploadFile(MultipartFile file) {
        try {
            String uploadFolder = "uploads";
            // Ensure the upload folder exists
            Path uploadPath = Paths.get(uploadFolder);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate a unique filename using timestamp
            String originalFilename = file.getOriginalFilename();
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String uniqueFilename = timestamp + "-" + originalFilename;


            // Save the file
            byte[] bytes = file.getBytes();
            Path path = uploadPath.resolve(uniqueFilename);
            Files.write(path, bytes);

            return String.valueOf(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
