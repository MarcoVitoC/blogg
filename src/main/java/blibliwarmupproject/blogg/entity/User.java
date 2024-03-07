package blibliwarmupproject.blogg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("users")
public class User implements Persistable<String> {
    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String username;

    private String email;

    private String password;

    @Transient
    @JsonIgnore
    private boolean isNewUser = true;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return this.isNewUser;
    }
}
