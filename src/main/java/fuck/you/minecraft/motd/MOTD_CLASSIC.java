package fuck.you.minecraft.motd;

import com.google.gson.Gson;
import lombok.Builder;

@Builder
public class MOTD_CLASSIC {

    private Version version;
    private PlayerInfo players;
    private String description;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
