package reqres_objects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    String name;
    String job;
    String id;
    String token;
}
