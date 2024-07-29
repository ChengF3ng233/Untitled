package net.minecraft.entity;

import net.minecraft.entity.ai.attributes.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.UUID;

public class SharedMonsterAttributes {
    public static final IAttribute maxHealth = (new RangedAttribute(null, "generic.maxHealth", 20.0D, 0.0D, 1024.0D)).setDescription("Max Health").setShouldWatch(true);
    public static final IAttribute followRange = (new RangedAttribute(null, "generic.followRange", 32.0D, 0.0D, 2048.0D)).setDescription("Follow Range");
    public static final IAttribute knockbackResistance = (new RangedAttribute(null, "generic.knockbackResistance", 0.0D, 0.0D, 1.0D)).setDescription("Knockback Resistance");
    public static final IAttribute movementSpeed = (new RangedAttribute(null, "generic.movementSpeed", 0.699999988079071D, 0.0D, 1024.0D)).setDescription("Movement Speed").setShouldWatch(true);
    public static final IAttribute attackDamage = new RangedAttribute(null, "generic.attackDamage", 2.0D, 0.0D, 2048.0D);
    private static final Logger logger = LogManager.getLogger();

    /**
     * Creates an NBTTagList from a BaseAttributeMap, including all its AttributeInstances
     */
    public static NBTTagList writeBaseAttributeMapToNBT(BaseAttributeMap map) {
        NBTTagList nbttaglist = new NBTTagList();

        for (IAttributeInstance iattributeinstance : map.getAllAttributes()) {
            nbttaglist.appendTag(writeAttributeInstanceToNBT(iattributeinstance));
        }

        return nbttaglist;
    }

    /**
     * Creates an NBTTagCompound from an AttributeInstance, including its AttributeModifiers
     */
    private static NBTTagCompound writeAttributeInstanceToNBT(IAttributeInstance instance) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        IAttribute iattribute = instance.getAttribute();
        nbttagcompound.setString("Name", iattribute.getAttributeUnlocalizedName());
        nbttagcompound.setDouble("Base", instance.getBaseValue());
        Collection<AttributeModifier> collection = instance.func_111122_c();

        if (collection != null && !collection.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();

            for (AttributeModifier attributemodifier : collection) {
                if (attributemodifier.isSaved()) {
                    nbttaglist.appendTag(writeAttributeModifierToNBT(attributemodifier));
                }
            }

            nbttagcompound.setTag("Modifiers", nbttaglist);
        }

        return nbttagcompound;
    }

    /**
     * Creates an NBTTagCompound from an AttributeModifier
     */
    private static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier modifier) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("Name", modifier.getName());
        nbttagcompound.setDouble("Amount", modifier.getAmount());
        nbttagcompound.setInteger("Operation", modifier.getOperation());
        nbttagcompound.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
        nbttagcompound.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());
        return nbttagcompound;
    }

    public static void setAttributeModifiers(BaseAttributeMap map, NBTTagList list) {
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
            IAttributeInstance iattributeinstance = map.getAttributeInstanceByName(nbttagcompound.getString("Name"));

            if (iattributeinstance != null) {
                applyModifiersToAttributeInstance(iattributeinstance, nbttagcompound);
            } else {
                logger.warn("Ignoring unknown attribute '" + nbttagcompound.getString("Name") + "'");
            }
        }
    }

    private static void applyModifiersToAttributeInstance(IAttributeInstance instance, NBTTagCompound compound) {
        instance.setBaseValue(compound.getDouble("Base"));

        if (compound.hasKey("Modifiers", 9)) {
            NBTTagList nbttaglist = compound.getTagList("Modifiers", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                AttributeModifier attributemodifier = readAttributeModifierFromNBT(nbttaglist.getCompoundTagAt(i));

                if (attributemodifier != null) {
                    AttributeModifier attributemodifier1 = instance.getModifier(attributemodifier.getID());

                    if (attributemodifier1 != null) {
                        instance.removeModifier(attributemodifier1);
                    }

                    instance.applyModifier(attributemodifier);
                }
            }
        }
    }

    /**
     * Creates an AttributeModifier from an NBTTagCompound
     */
    public static AttributeModifier readAttributeModifierFromNBT(NBTTagCompound compound) {
        UUID uuid = new UUID(compound.getLong("UUIDMost"), compound.getLong("UUIDLeast"));

        try {
            return new AttributeModifier(uuid, compound.getString("Name"), compound.getDouble("Amount"), compound.getInteger("Operation"));
        } catch (Exception exception) {
            logger.warn("Unable to create attribute: " + exception.getMessage());
            return null;
        }
    }
}
