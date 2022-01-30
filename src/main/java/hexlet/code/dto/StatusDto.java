package hexlet.code.dto;

import javax.validation.constraints.Size;

public class StatusDto {
    @Size(min = 1, message = "Name must be at least 1 symbol")
    private String name;

    public StatusDto(String name) {
        this.name = name;
    }

    public StatusDto() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
