package reqres_objects;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Support {
    @Expose
    String url;
    @Expose
    String text;
}
