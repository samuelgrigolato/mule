/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.extensions.annotations.Operation;
import org.mule.extensions.annotations.param.Optional;
import org.mule.extensions.annotations.param.Payload;
import org.mule.util.ValueHolder;

import javax.inject.Inject;

public class HeisenbergOperations
{

    private static final String SECRET_PACKAGE = "secretPackage";
    private static final String METH = "meth";

    public static ValueHolder<HeisenbergExtension> configHolder = new ValueHolder<>();
    public static ValueHolder<MuleEvent> eventHolder = new ValueHolder<>();
    public static ValueHolder<MuleMessage> messageHolder = new ValueHolder<>();

    @Inject
    private HeisenbergExtension config;

    @Inject
    private MuleEvent event;

    @Inject
    private MuleMessage message;

    public HeisenbergOperations()
    {
        // remove when injector is in place
        config = configHolder.get();
        event = eventHolder.get();
        message = messageHolder.get();
    }

    @Operation
    public String sayMyName()
    {
        return config.getMyName();
    }

    @Operation
    public void die()
    {
        config.setFinalHealth(HealthStatus.DEAD);
    }

    @Operation
    public String getEnemy(int index)
    {
        return config.getEnemies().get(index);
    }

    @Operation
    public String kill(@Payload String victim, String goodbyeMessage) throws Exception
    {
        return killWithCustomMessage(victim, goodbyeMessage);
    }

    @Operation
    public String killWithCustomMessage(@Optional(defaultValue = "#[payload]") String victim, String goodbyeMessage)
    {
        return String.format("%s, %s", goodbyeMessage, victim);
    }

    @Operation
    public void hideMethInEvent()
    {
        event.setFlowVariable(SECRET_PACKAGE, METH);
    }

    @Operation
    public void hideMethInMessage()
    {
        message.setProperty(SECRET_PACKAGE, METH, PropertyScope.INVOCATION);
    }
}
