package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.IBlockAccess;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;

import java.util.UUID;

public class GoalBreakDoor extends GoalDoorInteract {
    private int i;
    private int j = -1;

    public GoalBreakDoor(EntityInsentient entityinsentient) {
        super(entityinsentient);
    }

    @Override
    public GoalType getType() {
        return GoalType.ZERO;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("3dd56fb0-258a-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        return !super.shouldStart() ? false :
                (!this.a.world.getGameRules().getBoolean("mobGriefing") ? false :
                !this.e.f((IBlockAccess) this.a.world, this.b, this.c, this.d));
                // CraftBukkit - Fix decompilation issue by casting world to IBlockAccess
    }

    @Override
    public void start() {
        super.start();
        this.i = 0;
    }

    @Override
    public boolean shouldContinue() {
        double d0 = this.a.e((double) this.b, (double) this.c, (double) this.d);

        return this.i <= 240 && !this.e.f((IBlockAccess) this.a.world, this.b, this.c, this.d) && d0 < 4.0D; // CraftBukkit - Fix decompilation issue by casting world to IBlockAccess
    }

    @Override
    public void finish() {
        super.finish();
        this.a.world.d(this.a.getId(), this.b, this.c, this.d, -1);
    }

    public void tick() {
        super.tick();
        if (this.a.aI().nextInt(20) == 0) {
            this.a.world.triggerEffect(1010, this.b, this.c, this.d, 0);
        }

        ++this.i;
        int i = (int) ((float) this.i / 240.0F * 10.0F);

        if (i != this.j) {
            this.a.world.d(this.a.getId(), this.b, this.c, this.d, i);
            this.j = i;
        }

        if (this.i == 240) {
            // CraftBukkit start
            if (CraftEventFactory.callEntityBreakDoorEvent(this.a, this.b, this.c, this.d).isCancelled()) {
                this.start();
                return;
            }
            // CraftBukkit end

            this.a.world.setAir(this.b, this.c, this.d);
            this.a.world.triggerEffect(1012, this.b, this.c, this.d, 0);
            this.a.world.triggerEffect(2001, this.b, this.c, this.d, Block.getId(this.e));
        }
    }
}
