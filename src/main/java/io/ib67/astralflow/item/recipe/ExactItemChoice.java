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

package io.ib67.astralflow.item.recipe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor
@Getter
public final class ExactItemChoice implements IngredientChoice {
    private final short count;
    private final short durability;
    private final List<ItemStack> material;

    public ExactItemChoice(ItemStack... material) {
        this((short) 1, (short) 0, List.of(material));
    }

    @Override
    public boolean test(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        return material.stream().anyMatch(e -> e.isSimilar(itemStack));
    }

    @Override
    public List<? extends ItemStack> getRepresentativeItems() {
        return material;
    }
}
