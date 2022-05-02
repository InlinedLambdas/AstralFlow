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

package io.ib67.astralflow.util;

import org.jetbrains.annotations.ApiStatus;

/**
 * Categories of logs.
 */
@ApiStatus.AvailableSince("0.1.0")
public final class LogCategory {
    /**
     * The category for general logs.
     */
    public static final String INIT = "Init";
    /**
     * The category for logs at plugin termination
     */
    public static final String TERMINATION = "Termination";
    /**
     * The category for logs at debugging
     */
    public static final String DEBUG = "Debug";
    /**
     * The category for extensions' log
     */
    public static final String EXTENSION = "Extension";
    /**
     * The category for config migrator
     */
    public static final String MIGRATOR = "Migrator";
    /**
     * The category for ticking scheduler. {@link io.ib67.astralflow.machines.scheduler.SimpleCatchingScheduler}
     */
    public static final String SCHEDULER = "Scheduler";
}
