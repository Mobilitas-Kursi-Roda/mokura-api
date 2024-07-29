package com.mokura.mokura_api.controller.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.Reply;
import com.mokura.mokura_api.entity.Topic;
import com.mokura.mokura_api.service.TopicService;
import com.mokura.mokura_api.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client/topics")
public class TopicClientController {

    private final TopicService topicService;
    private final UploadService uploadService;

    public TopicClientController(TopicService topicService, UploadService uploadService) {
        this.topicService = topicService;
        this.uploadService = uploadService;
    }

    @GetMapping
    public ResponseEntity<BaseResponseDto<List<Topic>>> getTopics() {
        return topicService.getAllTopics();
    }

    @PostMapping
    public ResponseEntity<BaseResponseDto<Topic>> createTopic(
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        String pathImage = null;
        if (file != null && !file.isEmpty()) {
            pathImage = uploadService.upload(file);
        }
        return topicService.addTopic(title, text, pathImage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<Map<String, Object>>> getTopic(@PathVariable Long id) {
        return topicService.getTopicById(id);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<BaseResponseDto<Reply>> replyTopic(
            @PathVariable Long id,
            @RequestParam("text") String text,
            @RequestParam(value = "image", required = false) MultipartFile file
    ) {
        String pathImage = null;
        if (file != null && !file.isEmpty()) {
            pathImage = uploadService.upload(file);
        }
        return topicService.reply(id, text, pathImage);
    }

}
