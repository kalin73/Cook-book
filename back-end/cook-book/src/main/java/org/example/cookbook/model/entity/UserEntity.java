package org.example.cookbook.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.example.cookbook.model.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(targetEntity = RecipeEntity.class, mappedBy = "user", fetch = FetchType.EAGER)
    private List<RecipeEntity> recipes;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public List<RecipeEntity> getRecipes() {
        return recipes;
    }

    public UserEntity setRecipes(List<RecipeEntity> recipes) {
        this.recipes = recipes;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public UserEntity setRole(Role role) {
        this.role = role;
        return this;
    }
}
