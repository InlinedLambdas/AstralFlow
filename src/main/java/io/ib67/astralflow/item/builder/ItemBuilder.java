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

import io.ib67.astralflow.AstralFlow;
import io.ib67.astralflow.api.item.armor.ArmorCategory;
import io.ib67.astralflow.api.item.armor.ArmorItem;
import io.ib67.astralflow.api.item.dummy.DummyCategory;
import io.ib67.astralflow.api.item.dummy.DummyItem;
import io.ib67.astralflow.api.item.machine.MachineCategory;
import io.ib67.astralflow.api.item.machine.MachineItem;
import io.ib67.astralflow.api.item.weapon.WeaponBase;
import io.ib67.astralflow.api.item.weapon.WeaponCategory;
import io.ib67.astralflow.item.ItemKey;
import io.ib67.astralflow.item.factory.ItemPrototypeFactory;
import io.ib67.astralflow.item.recipe.AstralRecipe;
import io.ib67.astralflow.texture.Texture;
import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * <p>A utility class to create {@literal & } register your custom items quickly.</p><br />
 * <p>
 * This "builder" doesn't create an item for you, you should consider using {@link ItemKey#createNewItem()} instead.<br />
 * If you're looking for creating itemstacks conveniently, you'd better take a look at {@link io.ib67.astralflow.util.ItemStacks#builder(Material)}
 *
 * @param <C> The type of category
 * @param <T> The type of item, which determined by the category type.
 */
@ApiStatus.AvailableSince("0.1.0")
public final class ItemBuilder<C extends ItemCategory<T>, T> {
    private final ItemCategory<T> category;
    private Texture texture;
    private final List<String> oreDictId = new ArrayList<>();
    private T itemPrototype;
    private final List<AstralRecipe> recipes = new ArrayList<>();

    private ItemBuilder(ItemCategory<T> category) {
        this.category = requireNonNull(category, "Category cannot be null");
    }

    /**
     * Creating a new item builder.
     *
     * @param category The category of the item.
     * @param <C>      The type of category
     * @param <P>      The type of item, which determined by the category type.
     * @return The new item builder.
     */
    public static <C extends ItemCategory<P>, P>
    ItemBuilder<C, P> of(@NotNull ItemCategory<P> category) {
        return new ItemBuilder<>(category);
    }

    /**
     * Create a new itemBuilder with {@link WeaponCategory} preset.
     *
     * @return the weapon builder
     */
    public static ItemBuilder<WeaponCategory, WeaponBase> ofSimpleWeapon() {
        return of(WeaponCategory.INSTANCE);
    }

    /**
     * Create a new itemBuilder with {@link ArmorCategory} preset.
     *
     * @return the armor builder
     */
    public static ItemBuilder<ArmorCategory, ArmorItem> ofSimpleArmor() {
        return of(ArmorCategory.INSTANCE);
    }

    /**
     * Create a new itemBuilder with {@link DummyCategory} preset.
     *
     * @return the dummy item builder
     */
    public static ItemBuilder<DummyCategory, DummyItem> ofDummyItem() {
        return of(DummyCategory.INSTANCE);
    }

    /**
     * Create a new itemBuilder with {@link MachineItem} preset.
     *
     * @return the machine item builder.
     */
    public static ItemBuilder<MachineCategory, MachineItem> ofMachineItem() {
        return of(MachineCategory.INSTANCE);
    }

    @ApiStatus.Experimental
    public ItemBuilder<C, T> bind(String textureId) {
        this.texture = AstralFlow.getInstance().getTextureRegistry().getTexture(textureId).orElseThrow(); // FIXME: default fallback texture
        return this;
    }

    public ItemBuilder<C, T> bind(Texture texture) {
        texture.getModelId(); //TODO: @BEFORE_RELEASE@ Unstable behaviour
        // ensure it is valid registered.
        this.texture = texture;
        return this;
    }

    /**
     * Define the ore dictionary id of the item.
     *
     * @param oreDictId The ore dictionary id of the item.
     * @return The item builder.
     */
    public ItemBuilder<C, T> oreDict(String oreDictId) {
        requireNonNull(oreDictId, "OreDictId cannot be null");
        this.oreDictId.add(oreDictId);
        return this;
    }

    /**
     * Define how can players craft this item.
     * See {@link io.ib67.astralflow.item.recipe.kind.Shaped} , {@link io.ib67.astralflow.item.recipe.kind.Shapeless} etc.
     *
     * @param recipe The recipe of the item.
     * @return The item builder.
     */
    public ItemBuilder<C, T> recipe(AstralRecipe recipe) {
        requireNonNull(recipe, "Recipe cannot be null");
        recipes.add(recipe);
        return this;
    }

    /**
     * Define the item prototype of the item.
     * Type of parameter is up to the category. For example, if you're using {@link io.ib67.astralflow.api.item.machine.MachineCategory} as the category,
     * then you'll give us a {@link io.ib67.astralflow.api.item.machine.MachineItem} as the prototype.
     *
     * @param prototypeRegistry The item prototype of the item.
     * @return The item builder.
     */
    public WrappedBuilder prototype(T prototypeRegistry) {
        requireNonNull(prototypeRegistry, "Item prototype cannot be null");
        this.itemPrototype = prototypeRegistry;
        return new WrappedBuilder(this);
    }

    private void register() {
        var p = category.getFactory(itemPrototype);
        p = PrototypeDecorator.builder()
                .registry(p)
                .itemMapper(i -> {
                    var item = i.clone();
                    var im = item.getItemMeta();
                    if (im == null) {
                        throw new IllegalStateException("ItemMeta is null or AIR! " + category);
                    }
                    if (texture != null) {
                        im.setCustomModelData(texture.getModelId()); //todo: @BEFORE_RELEASE@ @BREAKING_CHANGE@ Textures should be dynamic generated from the texture registry and updated via packet modification when the texture is changed.
                    }
                    i.setItemMeta(im);
                    return i;
                })
                .build();
        // register item.
        for (String s : oreDictId) {
            AstralFlow.getInstance().getItemRegistry().registerItem(p, s);
        }
        if (oreDictId.isEmpty()) {
            AstralFlow.getInstance().getItemRegistry().registerItem(p, null);
        }
        // recipes
        for (AstralRecipe recipe : recipes) {
            ItemPrototypeFactory finalP = p;
            recipe.setResult(() -> AstralFlow.getInstance().getItemRegistry().createItem(finalP.getId()).asItemStack());
            recipe.setPrototype(p.getPrototype().clone());
            AstralFlow.getInstance().getRecipeRegistry().registerRecipe(recipe);
        }
    }

    /**
     * A view for the item builder.
     */
    public final class WrappedBuilder {
        private final ItemBuilder<C, T> builder;

        private WrappedBuilder(ItemBuilder<C, T> builder) {
            this.builder = builder;
        }

        /**
         * Register your item into ItemRegistry.
         */
        public void register() {
            builder.register();
        }
    }
}
