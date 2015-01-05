/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.validation.internal;

import org.mule.api.DefaultMuleException;
import org.mule.config.i18n.MessageFactory;
import org.mule.extension.validation.ValidationExtension;
import org.mule.extension.validation.Validator;
import org.mule.extension.validation.exception.ValidationException;
import org.mule.extensions.annotations.WithConfig;
import org.mule.util.ClassUtils;

import org.apache.commons.lang.StringUtils;

abstract class ValidationSupport
{
    @WithConfig
    protected ValidationExtension config;

    protected ValidationOptions options;

    protected void validateWith(Validator validator) throws Exception
    {
        if (!validator.isValid())
        {
            String customMessage = options.getMessage();
            String message = StringUtils.isBlank(customMessage) ? validator.getErrorMessage() : customMessage;
            throw buildException(options, message);
        }
    }

    private Exception buildException(ValidationOptions options, String message)
    {
        try
        {
            return ClassUtils.instanciateClass(getExceptionClass(options), new Object[] {message});
        }
        catch (IllegalArgumentException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            return new DefaultMuleException(
                    MessageFactory.createStaticMessage("Failed to create validation exception"), e);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Exception> getExceptionClass(ValidationOptions options)
    {
        String exceptionClassName = options.getExceptionClass();

        if (StringUtils.isEmpty(exceptionClassName))
        {
            exceptionClassName = config.getDefaultExceptionClass();
        }

        if (StringUtils.isEmpty(exceptionClassName))
        {
            return ValidationException.class;
        }

        Class<? extends Exception> exceptionClass;
        try
        {
            exceptionClass = ClassUtils.getClass(exceptionClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException("Could not find exception class " + exceptionClassName);
        }

        if (!Exception.class.isAssignableFrom(exceptionClass))
        {
            throw new IllegalArgumentException(String.format(
                    "Was expecting an exception type, %s found instead", exceptionClass.getCanonicalName()));
        }

        if (ClassUtils.getConstructor(exceptionClass, new Class[] {String.class}) == null)
        {
            throw new IllegalArgumentException(
                    String.format(
                            "Exception class must contain a constructor with a single String argument. %s doesn't have a matching constructor",
                            exceptionClass.getCanonicalName()));
        }

        return exceptionClass;
    }


}
