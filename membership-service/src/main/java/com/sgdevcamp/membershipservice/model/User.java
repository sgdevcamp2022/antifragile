package com.sgdevcamp.membershipservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    @Type(type="json")
    @Column(columnDefinition = "json")
    private Profile profile;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String introduction;

    @Column(insertable = false, updatable = false)
    protected AuthType auth_type;

    private LocalDateTime access_time;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "salt_id")
    private Salt salt;

    public void updateRole(UserRole role){
        this.role = role;
    }

    public void updateProfile(Profile profile){
        this.profile = profile;
    }

    public void updateIntroduction(String introduction){
        this.introduction = introduction;
    }

}
