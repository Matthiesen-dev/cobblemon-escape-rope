package dev.matthiesen.common.cobblemon_escape_rope.data;

import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class CoordsHelper {
    private static final String DATA_ID = Constants.MOD_ID + "_player_coords";

    public static PlayerCoordsData get(MinecraftServer server) {
        DimensionDataStorage storage = server.overworld().getDataStorage();
        SavedData.Factory<PlayerCoordsData> factory = new SavedData.Factory<>(
                PlayerCoordsData::new,
                PlayerCoordsData::load,
                null
        );
        return storage.computeIfAbsent(factory, DATA_ID);
    }
}
