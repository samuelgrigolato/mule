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
import org.mule.module.extensions.internal.util.ValueSetter;

import java.util.List;
import java.util.Map;

public final class DefaultOperationContext implements OperationContext
{

    private final Object configurationInstance;
    private final ResolverSetResult parameters;
    private final MuleEvent event;
    private final List<ValueSetter> instanceLevelGroupValueSetters;

    public DefaultOperationContext(Object configurationInstance, ResolverSetResult parameters, MuleEvent event, List<ValueSetter> instanceLevelGroupValueSetters)
    {
        this.configurationInstance = configurationInstance;
        this.parameters = parameters;
        this.event = event;
        this.instanceLevelGroupValueSetters = instanceLevelGroupValueSetters;
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

    ResolverSetResult getParameters()
    {
        return parameters;
    }

    MuleEvent getEvent()
    {
        return event;
    }

    List<ValueSetter> getInstanceLevelGroupValueSetters()
    {
        return instanceLevelGroupValueSetters;
    }
}
