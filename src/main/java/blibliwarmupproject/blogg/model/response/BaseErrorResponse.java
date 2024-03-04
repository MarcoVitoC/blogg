package blibliwarmupproject.blogg.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseErrorResponse {
    private int code;
    private String status;
    private String errors;
}
