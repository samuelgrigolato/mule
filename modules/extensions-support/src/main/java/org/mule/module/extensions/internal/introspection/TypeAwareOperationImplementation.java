/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal.introspection;

import org.mule.extensions.introspection.OperationImplementation;

import java.util.concurrent.Future;

/**
 * Implementation of {@link OperationImplementation} which relies on a
 * {@link Class} and a reference to one of its methods. On each execution,
 * that class instantiated and the method invoked.
 *
 * @since 3.7.0
 */
final class TypeAwareOperationImplementation implements OperationImplementation
{

    private final Class<?> actingClass;

    TypeAwareOperationImplementation(Class<?> actingClass)
    {
        this.actingClass = actingClass;
    }

    @Override
    public Future<Object> tbdHowToExecute()
    {
        throw new UnsupportedOperationException("coming soon!");
    }
}
