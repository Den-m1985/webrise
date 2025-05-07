package com.example.webrise.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
public class Contact extends BaseEntity {

    @Column(name = "phone_number", unique = true)
    @Pattern(regexp = "^\\+\\d{1,3}\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_contact_user"))
    private User user;
}
