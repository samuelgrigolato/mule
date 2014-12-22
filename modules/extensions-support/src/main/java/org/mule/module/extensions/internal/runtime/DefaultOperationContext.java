/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal.runtime;

import org.mule.api.MuleEvent;
import org.mule.extensions.introspection.OperationContext;
import org.mule.extensions.introspection.Parameter;
import org.mule.module.extensions.internal.runtime.resolver.ResolverSetResult;

import java.util.Map;

public final class DefaultOperationContext implements OperationContext
{

    private final Object configurationInstance;
    private final ResolverSetResult parameters;
    private final MuleEvent event;

    public DefaultOperationContext(Object configurationInstance, ResolverSetResult parameters, MuleEvent event)
    {
        this.configurationInstance = configurationInstance;
        this.parameters = parameters;
        this.event = event;
    }

    @Override
    public Object getConfigurationInstance()
    {
        return configurationInstance;
    }

    @Override
    public Map<Parameter, Object> getParametersValues()
    {
        return parameters.asMap();
    }

    MuleEvent getEvent()
    {
        return event;
    }
}
