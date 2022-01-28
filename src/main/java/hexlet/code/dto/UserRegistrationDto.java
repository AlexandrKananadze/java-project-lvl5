package hexlet.code.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRegistrationDto {
    @NotNull
    @Email(message = "Email invalid")
    private String email;

    @NotNull
    @Size(min = 1, message = "Firstname must be at least 1 symbol")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "Lastname must be at least 1 symbol")
    private String lastName;

    @NotNull
    @Size(min = 3, message = "Password must be at least 3 symbols")
    private String password;

    public UserRegistrationDto(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public UserRegistrationDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
