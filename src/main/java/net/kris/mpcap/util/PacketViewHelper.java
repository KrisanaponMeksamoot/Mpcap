package net.kris.mpcap.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.security.PublicKey;
import java.time.Instant;
import java.util.BitSet;
import java.util.Date;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.Sets;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.kris.mpcap.Mpcap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.registry.RegistryKey;

public class PacketViewHelper {
    protected static HashSet<Class<?>> rawStringShowableClasses = Sets.newHashSet(BlockPos.class,ChunkPos.class,ChunkSectionPos.class,GlobalPos.class,UUID.class,NbtCompound.class,ItemStack.class,BlockHitResult.class);
    protected static HashSet<Class<?>> stringifiableClasses = Sets.newHashSet(String.class,Identifier.class,RegistryKey.class,Date.class,BitSet.class,Optional.class);
    protected static MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
    protected static String namespace = mappingResolver.getCurrentRuntimeNamespace();
    public static String stringView(Packet<?> packet) {
        Class<?> clazz = packet.getClass();
        String className = clazz.getName();
        String out = mappingResolver.mapClassName(namespace, className) + ": {";
        boolean hasPrev = false;
        for (Field field : clazz.getFields()) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            if (!field.trySetAccessible())
                continue;
            if (hasPrev) {
                out += ", ";
            }
            String value = null;
            try {
                Class<?> type = field.getType();
                if (type.isPrimitive()) {
                    Type ptype = field.getGenericType();
                    if (ptype == Boolean.TYPE) {
                        value = Boolean.toString(field.getBoolean(packet));
                    } else if (ptype == Byte.TYPE) {
                        value = (((int)field.getByte(packet))&0xFF)+"B";
                    } else if (ptype == Short.TYPE) {
                        value = field.getShort(packet)+"S";
                    } else if (ptype == Character.TYPE) {
                        value = Character.toString(field.getChar(packet));
                    } else if (ptype == Integer.TYPE) {
                        value = Integer.toString(field.getShort(packet));
                    } else if (ptype == Long.TYPE) {
                        value = field.getShort(packet)+"L";
                    }
                } else {
                    Object fieldValue = field.get(packet);
                    if (type == byte[].class) {
                        value = String.format("[B;%s]",HexFormat.ofDelimiter(",").formatHex((byte[]) field.get(packet)));
                    } else if (type == int[].class) {
                        for (int v : (int[]) fieldValue) {
                            value += v+",";
                        }
                        value = String.format("[I;%*s]",Math.max(value.length()-1,0),value);
                    } else if (type == long[].class) {
                        for (long v : (long[]) fieldValue) {
                            value += v+",";
                        }
                        value = String.format("[L;%*s]",Math.max(value.length()-1,0),value);
                    } else if (rawStringShowableClasses.contains(type)) {
                        value = fieldValue.toString();
                    } else if (stringifiableClasses.contains(type)) {
                        value = String.format("\"%s\"",fieldValue.toString());
                    } else if (Text.class.isAssignableFrom(type)) {
                        value = fieldValue.toString();
                    } else if (Enum.class.isAssignableFrom(type)) {
                        value = fieldValue.toString();
                    } else if (Instant.class.isAssignableFrom(type)) {
                        value = String.format("\"%s\"",fieldValue.toString());
                    } else if (PublicKey.class.isAssignableFrom(type)) {
                        value = fieldValue.toString();
                    } else {
                        Mpcap.LOGGER.warn("Unsupported data type: {}",type.getName());
                        continue;
                    }
                }
            } catch (IllegalAccessException iae) {
                Mpcap.LOGGER.warn(iae.toString());
                continue;
            }
            out += String.format("%s: %s",mappingResolver.mapFieldName(namespace, className, field.getName(), org.objectweb.asm.Type.getDescriptor(field.getType())),value);
            hasPrev = true;
        }
        return out+"}";
    }
}
