package dev.matthiesen.common.cobblemon_escape_rope.items;

import com.cobblemon.mod.common.CobblemonSounds;
import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.data.PlayerCoordsData;
import dev.matthiesen.common.cobblemon_escape_rope.utils.DataUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class EscapeRopeItem extends Item {
    private static int cooldownTicks() { return 20 * CobblemonEscapeRope.config.escapeRopeItemConfig.cooldownInSeconds; }
    private static int useDuration() { return 20 * CobblemonEscapeRope.config.escapeRopeItemConfig.useTimeInSeconds; }
    private static int teleportSearchRadius() { return CobblemonEscapeRope.config.escapeRopeItemConfig.teleportSafeSearchRadius; }

    public EscapeRopeItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return useDuration();
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide && entity instanceof Player player) {
            ServerLevel serverLevel = (ServerLevel) level;
            int elapsed = useDuration() - remainingUseDuration;
            float progress = (float) elapsed / useDuration();

            // Spiral Particles
            double angle = elapsed * 0.5;
            double xOff = Math.cos(angle) * 0.6;
            double zOff = Math.sin(angle) * 0.6;
            serverLevel.sendParticles(ParticleTypes.PORTAL, player.getX() + xOff, player.getY() + 0.5, player.getZ() + zOff, 1, 0, 0, 0, 0);

            // Charging sound
            if (elapsed % 10 == 0) {
                level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5f, 0.5f + (progress));
            }
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            String currentDim = level.dimension().location().toString();

            if (CobblemonEscapeRope.isDimensionBlacklisted(currentDim)) {
                player.displayClientMessage(Component.translatable("cobblemon_escape_rope.msg.dimension_blacklisted").withStyle(ChatFormatting.RED), true);
                level.playSound(null, player.blockPosition(), CobblemonSounds.POKE_BALL_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                return stack;
            }

            if (level.canSeeSky(player.blockPosition())) {
                player.displayClientMessage(Component.translatable("cobblemon_escape_rope.msg.already_can_see_sky").withStyle(ChatFormatting.YELLOW), true);
                level.playSound(null, player.blockPosition(), CobblemonSounds.POKE_BALL_HIT, SoundSource.PLAYERS, 1.0F, 1.0F);
                player.getCooldowns().addCooldown(this, useDuration());
                return stack;
            }

            PlayerCoordsData.DataStoreEntry data = DataUtil.getSavedPlayerData(serverPlayer);


            if (!data.dimension.isEmpty() && data.dimension.equals(currentDim)) {
                BlockPos safeTarget = findSafeTeleportTarget(level, serverPlayer, data.pos);
                if (safeTarget == null) {
                    player.displayClientMessage(Component.translatable("cobblemon_escape_rope.msg.no_safe_destination").withStyle(ChatFormatting.RED), true);
                    return stack;
                }

                double x = safeTarget.getX() + 0.5;
                double y = safeTarget.getY();
                double z = safeTarget.getZ() + 0.5;

                serverPlayer.teleportTo(x, y, z);
                level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

                DataUtil.setCooldown(serverPlayer, cooldownTicks());
                player.getCooldowns().addCooldown(this, cooldownTicks());

                if (!player.getAbilities().instabuild &&
                        CobblemonEscapeRope.config.escapeRopeItemConfig.consumeOnUse) stack.shrink(1);
            } else {
                String error = !level.dimensionType().hasSkyLight()
                        ? "cobblemon_escape_rope.msg.wrong_dimension"
                        : "cobblemon_escape_rope.msg.can_not_see_sky";
                player.displayClientMessage(Component.translatable(error).withStyle(ChatFormatting.RED), true);
            }
        }
        return stack;
    }

    private BlockPos findSafeTeleportTarget(Level level, ServerPlayer serverPlayer, BlockPos expectedPos) {
        int radius = teleportSearchRadius();
        for (int distance = 0; distance <= radius * radius * 2; distance++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if ((dx * dx) + (dz * dz) != distance) {
                        continue;
                    }

                    BlockPos candidate = expectedPos.offset(dx, 0, dz);
                    if (isSafeTeleportTarget(level, serverPlayer, candidate)) {
                        return candidate;
                    }
                }
            }
        }

        return null;
    }

    private boolean isSafeTeleportTarget(Level level, ServerPlayer serverPlayer, BlockPos targetPos) {
        BlockPos floorPos = targetPos.below();
        if (!level.getBlockState(floorPos).isFaceSturdy(level, floorPos, Direction.UP)) {
            return false;
        }

        var standingBox = serverPlayer.getDimensions(Pose.STANDING)
                .makeBoundingBox(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
        return level.noBlockCollision(serverPlayer, standingBox);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("cobblemon_escape_rope.tooltip.description").withStyle(ChatFormatting.GRAY));

        int remainingTicks = getRemainingCooldownTicks();
        if (remainingTicks > 0) {
            tooltip.add(Component.translatable("cobblemon_escape_rope.tooltip.cooldown_remaining", formatCooldownTime(remainingTicks)).withStyle(ChatFormatting.RED));
            return;
        }

        tooltip.add(Component.translatable("cobblemon_escape_rope.tooltip.cooldown_ready").withStyle(ChatFormatting.GREEN));
    }

    private int getRemainingCooldownTicks() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.player.getCooldowns().isOnCooldown(this)) {
            return 0;
        }

        float percent = minecraft.player.getCooldowns().getCooldownPercent(this, 0.0F);
        return Math.max(1, (int) Math.ceil(percent * cooldownTicks()));
    }

    private String formatCooldownTime(int ticks) {
        int totalSeconds = (int) Math.ceil(ticks / 20.0);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format(Locale.ROOT, "%d:%02d", minutes, seconds);
    }
}
