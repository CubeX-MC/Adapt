package com.volmit.adapt.content.adaptation.stealth;

import com.volmit.adapt.api.adaptation.SimpleAdaptation;
import com.volmit.adapt.util.C;
import com.volmit.adapt.util.Element;
import com.volmit.adapt.util.Form;
import com.volmit.adapt.util.J;
import com.volmit.adapt.util.KList;
import com.volmit.adapt.util.M;
import com.volmit.adapt.util.VectorMath;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Collection;

public class GhostArmor extends SimpleAdaptation<GhostArmor.Config> {
    public GhostArmor() {
        super("ghost-armor");
        registerConfiguration(Config.class);
        setDescription("Slow building armor when not taking damage");
        setIcon(Material.NETHERITE_CHESTPLATE);
        setInterval(5353);
        setBaseCost(getConfig().baseCost);
        setInitialCost(getConfig().initialCost);
        setCostFactor(getConfig().costFactor);
        setMaxLevel(getConfig().maxLevel);
    }

    @Override
    public void addStats(int level, Element v) {
        v.addLore(C.GREEN + "+ " + Form.f(getMaxArmorPoints(getLevelPercent(level)), 0) + " Max Armor");
        v.addLore(C.GREEN + "+ " + Form.f(getMaxArmorPerTick(getLevelPercent(level)), 1) + " Speed");
    }

    public double getMaxArmorPoints(double factor) {
        return M.lerp(getConfig().minArmor, getConfig().maxArmor, factor);
    }

    public double getMaxArmorPerTick(double factor) {
        return M.lerp(getConfig().minArmorPerTick, getConfig().maxArmorPerTick, factor);
    }

    @Override
    public void onTick() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(!hasAdaptation(p)){
                Collection<AttributeModifier> c = p.getAttribute(Attribute.GENERIC_ARMOR).getModifiers();
                for (AttributeModifier i : new KList<>(c)) {
                    if(i.getName().equals("adapt-ghost-armor")) {
                        p.getAttribute(Attribute.GENERIC_ARMOR).removeModifier(i);
                    }
                }
                continue;
            }
            double oldArmor = 0;
            double armor = getMaxArmorPoints(getLevelPercent(p));
            armor = Double.isNaN(armor) ? 0 : armor;



            if(oldArmor < armor)
            {Collection<AttributeModifier> c = p.getAttribute(Attribute.GENERIC_ARMOR).getModifiers();
                for (AttributeModifier i : new KList<>(c)) {
                    if(i.getName().equals("adapt-ghost-armor")) {
                        oldArmor = i.getAmount();
                        oldArmor = Double.isNaN(oldArmor) ? 0 : oldArmor;
                        p.getAttribute(Attribute.GENERIC_ARMOR).removeModifier(i);
                    }
                }
                p.getAttribute(Attribute.GENERIC_ARMOR)
                    .addModifier(new AttributeModifier("adapt-ghost-armor", Math.min(armor, oldArmor+getMaxArmorPerTick(getLevelPercent(p))), AttributeModifier.Operation.ADD_NUMBER));
            }

            else if(oldArmor > armor)
            {Collection<AttributeModifier> c = p.getAttribute(Attribute.GENERIC_ARMOR).getModifiers();
                for (AttributeModifier i : new KList<>(c)) {
                    if(i.getName().equals("adapt-ghost-armor")) {
                        oldArmor = i.getAmount();
                        oldArmor = Double.isNaN(oldArmor) ? 0 : oldArmor;
                        p.getAttribute(Attribute.GENERIC_ARMOR).removeModifier(i);
                    }
                }
                p.getAttribute(Attribute.GENERIC_ARMOR)
                    .addModifier(new AttributeModifier("adapt-ghost-armor", armor, AttributeModifier.Operation.ADD_NUMBER));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Player p && hasAdaptation(p) && !e.isCancelled() && e.getDamage() > 0)
        {
            J.s(() -> {
                Collection<AttributeModifier> c = p.getAttribute(Attribute.GENERIC_ARMOR).getModifiers();
                for (AttributeModifier i : new KList<>(c)) {
                    if(i.getName().equals("adapt-ghost-armor")) {
                        p.getAttribute(Attribute.GENERIC_ARMOR).removeModifier(i);
                    }
                }
            });
        }
    }

    @Override
    public boolean isEnabled() {
        return getConfig().enabled;
    }

    @NoArgsConstructor
    protected static class Config {
        boolean enabled = true;
        int baseCost = 3;
        int maxArmor = 16;
        int minArmor = 2;
        int maxArmorPerTick = 3;
        int minArmorPerTick = 1;
        int initialCost = 1;
        double costFactor = 0.335;
        int maxLevel = 7;
    }
}
