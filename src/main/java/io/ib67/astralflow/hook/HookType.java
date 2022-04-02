/*
 *
 *   AstralFlow - The plugin enriches bukkit servers
 *   Copyright (C) 2022 The Inlined Lambdas and Contributors
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

package io.ib67.astralflow.hook;

import io.ib67.astralflow.AstralFlow;
import io.ib67.astralflow.hook.event.machine.MachineBreakEvent;
import io.ib67.astralflow.hook.event.machine.MachinePlaceEvent;
import io.ib67.astralflow.hook.event.server.SaveDataEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.function.Consumer;

// Constants. 提供一个统一一的监听系统
@SuppressWarnings("unused")
public final class HookType<T> {
    public static final HookType<?> PLUGIN_SHUTDOWN = new HookType<>();
    public static final HookType<SaveDataEvent> SAVE_DATA = new HookType<>();
    public static final HookType<?> ASTRALFLOW_STARTUP_COMPLETED = new HookType<>();

    public static final HookType<PlayerItemConsumeEvent> ITEM_CONSUME = new HookType<>();
    public static final HookType<PlayerItemDamageEvent> ITEM_DAMAGE = new HookType<>();
    public static final HookType<PlayerInteractEvent> ITEM_USE = new HookType<>();
    public static final HookType<PlayerInteractEvent> PLAYER_INTERACT = new HookType<>();
    public static final HookType<PlayerInteractEntityEvent> PLAYER_INTERACT_ENTITY = new HookType<>();
    public static final HookType<PlayerInteractEvent> PLAYER_INTERACT_BLOCK = new HookType<>();
    public static final HookType<PlayerItemBreakEvent> ITEM_BROKEN = new HookType<>();

    // For chunks
    public static final HookType<ChunkLoadEvent> CHUNK_LOAD = new HookType<>();
    public static final HookType<ChunkUnloadEvent> CHUNK_UNLOAD = new HookType<>();

    // For machines
    public static final HookType<MachineBreakEvent> MACHINE_BREAK = new HookType<>();
    public static final HookType<MachinePlaceEvent> MACHINE_PLACE = new HookType<>();

    public static final HookType<EntityDamageByEntityEvent> ENTITY_DAMAGE = new HookType<>();
    public static final HookType<AsyncPlayerChatEvent> PLAYER_CHAT = new HookType<>();
    public static final HookType<PlayerMoveEvent> PLAYER_MOVE = new HookType<>();

    // For blocks
    public static final HookType<BlockBreakEvent> BLOCK_BREAK = new HookType<>();
    public static final HookType<BlockPlaceEvent> BLOCK_PLACE = new HookType<>();

    public void register(Consumer<T> acceptor) {
        AstralFlow.getInstance().addHook(this, acceptor);
    }

    public void register(Runnable acceptor) {
        AstralFlow.getInstance().addHook(this, t -> acceptor.run());
    }
}
