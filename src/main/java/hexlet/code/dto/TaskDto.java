package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @Size(min = 1, message = "Name must be at least 1 symbol")
    private String name;
    @Nullable
    private String description;
    @Nullable
    private Long executorId;
    @NotNull
    private Long taskStatusId;
}
