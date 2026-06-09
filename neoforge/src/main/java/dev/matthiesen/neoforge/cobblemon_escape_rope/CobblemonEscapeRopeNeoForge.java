package dev.matthiesen.neoforge.cobblemon_escape_rope;

import com.cobblemon.mod.common.ResourcePackActivationBehaviour;
import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.KnownPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.Optional;

@Mod(Constants.MOD_ID)
public final class CobblemonEscapeRopeNeoForge {
    public CobblemonEscapeRopeNeoForge(IEventBus modBus) {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        CobblemonEscapeRope.initialize();
        modBus.register(this);
    }

    @SubscribeEvent
    public void onAddPackFindersEvent(AddPackFindersEvent event) {
        var optionalModContainer = ModList.get().getModContainerById(Constants.MOD_ID);
        if (optionalModContainer.isEmpty()) {
            Constants.createInfoLog("Could not find mod container for mod id: " + Constants.MOD_ID);
            return;
        }

        var modFile = optionalModContainer.get().getModInfo();
        CobblemonEscapeRope.BuiltInResourcePacks
                .stream().filter(rp -> rp.getPackType() == event.getPackType())
                .forEach(rp -> {
                    var subPath = (rp.getPackType() == PackType.CLIENT_RESOURCES ? "resourcepacks" : "datapacks");
                    var packLocation = Constants.modResource(subPath + "/" + rp.getId());
                    var resourcePath = modFile.getOwningFile().getFile().findResource(packLocation.getPath());

                    var version = modFile.getVersion();

                    var pack = Pack.readMetaAndCreate(
                            new PackLocationInfo(
                                    "mod/" + packLocation,
                                    rp.getDisplayName(),
                                    PackSource.BUILT_IN,
                                    Optional.of(new KnownPack("neoforge", "mod/$packLocation", version.toString()))
                            ),
                            BuiltInPackSource.fromName((info) -> new PathPackResources(info, resourcePath)),
                            rp.getPackType(),
                            new PackSelectionConfig(
                                    rp.getActivationBehaviour() == ResourcePackActivationBehaviour.ALWAYS_ENABLED,
                                    Pack.Position.TOP,
                                    false
                            )
                    );

                    if (pack == null) {
                        Constants.createInfoLog("Failed to load built-in resource pack: " + rp.getId());
                        return;
                    }

                    event.addRepositorySource(it -> it.accept(pack));
                });
    }
}
