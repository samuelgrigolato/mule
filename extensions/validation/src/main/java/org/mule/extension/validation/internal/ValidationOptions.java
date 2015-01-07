/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.validation.internal;

import org.mule.extensions.annotations.Parameter;
import org.mule.extensions.annotations.param.Optional;

final class ValidationOptions
{

    @Parameter
    @Optional
    private String exceptionClass;

    @Parameter
    @Optional
    private String message = null;

    public String getExceptionClass()
    {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass)
    {
        this.exceptionClass = exceptionClass;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
