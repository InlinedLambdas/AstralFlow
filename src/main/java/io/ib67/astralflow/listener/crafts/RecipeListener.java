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

package io.ib67.astralflow.listener.crafts;

import io.ib67.astralflow.AstralFlow;
import io.ib67.astralflow.internal.RecipeHelper;
import io.ib67.astralflow.item.recipe.AstralRecipe;
import io.ib67.astralflow.item.recipe.IRecipeRegistry;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;

import java.util.Map;
import java.util.WeakHashMap;

@RequiredArgsConstructor
public class RecipeListener implements Listener {
    private final IRecipeRegistry recipeRegistry;

    private final Map<Player, AstralRecipe> recipeSessions = new WeakHashMap<>();

    @EventHandler(priority = EventPriority.LOW)
    public void onCraftPrepare(PrepareItemCraftEvent event) {
        boolean override = AstralFlow.getInstance().getSettings().getRecipeSetting().isOverrideVanillaRecipe();
        if (event.getRecipe() != null && !override) {
            return;
        }
        var matrix = event.getInventory().getMatrix();
        var recipe = recipeRegistry.matchRecipe(RecipeHelper.leftAlignMatrixItems(matrix));
        recipeSessions.put((Player) event.getViewers().get(0), recipe);
        if (recipe != null) {
            //var modifiedMatrix = recipe.apply(matrix);
            event.getInventory().setResult(recipe.getPrototype().clone());
            //event.getInventory().setMatrix(modifiedMatrix);
        }
    }

    @EventHandler
    public void onTableClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof CraftingInventory) recipeSessions.remove((Player) event.getPlayer());
    }

    @EventHandler
    public void onCraftItem(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof CraftingInventory) {
            var inv = (CraftingInventory) event.getClickedInventory();
            if (event.getSlotType() == InventoryType.SlotType.RESULT && inv.getResult() != null) {
                var recipe = recipeSessions.get((Player) event.getWhoClicked());
                if (recipe != null) {
                    event.setCurrentItem(recipe.produceResult());
                    ;
                    //inv.setMatrix(recipe.apply(inv.getMatrix()));
                }
            }
        }
    }
}
