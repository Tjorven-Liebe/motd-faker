package fuck.you.minecraft.motd;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerInfo {

    private int max;
    private int online;
    private SampleInfo[] sample;

}
