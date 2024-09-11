package org.example.expert.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleChangeRequest {

    private String role;


    public void setNewRole(String role){
        this.role = role;
    }
}
