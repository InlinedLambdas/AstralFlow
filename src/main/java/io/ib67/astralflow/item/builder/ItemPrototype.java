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

package io.ib67.astralflow.item.builder;

import io.ib67.astralflow.item.ItemKey;
import io.ib67.astralflow.item.ItemState;
import io.ib67.astralflow.item.LogicalHolder;
import io.ib67.astralflow.item.factory.ItemPrototypeFactory;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@Builder
public final class ItemPrototype implements ItemPrototypeFactory {
    private final ItemStack prototype;
    private final ItemState statePrototype;
    private final ItemKey id;
    private final LogicalHolder holder;
}
