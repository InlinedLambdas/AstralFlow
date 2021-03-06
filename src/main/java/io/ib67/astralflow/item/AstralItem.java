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

package io.ib67.astralflow.item;

import io.ib67.astralflow.manager.ItemRegistry;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A utility class helps users to interact with custom items.
 */
@ApiStatus.AvailableSince("0.1.0")
@RequiredArgsConstructor
public final class AstralItem {
    private final ItemStack originalItemStack;
    private final ItemRegistry itemRegistry;

    /**
     * Get as a itemStack
     *
     * @return itemStack
     */
    @NotNull
    public ItemStack asItemStack() {
        return originalItemStack;
    }

    /**
     * Get the state of item.
     * You should save state after your operation is done.
     *
     * @return
     */
    public Optional<ItemState> getState() {
        return Optional.ofNullable(itemRegistry.getState(originalItemStack));
    }

    public void saveState(ItemState state) {
        itemRegistry.saveState(originalItemStack, StateScope.USER_ITEM, state);
    }

    @Override
    public int hashCode() {
        return originalItemStack.hashCode();
    }
}
