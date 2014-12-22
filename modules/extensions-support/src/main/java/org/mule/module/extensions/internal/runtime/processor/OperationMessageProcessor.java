/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal.runtime.processor;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.extensions.introspection.Operation;
import org.mule.module.extensions.internal.runtime.resolver.ResolverSet;
import org.mule.module.extensions.internal.runtime.resolver.ResolverSetResult;
import org.mule.module.extensions.internal.runtime.resolver.ValueResolver;

import java.util.concurrent.Future;

public final class OperationMessageProcessor implements MessageProcessor
{

    private final ValueResolver configuration;
    private final Operation operation;
    private final ResolverSet resolverSet;

    public OperationMessageProcessor(ValueResolver configuration, Operation operation, ResolverSet resolverSet)
    {
        this.configuration = configuration;
        this.operation = operation;
        this.resolverSet = resolverSet;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        Object configInstance = configuration.resolve(event);
        ResolverSetResult parameters = resolverSet.resolve(event);

        Future<Object> future = operation.getImplementation().execute(new DefaultOperationContext(configInstance, parameters, event));

        // for now this is fine because the execution engine is blocking. When we move
        // to a non-blocking engine, this future needs to be handled differently
        //Object result = future.get();
        //
        //if (result instanceof MuleEvent)
        //{
        //    return (MuleEvent) result;
        //}
        //else if (result instanceof MuleMessage)
        //{
        //    event.setMessage((MuleMessage) result);
        //}
        //else
        //{
        //    event.getMessage().setPayload(result);
        //}

        return event;
    }
}
