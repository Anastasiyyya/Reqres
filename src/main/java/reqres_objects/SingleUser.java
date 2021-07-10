package reqres_objects;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SingleUser {
    @Expose
    UsersForUsersList data;
    @Expose
    Support support;
}
