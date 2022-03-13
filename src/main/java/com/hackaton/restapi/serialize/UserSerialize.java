package com.hackaton.restapi.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hackaton.restapi.entity.User;

public class UserSerialize extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator gen, SerializerProvider arg2) throws IOException {
        gen.useDefaultPrettyPrinter();
        gen.writeStartObject();
        gen.writeFieldName("id");
        gen.writeObject(user.getId());
        gen.writeFieldName("username");
        gen.writeObject(user.getUsername());
        gen.writeFieldName("dateCreation");
        gen.writeObject(user.getDateCreation());
        gen.writeFieldName("role");
        gen.writeObject(user.getRole());
        gen.writeEndObject();
    }
    
}
