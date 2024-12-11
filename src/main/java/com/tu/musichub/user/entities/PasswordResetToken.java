package com.tu.musichub.user.entities;

import com.tu.musichub.user.entityListeners.PasswordResetTokenListener;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(PasswordResetTokenListener.class)
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    private static final int EXPIRATION_TIME_HOURS = 1;

    private String id;

    private String token;

    private Date createDate;

    private Date expiryDate;

    private User user;

    public PasswordResetToken() {
    }

    public PasswordResetToken(final String token, final User user) {
        this.createDate = new Date();
        this.expiryDate = this.calculateExpiryDate();
        this.token = token;
        this.user = user;
    }

    @Id
    @Column(updatable = false, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "token", nullable = false)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "create_date", nullable = false, updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "expiry_date", nullable = false, updatable = false)
    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private Date calculateExpiryDate() {
        return DateUtils.addHours(this.createDate, EXPIRATION_TIME_HOURS);
    }
}
