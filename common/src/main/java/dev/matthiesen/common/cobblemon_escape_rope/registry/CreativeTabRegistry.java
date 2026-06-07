package dev.matthiesen.common.cobblemon_escape_rope.registry;

import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import dev.matthiesen.common.matthiesen_lib.registry.AbstractCreativeModeTabRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class CreativeTabRegistry extends AbstractCreativeModeTabRegistry {
    private static final CreativeTabRegistry INSTANCE = new CreativeTabRegistry();

    protected CreativeTabRegistry() {
        super(Constants.MOD_ID);
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

    public static void init() {}
}
