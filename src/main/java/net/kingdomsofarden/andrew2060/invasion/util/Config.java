package net.kingdomsofarden.andrew2060.invasion.util;

public class Config {
    public static double GROWTH_PER_SQUARED_NORMAL = 25000000;
    public static double GROWTH_PER_SQUARED_NETHER = 390625D;
    public static double GROWTH_RATE_HEALTH = Math.sqrt(1 + 1/3D);
    public static double GROWTH_RATE_EXP = Math.sqrt(1.15);
    public static double GROWTH_RATE_DMG = Math.sqrt(1.2);

    public static double ELITE_MOB_CHANCE = 0.005;

    public static String[] MOB_NAMES = new String[] {
        "Zealot","Herald","Impairor",
        "Inquisitor","Tormentor","Crucifier",
        "Sentinel","Absolution","Anathema",
        "Abaddon","Redeemer","Archon",
        "Sacrilege","Devoter","Templar",
        "Avatar", "Cherubis","Nephilis",
        "Seraphis", "Harbinger","Dominion",
    };
    
    public static double PVP_EXP_BASE = 5000;
    public static double PVP_EXP_PER_LEVEL_DIFF = 50;
}
