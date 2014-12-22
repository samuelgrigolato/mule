/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal.runtime;

import org.mule.extensions.introspection.Configuration;
import org.mule.extensions.introspection.Parameter;
import org.mule.module.extensions.internal.runtime.resolver.ResolverSet;
import org.mule.module.extensions.internal.runtime.resolver.ValueResolver;
import org.mule.module.extensions.internal.util.IntrospectionUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Implementation of {@link ObjectBuilder} to create instances that
 * implement a given {@link Configuration}.
 * <p/>
 * The object instances are created through the {@link Configuration#getInstantiator()#instantiateObject()}
 * method. A {@link ResolverSet} is also used to automatically set this builders
 * properties. The name of the properties in the {@link ResolverSet must match} the
 * name of an actual property in the prototype class
 *
 * @since 3.7.0
 */
public class ConfigurationResolverSetObjectBuilder extends BaseObjectBuilder<Object>
{

    private final Configuration configuration;

    public ConfigurationResolverSetObjectBuilder(Configuration configuration, ResolverSet resolverSet)
    {
        this.configuration = configuration;
        final Class<?> prototypeClass = configuration.getInstantiator().getObjectType();

        for (Map.Entry<Parameter, ValueResolver> entry : resolverSet.getResolvers().entrySet())
        {
            Method setter = IntrospectionUtils.getSetter(prototypeClass, entry.getKey());
            addPropertyResolver(setter, entry.getValue());
        }
    }

    /**
     * Creates a new instance by calling {@link Configuration#getInstantiator()#instantiateObject()}
     * {@inheritDoc}
     */
    @Override
    protected Object instantiateObject()
    {
        return configuration.getInstantiator().newInstance();
    }
}
