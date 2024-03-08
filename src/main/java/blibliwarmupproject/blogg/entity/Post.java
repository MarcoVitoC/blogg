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

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("posts")
public class Post implements Persistable<String> {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private Long categoryId;

    private String title;

    private String body;

    @Transient
    @JsonIgnore
    private boolean isNewPost = true;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return this.isNewPost;
    }
}
