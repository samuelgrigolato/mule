/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.guice;

import org.mule.DefaultMuleContext;
import org.mule.api.MuleContext;
import org.mule.config.builders.AbstractConfigurationBuilder;
import org.mule.registry.Injector;

public class RootGuiceInjectorConfigurationBuilder extends AbstractConfigurationBuilder
{

    @Override
    protected void doConfigure(MuleContext muleContext) throws Exception
    {
        if (muleContext instanceof DefaultMuleContext)
        {
            Injector injector = new GuiceInjector(muleContext);
            injector.initialise();
            ((DefaultMuleContext) muleContext).setRootInjector(injector);
        }
    }
}
