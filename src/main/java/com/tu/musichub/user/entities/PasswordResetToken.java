package com.tu.musichub.user.entities;

import com.tu.musichub.user.entityListeners.PasswordResetTokenListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(PasswordResetTokenListener.class)
@Table(name = "password_reset_tokens")
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetToken {

    private static final int EXPIRATION_TIME_HOURS = 1;

    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "create_date", nullable = false, updatable = false)
    private Date createDate;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    public PasswordResetToken(final String token, final User user) {
        this.createDate = new Date();
        this.expiryDate = this.calculateExpiryDate();
        this.token = token;
        this.user = user;
    }

    private Date calculateExpiryDate() {
        return DateUtils.addHours(this.createDate, EXPIRATION_TIME_HOURS);
    }
}
