/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal.config;

import static org.mule.module.extensions.internal.config.XmlExtensionParserUtils.getResolverSet;
import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.LifecycleUtils;
import org.mule.extensions.introspection.Configuration;
import org.mule.module.extensions.internal.runtime.resolver.ConfigurationValueResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} which returns a {@link ConfigurationValueResolver} that provides the actual instances
 * that implement a given {@link Configuration}. Subsequent invokations to {@link #getObject()} method
 * returns always the same {@link ConfigurationValueResolver}.
 *
 * @since 3.7.0
 */
public final class ExtensionConfigurationFactoryBean implements FactoryBean<Object>, MuleContextAware, Initialisable, Disposable
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionConfigurationFactoryBean.class);

    private final ConfigurationValueResolver resolver;
    private MuleContext muleContext;

    public ExtensionConfigurationFactoryBean(String name, Configuration configuration, ElementDescriptor element)
    {
        resolver = new ConfigurationValueResolver(name, configuration, getResolverSet(element, configuration.getParameters()));
    }

    /**
     * Returns a {@link ConfigurationValueResolver}
     */
    @Override
    public Object getObject() throws Exception
    {
        return resolver;
    }

    /**
     * @return {@link ConfigurationValueResolver}
     */
    @Override
    public Class<?> getObjectType()
    {
        return ConfigurationValueResolver.class;
    }

    /**
     * @return {@value true}
     */
    @Override
    public boolean isSingleton()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMuleContext(MuleContext context)
    {
        muleContext = context;
    }

    /**
     * Propagates the {@link #muleContext} and initialisation event
     * to the underlying {@link #resolver} in case it implements the
     * {@link MuleContextAware} and/or {@link Initialisable} interfaces
     *
     * @throws InitialisationException
     */
    @Override
    public void initialise() throws InitialisationException
    {
        LifecycleUtils.initialiseIfNeeded(resolver, muleContext);
    }

    /**
     * Propagates the disposal event to the
     * underlying {@link #resolver} if it implements
     * the {@link Disposable} interface
     */
    @Override
    public void dispose()
    {
        LifecycleUtils.disposeIfNeeded(resolver, LOGGER);
    }
}
