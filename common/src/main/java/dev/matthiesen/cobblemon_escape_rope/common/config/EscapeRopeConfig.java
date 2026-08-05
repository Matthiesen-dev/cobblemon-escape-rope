package dev.matthiesen.cobblemon_escape_rope.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class EscapeRopeConfig {
    public static final ServerConfig SERVER_CONFIG;
    public static final ModConfigSpec SERVER_SPEC;

//    public static final ClientConfig CLIENT_CONFIG;
//    public static final ModConfigSpec CLIENT_SPEC;

    static {
        Pair<ServerConfig, ModConfigSpec> serverPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_CONFIG = serverPair.getLeft();
        SERVER_SPEC = serverPair.getRight();

//        Pair<ClientConfig, ModConfigSpec> clientPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
//        CLIENT_CONFIG = clientPair.getLeft();
//        CLIENT_SPEC = clientPair.getRight();
    }
}
