package hexlet.code.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    @Size(min = 1, message = "Name must be at least 1 symbol")
    private String name;
    @Nullable
    @Column(name = "description")
    private String description;
    @NotNull
    @ManyToOne
    private Status taskStatus;
    @ManyToOne
    private User author;
    @ManyToOne
    @Nullable
    private User executor;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private Date createdAt;

    public Task(String name, @Nullable String description, Status taskStatus, User author, @Nullable User executor) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.author = author;
        this.executor = executor;
    }
}
