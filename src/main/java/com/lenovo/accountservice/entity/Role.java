package com.lenovo.accountservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    private String id;
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
