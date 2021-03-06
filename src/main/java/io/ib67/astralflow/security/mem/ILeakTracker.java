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

package io.ib67.astralflow.security.mem;

import org.jetbrains.annotations.ApiStatus;

/**
 * A class that tracks object live time and detects memory leaks.
 */
@ApiStatus.AvailableSince("0.1.0")
public interface ILeakTracker {
    /**
     * Add an object into the track list. Tracker will alert if the object kept in memory for many rounds.
     *
     * @param obj
     */
    void track(Object obj);

    /**
     * Contrary to {@link #track(Object)}
     *
     * @param obj
     */
    void untrack(Object obj);
}
