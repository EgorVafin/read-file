package org.example.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Converter
public class UserRolesConverter implements AttributeConverter<List<String>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> roles) {

        String userInfoJson = null;
        try {
            userInfoJson = objectMapper.writeValueAsString(roles);
        } catch (final JsonProcessingException e) {

        }
        return userInfoJson;
    }

    @Override
    public List<String> convertToEntityAttribute(String rolesJson) {

        List<String> roles = null;
        try {
            roles = objectMapper.readValue(rolesJson,
                    new TypeReference<List<String>>() {
                    });
        } catch (final IOException e) {

        }
        return roles;
    }
}
