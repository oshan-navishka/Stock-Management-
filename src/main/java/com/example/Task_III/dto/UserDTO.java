package com.example.Task_III.dto;

import com.example.Task_III.enumaration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private UserRole role;
    private String name;
    private String address;
    private String phone;
}
