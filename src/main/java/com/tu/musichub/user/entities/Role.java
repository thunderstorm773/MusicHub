package com.tu.musichub.user.entities;

import com.tu.musichub.user.entityListeners.RoleEntityListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;

@Entity
@EntityListeners(RoleEntityListener.class)
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
