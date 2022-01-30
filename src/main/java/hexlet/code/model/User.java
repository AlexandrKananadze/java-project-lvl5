package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

@Table(name = "users")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "firstName")
    private String firstName;

    @NotNull
    @Column(name = "lastName")
    @Size(min = 1, message = "Lastname must be at least 1 symbol")
    private String lastName;

    @NotNull
    @Column(unique = true, name = "email")
    @Email(message = "Email invalid")
    private String email;

    @NotNull
    @JsonIgnore
    @Column(name = "password")
    @Size(min = 3, message = "Password must be at least 3 symbols")
    private String password;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
