package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LabelDto {
    @Size(min = 1, message = "Name must be at least 1 symbol")
    private String name;
}
