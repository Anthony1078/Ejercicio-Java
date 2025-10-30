package com.example.eval_java.infrastructure.mapper;


import com.example.eval_java.shared.dto.*;
import com.example.eval_java.infrastructure.persistence.entity.*;

import java.util.ArrayList;

public class UserMapper {

    public UserEntity toEntity(RegisterUserRequest req, String passwordHash, String token) {
        var u = new UserEntity();
        u.setName(req.name());
        u.setEmail(req.email().toLowerCase());
        u.setPasswordHash(passwordHash);
        u.setToken(token);
        u.setIsActive(true);

        var phones = new ArrayList<PhoneEntity>();
        for (var p : req.phones()) {
            var pe = new PhoneEntity();
            pe.setNumber(p.number());
            pe.setCitycode(p.citycode());
            pe.setContrycode(p.contrycode());
            pe.setUser(u);
            phones.add(pe);
        }
        u.setPhones(phones);
        return u;
    }

    public UserDto toDto(UserEntity e) {
        var phones = e.getPhones().stream()
                .map(p -> new PhoneDto(p.getId(), p.getNumber(), p.getCitycode(), p.getContrycode()))
                .toList();
        return new UserDto(
                e.getId(), e.getName(), e.getEmail(),
                e.getCreated(), e.getModified(), e.getLastLogin(),
                e.getToken(), Boolean.TRUE.equals(e.getIsActive()), phones
        );
    }
}
