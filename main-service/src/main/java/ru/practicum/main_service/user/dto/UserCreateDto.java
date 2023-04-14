package ru.practicum.main_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;
}
