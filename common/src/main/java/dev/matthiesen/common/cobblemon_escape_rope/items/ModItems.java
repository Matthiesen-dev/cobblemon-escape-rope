package dev.matthiesen.common.cobblemon_escape_rope.items;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {
    // Registries
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Constants.MOD_ID, Registries.CREATIVE_MODE_TAB);

    // Creative Tab
    public static final RegistrySupplier<CreativeModeTab> ITEMS_TAB = CREATIVE_TABS.register(
            "escape_rope_items_tab",
            () -> CreativeTabRegistry.create(
                    Component.translatable("category.cobblemon_escape_rope.escape_rope_items_tab"),
                    () -> new ItemStack(ModItems.ESCAPE_ROPE.get())
            )
    );

    // Items
    public static final DeferredSupplier<Item> ESCAPE_ROPE = ITEMS.register("escape_rope", () ->
            new EscapeRopeItem(new Item.Properties()
                    .stacksTo(16)
                    .component(
                            DataComponents.CUSTOM_NAME,
                            Component.translatable("item.cobblemon_escape_rope.escape_rope")
                                .withStyle(style -> style.withColor(ChatFormatting.AQUA).withItalic(false))
                    )
                    .arch$tab(ITEMS_TAB)
            )
    );

    // Init
    public static void init() {
        CREATIVE_TABS.register();
        ITEMS.register();
    }
}
