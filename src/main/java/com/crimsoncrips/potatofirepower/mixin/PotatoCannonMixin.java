package com.crimsoncrips.potatofirepower.mixin;

import com.crimsoncrips.potatofirepower.ReflectionUtility;
import com.crimsoncrips.potatofirepower.config.PPConfig;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.potatoCannon.*;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.function.Predicate;


@Mixin(PotatoCannonItem.class)
public class PotatoCannonMixin extends ProjectileWeaponItem implements CustomArmPoseItem {


    public PotatoCannonMixin(Properties p_43009_) {
        super(p_43009_);

    }
    private Optional<ItemStack> findAmmoInInventory(Level world, Player player, ItemStack held) {
        ItemStack findAmmo = player.getProjectile(held);
        return PotatoProjectileTypeManager.getTypeForStack(findAmmo).map(($) -> {
            return findAmmo;
        });
    }
    private int maxUses() {
        return (Integer) AllConfigs.server().equipment.maxPotatoCannonShots.get();
    }
    public boolean isCannon(ItemStack stack) {
        return stack.getItem() instanceof PotatoCannonItem;
    }




    public InteractionResultHolder use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return (InteractionResultHolder)this.findAmmoInInventory(world, player, stack).map((itemStack) -> {
            if (ShootableGadgetItemMethods.shouldSwap(player, stack, hand, this::isCannon)) {
                return InteractionResultHolder.fail(stack);
            } else if (world.isClientSide) {
                CreateClient.POTATO_CANNON_RENDER_HANDLER.dontAnimateItem(hand);
                return InteractionResultHolder.success(stack);
            } else {
                Vec3 barrelPos = ShootableGadgetItemMethods.getGunBarrelVec(player, hand == InteractionHand.MAIN_HAND, new Vec3(0.75, -0.15000000596046448, 1.5));
                Vec3 correction = ShootableGadgetItemMethods.getGunBarrelVec(player, hand == InteractionHand.MAIN_HAND, new Vec3(-0.05000000074505806, 0.0, 0.0)).subtract(player.position().add(0.0, (double)player.getEyeHeight(), 0.0));
                PotatoCannonProjectileType projectileType = PotatoProjectileTypeManager.getTypeForStack(itemStack).orElse(BuiltinPotatoProjectileTypes.FALLBACK);
                Vec3 lookVec = player.getLookAngle();
                Vec3 motion = lookVec.add(correction).normalize().scale(2.0).scale((double)projectileType.getVelocityMultiplier());
                float soundPitch = projectileType.getSoundPitch() + (Create.RANDOM.nextFloat() - 0.5F) / 4.0F;
                boolean spray = projectileType.getSplit() > 1;
                Vec3 sprayBase = VecHelper.rotate(new Vec3(0.0, 0.1, 0.0), (double)(360.0F * Create.RANDOM.nextFloat()), Direction.Axis.Z);
                float sprayChange = 360.0F / (float)projectileType.getSplit();

                for(int i = 0; i < projectileType.getSplit(); ++i) {
                    PotatoProjectileEntity projectile = AllEntityTypes.POTATO_PROJECTILE.create(world);
                    projectile.setItem(itemStack);
                    projectile.setEnchantmentEffectsFromCannon(stack);
                    Vec3 splitMotion = motion;
                    if (spray) {
                        float imperfection = 40.0F * (Create.RANDOM.nextFloat() - 0.5F);
                        Vec3 sprayOffset = VecHelper.rotate(sprayBase, (double)((float)i * sprayChange + imperfection), Direction.Axis.Z);
                        splitMotion = splitMotion.add(VecHelper.lookAt(sprayOffset , motion));
                    }

                    if (i != 0) {
                        ReflectionUtility.setField(projectile, "recoveryChance", 0.0F);
                    }
                    projectile.setPos(barrelPos.x, barrelPos.y, barrelPos.z);
                    projectile.setDeltaMovement(splitMotion);
                    projectile.setOwner(player);

                    world.addFreshEntity(projectile);

                }

                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    if (itemStack.isEmpty()) {
                        player.getInventory().removeItem(itemStack);
                    }
                }

                if (!BacktankUtil.canAbsorbDamage(player, this.maxUses())) {
                    stack.hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(hand);
                    });
                }

                Integer cooldown = this.findAmmoInInventory(world, player, stack).flatMap(PotatoProjectileTypeManager::getTypeForStack).map(PotatoCannonProjectileType::getReloadTicks).orElse(PPConfig.FIRE_DELAY);
                ShootableGadgetItemMethods.applyCooldown(player, stack, hand, this::isCannon, cooldown);
                ShootableGadgetItemMethods.sendPackets(player, (b) -> {
                    return new PotatoCannonPacket(barrelPos, lookVec.normalize(), itemStack, hand, soundPitch, b);
                });
                return InteractionResultHolder.success(stack);
            }
        }).orElse(InteractionResultHolder.pass(stack));
    }

    @Override
    @Shadow
    public HumanoidModel.@Nullable ArmPose getArmPose(ItemStack itemStack, AbstractClientPlayer abstractClientPlayer, InteractionHand interactionHand) {
        return null;
    }

    @Override
    @Shadow
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return null;
    }

    @Override
    @Shadow
    public int getDefaultProjectileRange() {
        return 0;
    }
}

