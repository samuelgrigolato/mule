/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.guice;

import org.mule.api.registry.Registry;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.util.Map;

final class RegistryBridgeModule extends AbstractModule
{
    private final Registry registry;

    RegistryBridgeModule(Registry registry)
    {
        this.registry = registry;
    }

    @Override
    protected void configure()
    {
        for (Map.Entry<String, Object> entry : registry.getAll().entrySet())
        {
            Class<Object> clazz = (Class<Object>) entry.getValue().getClass();
            bind(clazz)
                    .annotatedWith(Names.named(entry.getKey()))
                    .toInstance(entry.getValue());
        }
    }

}
