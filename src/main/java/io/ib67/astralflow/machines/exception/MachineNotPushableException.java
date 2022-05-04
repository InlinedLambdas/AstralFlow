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

package io.ib67.astralflow.machines.exception;

import io.ib67.astralflow.machines.IMachine;
import org.jetbrains.annotations.ApiStatus;

/**
 * Thrown when a machine is not pushable.
 */
@ApiStatus.AvailableSince("0.1.0")
public class MachineNotPushableException extends MachineException {

    public MachineNotPushableException(IMachine machine) {
        super(machine);
    }

    public MachineNotPushableException(String message, IMachine machine) {
        super(message, machine);
    }

    public MachineNotPushableException(String message, Throwable cause, IMachine machine) {
        super(message, cause, machine);
    }

    public MachineNotPushableException(Throwable cause, IMachine machine) {
        super(cause, machine);
    }

    protected MachineNotPushableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, IMachine machine) {
        super(message, cause, enableSuppression, writableStackTrace, machine);
    }
}
