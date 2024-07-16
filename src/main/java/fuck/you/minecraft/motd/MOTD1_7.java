package fuck.you.minecraft.motd;

import com.google.gson.Gson;
import lombok.Builder;

@Builder
public class MOTD1_7 {

    private Version version;
    private PlayerInfo players;
    private String description;
    private String favicon;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
