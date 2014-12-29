/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.guice;

import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;

import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

final class MuleSupportModule extends AbstractModule
{
    private MuleContext muleContext;

    public MuleSupportModule(MuleContext muleContext)
    {
        this.muleContext = muleContext;
    }

    @Override
    protected final void configure()
    {

        bindListener(Matchers.any(), new TypeListener()
        {
            public <I> void hear(TypeLiteral<I> iTypeLiteral, TypeEncounter<I> iTypeEncounter)
            {
                //iTypeEncounter.register(new MuleRegistryInjectionLister());
                iTypeEncounter.register(new MuleContextAwareInjector<I>());
            }
        });
        bind(MuleContext.class).toInstance(muleContext);
    }


    private class MuleContextAwareInjector<I> implements MembersInjector<I>
    {
        @Override
        public void injectMembers(I o)
        {
            if(o instanceof MuleContextAware)
            {
                ((MuleContextAware)o).setMuleContext(muleContext);
            }
        }
    }
}
