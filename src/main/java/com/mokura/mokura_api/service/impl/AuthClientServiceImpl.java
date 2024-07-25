package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.dto.ReqLoginDto;
import com.mokura.mokura_api.dto.ReqRegisterDto;
import com.mokura.mokura_api.dto.ResLoginClient;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.UserRepository;
import com.mokura.mokura_api.service.AuthClientService;
import com.mokura.mokura_api.util.JWTUtil;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthClientServiceImpl implements AuthClientService {

    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthClientServiceImpl(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<BaseResponseDto<ResLoginClient>> login(ReqLoginDto reqLoginDto) {
        Optional<User> userOptional = null;
        if (reqLoginDto.getUsername() != null){
            userOptional = userRepository.findByUsername(reqLoginDto.getUsername());
            System.out.println(reqLoginDto.getUsername());
        }
        if (reqLoginDto.getEmail() != null){
            userOptional = userRepository.findByEmail(reqLoginDto.getEmail());
            System.out.println(reqLoginDto.getEmail());
        }
        if (userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( new BaseResponseDto<>(
                    "Username or email invalid",null
            ));
        }
        User user = userOptional.get();
        boolean isValid = passwordEncoder.matches(reqLoginDto.getPassword(), user.getPassword());
        if(!isValid){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new BaseResponseDto<>(
                            "Username or password invalid",null
                    )
            );
        }
        String token = jwtUtil.generateToken(user);
        Date expiresAt = jwtUtil.parseToken(token).getExpiration();
        ResLoginClient resLoginClient = new ResLoginClient();
        resLoginClient.setUsername(user.getUsername());
        resLoginClient.setEmail(user.getEmail());
        resLoginClient.setToken(token);
        resLoginClient.setFullname(user.getFull_name());
        resLoginClient.setExpire(expiresAt);
        resLoginClient.setUser_id(user.getId_user());
        System.out.println("sampai sini");
        return ResponseEntity.ok().body(
                new BaseResponseDto<>("Login Success",resLoginClient)
        );
    }

    @Override
    public ResponseEntity<BaseResponseDto<Object>> register(ReqRegisterDto reqRegisterDto) {
        Optional<User> email = userRepository.findByEmail(reqRegisterDto.getEmail());
        if (email.isPresent()){
            throw new CustomBadRequestException("Email already in use");
        }

        Optional<User> username = userRepository.findByUsername(reqRegisterDto.getUsername());
        if (username.isPresent()){
            throw new CustomBadRequestException("Username already in use");
        }
        User user = new User();
        if (reqRegisterDto.getUsername() == null){
            reqRegisterDto.setUsername(generateUsername(reqRegisterDto.getFull_name()));
        }
        user.setUsername(reqRegisterDto.getUsername());
        user.setFull_name(reqRegisterDto.getFull_name());
        user.setEmail(reqRegisterDto.getEmail());
        user.setPassword(passwordEncoder.encode(reqRegisterDto.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponseDto<>(
                        "User Created", user
                )
        );
    }

    @Override
    public ResponseEntity<BaseResponseDto<Boolean>> checkUsernameOrEmail(String key) {
        System.out.println(key);
        Optional<User> email = userRepository.findByEmail(key);
        Optional<User> username = userRepository.findByUsername(key);
        if (email.isPresent()){
            return ResponseEntity.ok(new BaseResponseDto<>(
                    "email already exist", false
            ));
        }
        if (username.isPresent()){
            return ResponseEntity.ok(new BaseResponseDto<>(
                    "username already exist", false
            ));
        }
        return ResponseEntity.ok(new BaseResponseDto<>(
                "username or email free to use", true
        ));
    }

    private String generateUsername(String fullName) {
        // Convert to lowercase and remove spaces
        String baseUsername = fullName.toLowerCase().replaceAll("\\s+", "");

        // Get the current time in milliseconds
        String timestamp = generateTimestamp();

        // Combine base username and timestamp
        return baseUsername + timestamp;
    }

    private String generateTimestamp() {
        // Get current time in milliseconds since epoch
        long currentTimeMillis = Instant.now().toEpochMilli();

        // Use only the last 4 digits of the current time in milliseconds
        String timestamp = String.valueOf(currentTimeMillis % 10000);

        // Ensure the timestamp is always 4 digits by padding with zeros if necessary
        return String.format("%04d", Integer.parseInt(timestamp));
    }
}
