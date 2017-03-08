package com.jayavery.jjmod.entities.projectile;

import com.jayavery.jjmod.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityArrowSteel extends EntityProjectile {

    /** Damager done by this arrow. */
    private static final double DAMAGE = 1.77;
    
    public EntityArrowSteel(World world) {

        super(world, DAMAGE);
    }

    public EntityArrowSteel(World world, double x, double y, double z) {

        super(world, x, y, z, DAMAGE);
    }

    public EntityArrowSteel(World world, EntityLivingBase shooter) {

        super(world, shooter, DAMAGE);
    }

    @Override
    protected ItemStack getArrowStack() {

        return new ItemStack(ModItems.arrowSteel);
    }
}