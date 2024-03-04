package blibliwarmupproject.blogg.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {
    private int code;
    private String status;
    private T data;

    public static<R> Mono<BaseResponse<R>> ok(Mono<R> data) {
        return data.map(response -> BaseResponse.<R>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .data(response)
            .build()
        );
    }
}
