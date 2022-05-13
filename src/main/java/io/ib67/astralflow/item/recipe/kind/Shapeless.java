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

package io.ib67.astralflow.item.recipe.kind;

import io.ib67.astralflow.AstralFlow;
import io.ib67.astralflow.internal.RecipeHelper;
import io.ib67.astralflow.item.ItemKey;
import io.ib67.astralflow.item.recipe.AstralRecipe;
import io.ib67.astralflow.item.recipe.IngredientChoice;
import io.ib67.astralflow.item.recipe.RecipeType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Shapeless crafting recipes, and item order is ignored
 */
@ApiStatus.AvailableSince("0.1.0")
@Getter
public final class Shapeless implements AstralRecipe {
    private final IngredientChoice[] choices;
    private final NamespacedKey key;
    private Supplier<ItemStack> resultSupplier;
    @Setter
    private ItemStack prototype;

    private Shapeless(List<IngredientChoice> choices, NamespacedKey key) {
        Objects.requireNonNull(choices, "choices");
        Objects.requireNonNull(key, "key");
        this.choices = choices.toArray(new IngredientChoice[0]);
        this.key = key;
    }

    public static ShapelessBuilder of(NamespacedKey key, Supplier<ItemStack> result) { // for unit tests
        return new ShapelessBuilder(key).setResult(result);
    }

    public static ShapelessBuilder of(Plugin plugin, String key, Supplier<ItemStack> result) {
        return new ShapelessBuilder(new NamespacedKey(plugin, key)).setResult(result);
    }

    public static ShapelessBuilder of(Plugin plugin, String key, ItemStack result) {
        return of(plugin, key, () -> result);
    }

    public static ShapelessBuilder of(Plugin plugin, String key, ItemKey resultItemId) {
        return of(plugin, key, () -> AstralFlow.getInstance().getItemRegistry().getRegistry(resultItemId).getPrototype().clone());
    }

    @Override
    public RecipeType getRecipeType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public ItemStack produceResult() {
        return resultSupplier.get();
    }

    @Override
    public IngredientChoice[] getMatrix() {
        return choices;
    }

    @Override
    public void setResult(Supplier<ItemStack> prototype) {
        this.resultSupplier = prototype;
    }

    @Override
    public boolean test(ItemStack[] itemStacks) {
        if (itemStacks == null) {
            return false;
        }
        // clean itemStack array
        List<ItemStack> cleanItemStacks = Arrays.stream(itemStacks).filter(Objects::nonNull).toList();
        if (cleanItemStacks.size() != choices.length) {
            return false;
        }
        var copy = new ArrayList<IngredientChoice>(List.of(choices));
        for (ItemStack cleanItemStack : cleanItemStacks) {
            var choice = copy.stream().filter(e -> e.test(cleanItemStack)).findFirst().orElse(null);
            if (choice == null) return false;
            copy.remove(choice);
        }
        return true;
    }

    @Override
    public ItemStack[] apply(ItemStack[] itemStacks) {
        Objects.requireNonNull(itemStacks, "itemStacks cannot be null");
        // clean itemStack array
        List<ItemStack> cleanItemStacks = Arrays.stream(itemStacks).filter(Objects::nonNull).toList();
        if (cleanItemStacks.size() != choices.length) {
            throw new IllegalArgumentException("itemStacks size does not match choices size");
        }
        var tran = List.copyOf(cleanItemStacks).toArray(new ItemStack[0]);
        var copy = new ArrayList<IngredientChoice>(List.of(choices));
        for (int i = 0; i < tran.length; i++) {
            var cleanItemStack = cleanItemStacks.get(i);

            ItemStack finalCleanItemStack = cleanItemStack;
            var choice = copy.stream().filter(e -> e.test(finalCleanItemStack)).findFirst().orElse(null);

            if (choice == null) throw new IllegalArgumentException("itemStacks does not match choices ,key: " + key);
            copy.remove(choice);
            if (!choice.test(cleanItemStack))
                throw new IllegalArgumentException("THE itemStack does not match choices ,key: " + key);
            cleanItemStack = choice.apply(cleanItemStack); // override the reference. DO NOT REMOVE THIS LINE
        }

        return RecipeHelper.populateEmptyRows(tran);
    }


    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ShapelessBuilder {
        private final NamespacedKey key;
        private final List<IngredientChoice> choices = new ArrayList<>();
        private Supplier<ItemStack> supplier;
        private ItemStack prototype;

        public ShapelessBuilder addIngredients(IngredientChoice... choices) {
            for (IngredientChoice choice : choices) {
                Objects.requireNonNull(choice, "IngredientChoice cannot be null");
            }
            this.choices.addAll(List.of(choices));
            return this;
        }

        public ShapelessBuilder setResult(Supplier<ItemStack> supplier) {
            this.supplier = supplier;
            return this;
        }

        public ShapelessBuilder demoItem(ItemStack itemStack) {
            this.prototype = itemStack;
            return this;
        }

        public Shapeless build() {
            var r = new Shapeless(choices, key);
            r.setResult(supplier);
            r.setPrototype(prototype == null ? new ItemStack(Material.STONE) : prototype);
            return r;
        }
    }
}
