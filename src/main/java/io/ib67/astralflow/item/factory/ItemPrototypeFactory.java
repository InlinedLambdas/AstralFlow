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

package io.ib67.astralflow.item.factory;

import io.ib67.astralflow.item.ItemKey;
import io.ib67.astralflow.item.ItemState;
import io.ib67.astralflow.item.LogicalHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Item 所有的动作都通过 Hook 系统实现。
 * 如果需要添加功能可以委托到现有的实现类中
 * <p>
 */
@ApiStatus.AvailableSince("0.1.0")
public interface ItemPrototypeFactory {

    /**
     * 返回物品原型供矿物辞典和物品创建使用。
     *
     * @return
     */
    @Contract(pure = true)
    @NotNull
    ItemStack getPrototype();

    /**
     * 物品状态原型
     *
     * @return
     */
    @Contract(pure = true)
    @Nullable // stateless item
    ItemState getStatePrototype();

    /**
     * 物品的标识 ID。
     *
     * @return
     */
    @Contract(pure = true)
    ItemKey getId();

    /**
     * 物品的注册源。一个注册源可能会被多个注册源装饰以完成对原型的修饰,因此用户需要使用 getRegistry 来获取最内层的注册源
     *
     * @return
     * @implSpec 装饰者必须返回被装饰者的注册源。
     */
    default ItemPrototypeFactory getRegistry() {
        return this;
    }

    default LogicalHolder getHolder() {
        return null;
    }
}
