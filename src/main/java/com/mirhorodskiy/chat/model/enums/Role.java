package com.mirhorodskiy.chat.model.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.READ, Permission.WRITE)),
    MANAGER(Set.of(Permission.READ, Permission.WRITE, Permission.MANAGE_CHATS, Permission.GENERATE_REPORTS)),
    ADMIN(Set.of(Permission.READ, Permission.WRITE, Permission.MANAGE_USERS, Permission.MANAGE_CHATS, Permission.GENERATE_REPORTS));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
