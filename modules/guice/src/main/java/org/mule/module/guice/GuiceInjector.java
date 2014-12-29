/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.guice;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.registry.Injector;

import com.google.inject.Guice;

public final class GuiceInjector implements Injector
{
    private final MuleContext muleContext;
    private com.google.inject.Injector injector;

    public GuiceInjector(MuleContext muleContext)
    {
        this.muleContext = muleContext;
    }

    @Override
    public void inject(Object object)
    {
        injector.injectMembers(object);
    }

    @Override
    public void initialise() throws InitialisationException
    {
        injector = Guice.createInjector(new RegistryBridgeModule(muleContext.getRegistry()),
                                        new MuleSupportModule(muleContext));
    }

    @Override
    public void start() throws MuleException
    {
    }

    @Override
    public void stop() throws MuleException
    {
    }

    @Override
    public void dispose()
    {
        injector = null;
    }
}
