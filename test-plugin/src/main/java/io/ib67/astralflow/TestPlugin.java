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

package io.ib67.astralflow;

import com.google.gson.GsonBuilder;
import io.ib67.astralflow.item.AnotherSimpleState;
import io.ib67.astralflow.util.LogCategory;
import io.ib67.util.bukkit.Log;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

public final class TestPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        new TestModule();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("stateless_item")) {
            if (!(sender instanceof Player)) return false;
            var player = (Player) sender;
            player.getInventory().addItem(TestItems.STATELESS_ITEM.createNewItem().asItemStack());
        } else if (label.equalsIgnoreCase("random_state_item")) {
            if (!(sender instanceof Player)) return false;
            var player = (Player) sender;
            var item = TestItems.STATEFUL_ITEM.createNewItem();
            var simpleState = item.getState().map(e -> (AnotherSimpleState) e).orElseThrow();
            simpleState.setData(UUID.randomUUID().toString());
            Log.info("testplug", "Created state: " + new GsonBuilder().setPrettyPrinting().create().toJson(simpleState));
            item.saveState(simpleState);
            player.getInventory().addItem(item.asItemStack());
        } else if (label.equalsIgnoreCase("lookup_simple_state")) {
            if (!(sender instanceof Player)) return false;
            var player = (Player) sender;
            var itemInHand = Objects.requireNonNull(player.getEquipment()).getItemInMainHand();
            if (itemInHand.getType() == Material.AIR) return false;
            var simpleState = AstralFlow.getInstance().getItemRegistry().getState(itemInHand);
            if (simpleState == null) return false;
            player.sendMessage(new GsonBuilder().setPrettyPrinting().create().toJson(simpleState));
        } else if (label.equalsIgnoreCase("jbwool")) {
            if (!(sender instanceof Player)) return false;
            var player = (Player) sender;
            player.getInventory().addItem(TestItems.JEB_WOOL.createNewItem().asItemStack());
        } else if (label.equalsIgnoreCase("loc")) {
            if (!(sender instanceof Player)) return false;
            var player = (Player) sender;
            var chunk = player.getLocation().getChunk();
            player.sendMessage(String.valueOf(chunk.getX()));
            player.sendMessage(String.valueOf(chunk.getZ()));
            player.sendMessage(String.valueOf(player.getLocation().getBlockX()));
            player.sendMessage(String.valueOf(player.getLocation().getBlockZ()));

            var loc = player.getLocation();
            var offsetX = loc.getBlockX() >= 0 ? loc.getBlockX() & 15 : 16 + (loc.getBlockX() % 16);
            var offsetZ = loc.getBlockZ() >= 0 ? loc.getBlockZ() & 15 : loc.getBlockZ() % 16 == 0 ? 0 : 16 + (loc.getBlockZ() % 16);
            player.sendMessage("Offset X: " + offsetX + " Offset Z: " + offsetZ);
            var resultX = chunk.getX() * 16 + offsetX;
            var resultZ = chunk.getZ() * 16 + offsetZ;
            player.sendMessage("Infer: " + "x: " + resultX + ChatColor.GRAY + loc.getBlockX() + ChatColor.RESET + " y: " + resultZ + ChatColor.GRAY + loc.getBlockZ());
        }
        return true;
    }
}
