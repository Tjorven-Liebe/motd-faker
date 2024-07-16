package fuck.you.minecraft.motd;

import com.google.gson.Gson;
import lombok.Builder;

@Builder
public class MOTD1_16 {

    private Version version;
    private PlayerInfo players;
    private String description;
    private String favicon;
    private boolean enforcesSecureChat;
    private boolean previewsChat;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
