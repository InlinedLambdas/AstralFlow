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

package io.ib67.astralflow.extension.impl;

import io.ib67.astralflow.api.external.AstralExtension;
import io.ib67.astralflow.extension.IExtensionRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@ApiStatus.Internal
public final class ExtensionRegistryImpl implements IExtensionRegistry {
    private final Map<String, AstralExtension> extensions = new HashMap<>();

    @Override
    public @NotNull Collection<? extends AstralExtension> getExtensions() {
        return extensions.values();
    }

    @Override
    public @NotNull Optional<AstralExtension> getExtensionByName(String name) {
        return Optional.ofNullable(extensions.get(name));
    }

    @Override
    public void registerExtension(AstralExtension extension) {
        requireNonNull(extension, "extension");
        requireNonNull(extension.getInfo(), "extension.getInfo()");
        if (extensions.containsKey(extension.getInfo().extensionName())) {
            throw new IllegalArgumentException("Extension with name " + extension.getInfo().extensionName() + " already exists");
        }
        extensions.put(extension.getInfo().extensionName(), extension);
    }
}
