package com.contestmodule.contest.service;


import com.contestmodule.contest.controller.UserController;
import com.contestmodule.contest.dto.UserDto;
import com.contestmodule.contest.entity.Role;
import com.contestmodule.contest.entity.User;
import com.contestmodule.contest.repository.RoleRepository;
import com.contestmodule.contest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private RoleService roleService;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String userName) {
        Optional<User> foundUser = Optional.ofNullable(userRepository.findByEmail(userName));
        userRepository.deleteById(foundUser.get().getId());
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent())
            userRepository.delete(user.get());
    }


    public User getUserByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user, boolean isAdmin) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (isAdmin)
            user.getRoles().add(roleService.findRoleByName("ADMIN"));
        user.getRoles().add(roleService.findRoleByName("USER"));
        return userRepository.save(user);
    }

    public User saveWithoutEncryptingPassword(User user, boolean isAdmin) {
        if (isAdmin)
            user.getRoles().add(roleService.findRoleByName("ADMIN"));
        user.getRoles().add(roleService.findRoleByName("USER"));
        return userRepository.save(user);
    }


}
