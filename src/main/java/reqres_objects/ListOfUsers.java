package reqres_objects;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class ListOfUsers {
    @Expose
    ArrayList<UsersForUsersList> data;
}
