package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserKeycloak {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String role;
    private String organisationId;
}
