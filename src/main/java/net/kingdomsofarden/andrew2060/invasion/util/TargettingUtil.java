package net.kingdomsofarden.andrew2060.invasion.util;

import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityLiving;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
/**
 * Code Shamelessly stolen from aufdemrand/jbudda and Citizens Sentries, used 
 * to target projectiles in a more accurate manner
 *
 */


public class TargettingUtil {
    
    //Projectile targetting functions
    public static Location getFireSource(LivingEntity from, LivingEntity to){

        Location loco =  from.getEyeLocation();
        Vector norman = to.getEyeLocation().subtract(loco).toVector();
        norman = normalizeVector(norman);
        norman.multiply(.5);

        Location loc =loco.add(norman);

        return loc;

    }

    public static Location leadLocation(Location loc, Vector victor, double t){

        return loc.clone().add(victor.clone().multiply(t));

    }


    public static double hangtime(double launchAngle, double v, double elev, double g){



        double a = v * Math.sin(launchAngle);
        double b = -2*g*elev;

        if(Math.pow(a, 2) + b < 0){
            return 0;
        }

        return (a + Math.sqrt(Math.pow(a, 2) + b))  /  g;

    }


    public static Double launchAngle(Location from, Location to, double v, double elev, double g){

        Vector victor = from.clone().subtract(to).toVector();
        Double dist =  Math.sqrt(Math.pow(victor.getX(), 2) + Math.pow(victor.getZ(), 2));

        double v2 = Math.pow(v,2);
        double v4 = Math.pow(v,4);

        double derp =  g*(g*Math.pow(dist,2)+2*elev*v2);




        //Check unhittable.
        if( v4 < derp) {
            //target unreachable
            // use this to fire at optimal max angle launchAngle = Math.atan( ( 2*g*elev + v2) / (2*g*elev + 2*v2));
            return null;
        }
        else {
            //calc angle
            return Math.atan( (v2-   Math.sqrt(v4 - derp))/(g*dist));
        }



    }



    public static Vector normalizeVector(Vector victor){
        double  mag = Math.sqrt(Math.pow(victor.getX(), 2) + Math.pow(victor.getY(), 2)  + Math.pow(victor.getZ(), 2)) ;
        if (mag !=0) return victor.multiply(1/mag);
        return victor.multiply(0);
    }
    
    
    //Face target Utilities
    public static void faceEntity(Entity from, Entity at) {

        if (from.getWorld() != at.getWorld())
            return;
        Location loc = from.getLocation();

        double xDiff = at.getLocation().getX() - loc.getX();
        double yDiff = at.getLocation().getY() - loc.getY();
        double zDiff = at.getLocation().getZ() - loc.getZ();

        double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

        double yaw = (Math.acos(xDiff / distanceXZ) * 180 / Math.PI);
        double pitch = (Math.acos(yDiff / distanceY) * 180 / Math.PI) - 90;
        if (zDiff < 0.0) {
            yaw = yaw + (Math.abs(180 - yaw) * 2);
        }

        look((LivingEntity) from, (float) yaw - 90, (float) pitch);
    }
    
    public static void look(LivingEntity bukkitEntity, float yaw, float pitch) {
        EntityLiving handle = ((CraftLivingEntity)bukkitEntity).getHandle();
        handle.yaw = yaw;
        setHeadYaw(handle, yaw);
        handle.pitch = pitch;
    }

    public static void setHeadYaw(EntityLiving handle, float yaw) {
        while (yaw < -180.0F) {
            yaw += 360.0F;
        }

        while (yaw >= 180.0F) {
            yaw -= 360.0F;
        }
        handle.az = yaw;
        if (!(handle instanceof EntityHuman))
            handle.aN = yaw;
        handle.aP = yaw;
    }


}
