package ru.practicum.main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationUpdateDto {

    @Size(min = 3, max = 200)
    private String title;

    private Boolean pinned;

    private List<Long> events;
}
