package dev.matthiesen.common.cobblemon_escape_rope.utils;

import com.cobblemon.mod.common.ResourcePackActivationBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;

public class ResourcePackDef {
    private final String id;
    private final String name;
    private final PackType packType;
    private final ResourcePackActivationBehaviour activationBehaviour;
    private final Component displayName;

    public ResourcePackDef(
            String id,
            String name,
            PackType packType,
            ResourcePackActivationBehaviour activationBehaviour
    ) {
        this.id = id;
        this.name = name;
        this.packType = packType;
        this.activationBehaviour = activationBehaviour;
        this.displayName = Component.literal(name);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public PackType getPackType() { return packType; }
    public ResourcePackActivationBehaviour getActivationBehaviour() { return activationBehaviour; }
    public Component getDisplayName() { return displayName; }
}