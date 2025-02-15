package com.strelive.dto;

import com.strelive.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractToken {
    private String email;
    private Set<Role> role;
    private String type;
    private Date date;
}
