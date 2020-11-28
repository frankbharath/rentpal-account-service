package com.rentpal.accounts.model;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class TokenId implements Serializable {

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="userId", referencedColumnName = "id", nullable = false)
    private User user;

    private int type;

    public TokenId(User user, int type) {
        this.user = user;
        this.type = type;
    }

    public TokenId() {}

    public User getUser() {
        return user;
    }

    public int getType() {
        return type;
    }
}
