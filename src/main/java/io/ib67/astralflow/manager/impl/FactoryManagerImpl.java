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

package io.ib67.astralflow.manager.impl;

import io.ib67.astralflow.machines.IMachine;
import io.ib67.astralflow.machines.IState;
import io.ib67.astralflow.machines.factories.IMachineFactory;
import io.ib67.astralflow.manager.IFactoryManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FactoryManagerImpl implements IFactoryManager {
    private final Map<Class<? extends IMachine>, IMachineFactory<?, ?>> machineFactories = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IMachine, S extends IState> IMachineFactory<T, S> getMachineFactory(Class<T> type) {
        Objects.requireNonNull(type, "Type cannot be null");
        return (IMachineFactory<T, S>) machineFactories.get(type);
    }

    @Override
    public Collection<? extends IMachineFactory<?, ?>> getMachineFactories() {
        return machineFactories.values();
    }

    @Override
    public <T extends IMachine, S extends IState> boolean register(Class<T> claz, IMachineFactory<T, S> factory) {
        Objects.requireNonNull(claz, "Class cannot be null");
        Objects.requireNonNull(factory, "Factory cannot be null");

        if (machineFactories.containsKey(claz)) {
            return false;
        }

        machineFactories.put(claz, factory);
        return true;
    }

    @Override
    public <T extends IMachine, S extends IState> boolean unregister(Class<T> type) {
        Objects.requireNonNull(type, "Type cannot be null");
        return machineFactories.containsKey(type) && machineFactories.remove(type) != null;
    }
}
