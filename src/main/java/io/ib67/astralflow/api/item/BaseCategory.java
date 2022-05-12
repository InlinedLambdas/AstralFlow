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

package io.ib67.astralflow.api.item;

import io.ib67.astralflow.item.builder.ItemCategory;
import io.ib67.astralflow.item.builder.ItemPrototype;
import io.ib67.astralflow.item.factory.ItemPrototypeFactory;
import org.jetbrains.annotations.ApiStatus;

/**
 * The base class for some item based on {@link ItemBase}
 */
@ApiStatus.AvailableSince("0.1.0")
public class BaseCategory implements ItemCategory<ItemBase> {
    public static final BaseCategory INSTANCE = new BaseCategory();

    private BaseCategory() {

    }

    @Override
    public ItemPrototypeFactory getFactory(ItemBase item) {
        return ItemPrototype
                .builder()
                .holder(item)
                .id(item.getId())
                .prototype(item.getPrototype())
                .build();
    }
}
