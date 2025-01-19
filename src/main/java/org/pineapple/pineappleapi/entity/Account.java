package org.pineapple.pineappleapi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Document
public class Account implements Serializable, UserDetails {
    @Id
    private String id;

    private String username;
    private String password;

    private List<String> roles;
    private LocalDateTime timestamp = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(it -> new SimpleGrantedAuthority("ROLE_" + it)).toList();
    }
}
