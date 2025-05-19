package com.violet6bee.otp_rest_api.service;

import com.violet6bee.otp_rest_api.dto.UserDTO;
import com.violet6bee.otp_rest_api.entity.UserEntity;
import com.violet6bee.otp_rest_api.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.violet6bee.otp_rest_api.enums.Role;
import com.violet6bee.otp_rest_api.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public UserEntity save(UserEntity user) {

        return repository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public UserEntity create(UserEntity user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        if (user.getRole() == Role.ROLE_ADMIN && isAdminExists()) {
            throw new RuntimeException("Роль админа занята");
        }

        save(user);
        return user;
    }

    private boolean isAdminExists() {
        return repository.existsByRole(Role.ROLE_ADMIN);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public UserEntity getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {

        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public UserEntity getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    /**
     * Список пользователей, кроме администраторов
     * <p>
     *
     */
    public List<UserDTO> getAllUsers() {
        var user = UserEntity.builder().role(Role.ROLE_USER)
                .build();
        Example<UserEntity> example = Example.of(user);
        return repository.findAll(example).stream()
                .map(x -> UserDTO.builder()
                        .id(x.getId())
                        .username(x.getUsername())
                        .email(x.getEmail())
                        .build()
                ).collect(Collectors.toList());
    }

    public boolean deleteUser(Long id) {
        repository.deleteById(id);
        return true;
    }
}

