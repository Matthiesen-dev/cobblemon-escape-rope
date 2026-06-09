package dev.matthiesen.fabric.cobblemon_escape_rope;

import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.PackType;

@SuppressWarnings("UnstableApiUsage")
public final class CobblemonEscapeRopeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        CobblemonEscapeRope.initialize();

        CobblemonEscapeRope.BuiltInResourcePacks
                .forEach(rp -> {
                    Constants.createInfoLog("Registering built-in resource pack: " + rp.displayName());
                    var optionalModContainer = FabricLoader.getInstance().getModContainer(Constants.MOD_ID);
                    if (optionalModContainer.isEmpty()) {
                        Constants.createErrorLog("Couldn't find mod container for mod id: " + Constants.MOD_ID);
                        return;
                    }
                    var mod = optionalModContainer.get();
                    var resourcePackActivationType = switch (rp.activationBehaviour()) {
                        case NORMAL -> ResourcePackActivationType.NORMAL;
                        case DEFAULT_ENABLED -> ResourcePackActivationType.DEFAULT_ENABLED;
                        case ALWAYS_ENABLED -> ResourcePackActivationType.ALWAYS_ENABLED;
                    };
                    var id = Constants.modResource(rp.id());
                    String subPath = (rp.packType() == PackType.CLIENT_RESOURCES ? "resourcepacks" : "datapacks") + "/" + id.getPath();

                    ResourceManagerHelperImpl.registerBuiltinResourcePack(id, subPath, mod, rp.displayName(), resourcePackActivationType);
                });
    }

}
