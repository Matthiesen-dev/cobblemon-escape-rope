package dev.matthiesen.common.cobblemon_escape_rope.registry;

import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import dev.matthiesen.common.cobblemon_escape_rope.items.EscapeRopeItem;
import dev.matthiesen.common.matthiesen_lib.registry.AbstractItemRegistry;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public final class ItemRegistry extends AbstractItemRegistry {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    protected ItemRegistry() {
        super(Constants.MOD_ID);
    }

    public static final Supplier<Item> ESCAPE_ROPE;

    static {
        ESCAPE_ROPE = INSTANCE.register("escape_rope", EscapeRopeItem::new);
    }

    public static void init() {}
}
