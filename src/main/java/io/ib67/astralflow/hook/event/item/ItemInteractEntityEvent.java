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

package io.ib67.astralflow.hook.event.item;

import io.ib67.astralflow.item.AstralItem;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Getter
public class ItemInteractEntityEvent extends ItemEvent {
    private final Entity target;

    public ItemInteractEntityEvent(AstralItem item, Player player, Entity target) {
        super(item, player);
        this.target = target;
    }
}
