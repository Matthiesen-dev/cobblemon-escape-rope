package dev.matthiesen.cobblemon_escape_rope.common.registry;

import dev.matthiesen.cobblemon_escape_rope.common.CobblemonEscapeRope;
import dev.matthiesen.matthiesen_core.common.registry.AbstractCreativeModeTabRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public final class CreativeTabRegistry extends AbstractCreativeModeTabRegistry {
    private static final CreativeTabRegistry INSTANCE = new CreativeTabRegistry();
    public static final ResourceKey<CreativeModeTab> TOOLS_AND_UTILITIES = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            ResourceLocation.withDefaultNamespace("tools_and_utilities"));

    private CreativeTabRegistry() {
        super(CobblemonEscapeRope.MOD_ID);
    }

    public static void initialize() {
        INSTANCE.registerTabItemAugmentation(TOOLS_AND_UTILITIES, ItemRegistry.ESCAPE_ROPE);
    }

    public static final Supplier<CreativeModeTab> ESCAPE_ROPE_ITEMS_TAB;

    static {
        ESCAPE_ROPE_ITEMS_TAB = INSTANCE.register("escape_rope_items_tab", () -> INSTANCE.getRegistryBuilder()
                .newCreativeTabBuilder()
                .title(Component.translatable("category.cobblemon_escape_rope.escape_rope_items_tab"))
                .icon(() -> ItemRegistry.ESCAPE_ROPE.get().getDefaultInstance())
                .displayItems(((itemDisplayParameters, output) ->
                        output.accept(ItemRegistry.ESCAPE_ROPE.get())))
                .build()
        );
    }
}
