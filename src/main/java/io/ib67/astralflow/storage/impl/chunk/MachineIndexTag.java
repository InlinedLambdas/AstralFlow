/*
 *
 *   AstralFlow - The plugin who is turning bukkit into mod-pack
 *   Copyright (C) 2022 iceBear67
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *   USA
 */

package io.ib67.astralflow.storage.impl.chunk;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@ApiStatus.Internal
public final class MachineIndexTag implements PersistentDataType<byte[], ChunkMachineIndex> {
    public static final MachineIndexTag INSTANCE = new MachineIndexTag();
    private static final int STORAGE_VERSION = 0;

    public static void writeEntries(Collection<? extends Map.Entry<Location, String>> collection, ByteBuf buffer) {
        // [typeNameLen] [typeName] [Location]
        for (Map.Entry<Location, String> pair : collection) {
            var loc = pair.getKey();
            var type = pair.getValue();
            buffer.writeInt(type.length());
            buffer.writeBytes(type.getBytes(UTF_8));
            writeLocation(loc, buffer);
        }
    }

    public static void writeLocation(Location loc, ByteBuf buf) {
        // [name len] [name] [x(1 byte)] [y(1b)] [z(1b)]
        var worldName = loc.getWorld().getName();
        buf.writeInt(worldName.length());
        buf.writeBytes(worldName.getBytes(UTF_8));
        buf.writeByte(loc.getBlockX() & 15);
        buf.writeByte(loc.getBlockY());
        buf.writeByte(loc.getBlockZ() & 15);
    }

    public static Location readLocation(int chunkX, int chunkZ, ByteBuf buf) {
        var worldNameLen = buf.readInt();
        var worldName = buf.readBytes(worldNameLen).toString();
        var x = buf.readByte();
        var y = buf.readByte();
        var z = buf.readByte();
        return new Location(Bukkit.getWorld(worldName), (chunkX * 16) + x, y, (chunkZ * 16) + z);
    }

    public static Map<Location, String> readEntries(int chunkX, int chunkZ, int count, ByteBuf buf) {
        var result = new HashMap<Location, String>(count, 2); // avoid-resizing
        for (int i = 1; i < count; i++) {
            var nameLen = buf.readInt();
            var typeName = buf.readBytes(nameLen).toString();
            var loc = readLocation(chunkX, chunkZ, buf);
            result.put(loc, typeName);
        }
        return result;
    }

    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<ChunkMachineIndex> getComplexType() {
        return ChunkMachineIndex.class;
    }

    @NotNull
    @Override
    public byte[] toPrimitive(@NotNull ChunkMachineIndex complex, @NotNull PersistentDataAdapterContext context) {
        /**
         * [version]
         * [chunkX]
         * [chunkZ]
         * [hasMachine]
         * [machineCounts]
         * Machine List...
         * [machineLocation]
         * [machineType]
         */
        var buffer = Unpooled.buffer();
        buffer.writeByte(STORAGE_VERSION);
        buffer.writeInt(complex.chunkX);
        buffer.writeInt(complex.chunkZ);
        buffer.writeBoolean(complex.hasMachines);
        buffer.writeInt(complex.machines.size());
        writeEntries(complex.machines.entrySet(), buffer);
        var result = buffer.array();
        buffer.release();
        return result;
    }

    @NotNull
    @Override
    public ChunkMachineIndex fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {

        var buf = Unpooled.wrappedBuffer(primitive);
        var version = buf.readByte();
        var chunkX = buf.readInt();
        var chunkZ = buf.readInt();
        if (version != STORAGE_VERSION) {
            throw new IllegalArgumentException("Unsupported version: " + version);
        }
        var hasMachines = buf.readBoolean();
        if (!hasMachines) {
            return new ChunkMachineIndex(new HashMap<>(), chunkX, chunkZ);
        }
        var count = buf.readInt();
        var entries = readEntries(chunkX, chunkZ, count, buf);
        buf.release();
        return new ChunkMachineIndex(entries, chunkX, chunkZ);
    }
}