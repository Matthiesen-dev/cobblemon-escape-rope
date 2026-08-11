package dev.matthiesen.cobblemon_escape_rope.common.registry;

import dev.matthiesen.cobblemon_escape_rope.common.CobblemonEscapeRope;
import dev.matthiesen.cobblemon_escape_rope.common.item.EscapeRopeItem;
import dev.matthiesen.matthiesen_core.common.registry.AbstractItemRegistry;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public final class ItemRegistry extends AbstractItemRegistry {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    private ItemRegistry() {
        super(CobblemonEscapeRope.MOD_ID);
    }

    public static void initialize() {}

    public static final Supplier<Item> ESCAPE_ROPE;

    static {
        ESCAPE_ROPE = INSTANCE.register("escape_rope", EscapeRopeItem::new);
    }
}
