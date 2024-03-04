package blibliwarmupproject.blogg.Exception;

import org.springframework.data.relational.core.sql.Not;

public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
}
