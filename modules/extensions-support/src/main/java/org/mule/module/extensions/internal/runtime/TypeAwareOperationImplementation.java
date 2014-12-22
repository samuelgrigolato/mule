/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal.runtime;

import static org.mule.module.extensions.internal.util.IntrospectionUtils.checkInstantiable;
import static org.mule.util.Preconditions.checkArgument;
import org.mule.extensions.introspection.OperationContext;
import org.mule.extensions.introspection.OperationImplementation;
import org.mule.extensions.introspection.Parameter;
import org.mule.repackaged.internal.org.springframework.util.ReflectionUtils;

import com.google.common.util.concurrent.Futures;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Implementation of {@link OperationImplementation} which relies on a
 * {@link Class} and a reference to one of its methods. On each execution,
 * that class instantiated and the method invoked.
 *
 * @since 3.7.0
 */
public final class TypeAwareOperationImplementation implements OperationImplementation
{

    private static final ReturnDelegate VOID_RETURN_DELEGATE = new VoidReturnDelegate();
    private static final ReturnDelegate VALUE_RETURN_DELEGATE = new ValueReturnDelegate();

    private final Class<?> actingClass;
    private final Method operationMethod;
    private final ReturnDelegate returnDelegate;

    public TypeAwareOperationImplementation(Class<?> actingClass, Method operationMethod)
    {
        checkInstantiable(actingClass);
        checkArgument(operationMethod != null, "operation method cannot be null");
        this.actingClass = actingClass;
        this.operationMethod = operationMethod;
        returnDelegate = isVoid() ? VOID_RETURN_DELEGATE : VALUE_RETURN_DELEGATE;
    }

    @Override
    public Future<Object> execute(OperationContext operationContext)
    {
        Map<Parameter, Object> parameters = operationContext.getParametersValues();
        Object result = ReflectionUtils.invokeMethod(operationMethod, newOperationInstance(), parameters.values().toArray());

        return Futures.immediateFuture(returnDelegate.asReturnValue(result, operationContext));
    }

    private Object newOperationInstance()
    {
        try
        {
            return actingClass.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException(String.format("Implementation type %s could not be instantiated", actingClass.getName()), e);
        }
    }

    private boolean isVoid()
    {
        Class<?> returnType = operationMethod.getReturnType();
        return returnType.equals(Void.class) || returnType.equals(void.class);
    }

    private interface ReturnDelegate
    {

        Object asReturnValue(Object value, OperationContext operationContext);
    }

    private static class VoidReturnDelegate implements ReturnDelegate
    {

        @Override
        public Object asReturnValue(Object value, OperationContext operationContext)
        {
            return ((DefaultOperationContext) operationContext).getEvent();
        }
    }

    private static class ValueReturnDelegate implements ReturnDelegate
    {

        @Override
        public Object asReturnValue(Object value, OperationContext operationContext)
        {
            return value;
        }
    }
}
