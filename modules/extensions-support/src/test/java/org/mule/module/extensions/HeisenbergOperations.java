/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.NestedProcessor;
import org.mule.api.transport.PropertyScope;
import org.mule.extensions.annotations.Operation;
import org.mule.extensions.annotations.param.Optional;
import org.mule.extensions.annotations.param.Payload;

import java.util.List;

import javax.inject.Inject;

public class HeisenbergOperations
{

    @Inject
    private HeisenbergExtension config;

    @Operation
    public String sayMyName()
    {
        return config.getMyName();
    }

    @Operation
    public String getEnemy(int index)
    {
        return config.getEnemies().get(index);
    }

    @Operation
    public String kill(@Payload String goodbyeMessage, NestedProcessor enemiesLookup) throws Exception
    {
        return killWithCustomMessage(goodbyeMessage, enemiesLookup);
    }

    @Operation
    public String killWithCustomMessage(@Optional(defaultValue = "#[payload]") String goodbyeMessage,
                                        NestedProcessor enemiesLookup) throws Exception
    {
        List<String> toKill = (List<String>) enemiesLookup.process();
        StringBuilder builder = new StringBuilder();

        for (String kill : toKill)
        {
            builder.append(String.format("%s: %s", goodbyeMessage, kill)).append("\n");
        }

        return builder.toString();
    }

    @Operation
    public void hideMethInEvent(MuleEvent event)
    {
        hideMethInMessage(event.getMessage());
    }

    @Operation
    public void hideMethInMessage(MuleMessage message)
    {
        message.setProperty("secretPackage", "meth", PropertyScope.INVOCATION);
    }
}
