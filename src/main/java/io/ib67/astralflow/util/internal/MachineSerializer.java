/*
 *
 *
 *  *
 *  *     AstralFlow - Storage utilities for spigot servers.
 *  *     Copyright (C) 2022 iceBear67
 *  *
 *  *     This library is free software; you can redistribute it and/or
 *  *     modify it under the terms of the GNU Lesser General Public
 *  *     License as published by the Free Software Foundation; either
 *  *     version 2.1 of the License, or (at your option) any later version.
 *  *
 *  *     This library is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  *     Lesser General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU Lesser General Public
 *  *     License along with this library; if not, write to the Free Software
 *  *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *  *     USA
 *
 */

package io.ib67.astralflow.util.internal;

import com.google.gson.*;
import io.ib67.Util;
import io.ib67.astralflow.machines.IMachine;
import io.ib67.astralflow.machines.IMachineData;
import io.ib67.astralflow.manager.IFactoryManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Type;
import java.util.UUID;

@ApiStatus.Internal
@RequiredArgsConstructor
public class MachineSerializer implements JsonSerializer<IMachine>, JsonDeserializer<IMachine> {
    private static final String KEY_TYPE = "type";
    private static final String KEY_STATE = "state";
    private static final String KEY_LOCATION = "loc";
    private static final String KEY_ID = "uuid";
    private final IFactoryManager factories;

    @SuppressWarnings("unchecked")
    @Override
    public IMachine deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // assertion 1. context is a bukkit compatible serializer
        var jo = json.getAsJsonObject(); // assertion.
        UUID uuid = context.deserialize(jo.get(KEY_ID), UUID.class);
        IMachineData state = context.deserialize(jo.get(KEY_STATE), IMachineData.class);
        Location location = context.deserialize(jo.get(KEY_LOCATION), Location.class);
        var type = jo.getAsJsonPrimitive(KEY_TYPE).getAsString();

        return (IMachine) Util.runCatching(() -> (Object) Class.forName(type)) // to be caught.
                .onFailure(t -> {
                    throw new JsonParseException("Can't find machine type: " + type, t);
                }).onSuccess(clazz -> {
                    var factory = factories.getMachineFactory((Class<? extends IMachine>) clazz);
                    if (factory == null) {
                        throw new JsonParseException("No factories have registered for this type: " + type);
                    }
                    return factory.createMachine(location, uuid, state);
                });
    }

    @Override
    public JsonElement serialize(IMachine src, Type typeOfSrc, JsonSerializationContext context) {
        var jo = new JsonObject();
        jo.add(KEY_ID, context.serialize(src.getId()));
        jo.add(KEY_STATE, context.serialize(src.getState()));
        jo.add(KEY_LOCATION, context.serialize(src.getLocation()));
        jo.addProperty(KEY_TYPE, src.getType().getCanonicalName());
        return jo;
    }
}
