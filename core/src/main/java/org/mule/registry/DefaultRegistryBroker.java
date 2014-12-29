/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.registry;

import org.mule.api.MuleContext;
import org.mule.api.registry.Registry;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultRegistryBroker extends AbstractRegistryBroker
{
    private TransientRegistry transientRegistry;
    private List<Registry> registries = new CopyOnWriteArrayList<Registry>();

    public DefaultRegistryBroker(MuleContext context)
    {
        super(context);
        transientRegistry = new TransientRegistry(context);
        registries.add(0, transientRegistry);
    }

    @Override
    public Map<String, Object> getAll()
    {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        for (Registry registry : registries)
        {
            builder.putAll(registry.getAll());
        }

        return builder.build();
    }

    TransientRegistry getTransientRegistry()
    {
        return transientRegistry;
    }

    @Override
    public void addRegistry(Registry registry)
    {
        registries.add(1, registry);
    }

    @Override
    public void removeRegistry(Registry registry)
    {
        registries.remove(registry);
    }

    protected Collection<Registry> getRegistries()
    {
        return registries;
    }
}
