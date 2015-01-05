/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.validation;

import org.mule.api.MuleEvent;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.List;

public class ValidationResult implements Serializable
{

    private final MuleEvent muleEvent;
    private final List<String> messages;

    public ValidationResult(MuleEvent muleEvent, List<String> messages)
    {
        this.muleEvent = muleEvent;
        this.messages = messages != null ? ImmutableList.copyOf(messages) : ImmutableList.<String>of();
    }

    public List<String> getMessages()
    {
        return messages;
    }

    public boolean hasErrors()
    {
        return !messages.isEmpty();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if (this.messages != null)
        {
            for (String message : this.messages)
            {
                if (builder.length() > 0)
                {
                    builder.append("\n");
                }

                builder.append(message);
            }
        }

        return builder.toString();
    }
}
