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

package io.ib67.astralflow.item.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.List;

/**
 * This class could registry a recipe.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface IRecipeRegistry {
    @Contract(pure = true, value = "_->this")
    IRecipeRegistry registerRecipe(AstralRecipe recipe);

    @Contract(pure = true, value = "_->this")
    IRecipeRegistry unregisterRecipe(AstralRecipe recipe);

    AstralRecipe getRecipeByKey(NamespacedKey key);

    @Contract(value = " -> new")
    List<? extends AstralRecipe> getRecipes();

    AstralRecipe matchRecipe(ItemMatrix matrix);

    @SuppressWarnings("all")
    default AstralRecipe matchRecipe(ItemStack... recipe) {
        return matchRecipe(ItemMatrix.createRawMatrix(RecipeType.CRAFTING, recipe));
    }
}
