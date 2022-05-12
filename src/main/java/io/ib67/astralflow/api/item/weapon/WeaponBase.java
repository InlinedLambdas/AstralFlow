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

package io.ib67.astralflow.api.item.weapon;

import io.ib67.astralflow.api.AstralHelper;
import io.ib67.astralflow.api.item.ItemBase;
import io.ib67.astralflow.hook.HookType;
import io.ib67.astralflow.item.ItemKey;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * A simple template for weapon items.
 */
@ApiStatus.AvailableSince("0.1.0")
@Getter
public abstract class WeaponBase extends ItemBase {
    private final Predicate<Entity> entitySelector;
    private final WeaponProperty property;
    private final Set<EntityDamageEvent.DamageCause> damageTypes;

    protected WeaponBase(ItemKey id, ItemStack prototype, WeaponProperty property, Predicate<Entity> entitySelector, Set<EntityDamageEvent.DamageCause> types) {
        super(id, prototype);
        this.property = property;
        requireNonNull(property);
        this.damageTypes = types == null ? Collections.emptySet() : types;
        this.entitySelector = entitySelector == null ? e -> true : entitySelector;
        HookType.ENTITY_DAMAGE_BY_ENTITY.register(this::onEntityDamage);
    }

    private void onEntityDamage(EntityDamageByEntityEvent event) {
        var damager = event.getDamager();
        if (damager.getType() != EntityType.PLAYER) {
            return;
        }
        if (damageTypes.contains(event.getCause())) {
            var player = (Player) damager;
            var itemInHand = player.getEquipment().getItemInMainHand();
            if (itemInHand.getType() == Material.AIR) {
                return;
            }
            var isItem = AstralHelper.isHolder(itemInHand, this);
            if (!isItem) return;
            // apply damage.
            event.setDamage(damageCalc(event.getEntity(), event.getFinalDamage()));
        }
    }

    protected double damageCalc(Entity entity, double originalDamage) {

        // apply damage.
        if (entitySelector.test(entity)) {
            var damage = this.property.getDamage();
            if (!property.isClearOriginalDamage()) {
                damage = damage + originalDamage;
            }
            if (Math.random() > property.getCriticalChance()) {
                damage = damage * property.getCriticalMultiplexer();
            }
            return damage;
        }
        return originalDamage;
    }

}
