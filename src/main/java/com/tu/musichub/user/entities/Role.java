package com.tu.musichub.user.entities;

import com.tu.musichub.user.entityListeners.RoleEntityListener;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;

@Entity
@EntityListeners(RoleEntityListener.class)
@Table(name = "roles")
public class Role implements GrantedAuthority {

    private String id;

    private String authority;

    public Role() {
    }

    @Id
    @Column(nullable = false, updatable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, unique = true)
    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
