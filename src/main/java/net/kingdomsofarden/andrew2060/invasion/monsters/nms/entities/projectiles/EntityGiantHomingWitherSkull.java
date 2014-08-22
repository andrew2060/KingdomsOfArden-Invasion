package net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.projectiles;

import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.util.Vector;

public class EntityGiantHomingWitherSkull extends EntityGiantWitherSkull {

    private EntityLiving target;
    private int updateTicks;
    public EntityGiantHomingWitherSkull(World world, EntityLiving entityliving, double d0, double d1, double d2,
                                        int tier, EntityLiving target) {
        super(world, entityliving, d0, d1, d2, tier);
        this.target = target;
    }

    public EntityGiantHomingWitherSkull(World world) {
        super(world);
        this.die();
    }

    @Override
    public void h() {
        if (!(updateTicks % 10 == 9)) {
            this.world.createExplosion(this, this.locX, this.locY, this.locZ, 0F, false, false);
            this.updateTicks++;
            super.h();
            return;
        }
        this.updateTicks = 0;
        this.world.createExplosion(this, this.locX, this.locY, this.locZ, 0F, false, false);
        Vector v = new Vector(this.target.locX, this.target.locY, this.target.locZ)
                .subtract(new Vector(this.locX, this.locY, this.locZ)).normalize().multiply(1.9);
        this.motX = v.getX();
        this.motY = v.getY();
        this.motZ = v.getZ();
        this.velocityChanged = true;
        super.h();
        return;
    }
}
