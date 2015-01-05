/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.validation;

import org.mule.extension.validation.internal.AllValidation;
import org.mule.extension.validation.internal.CommonValidations;
import org.mule.extensions.annotations.Extension;
import org.mule.extensions.annotations.Operations;
import org.mule.extensions.annotations.Parameter;
import org.mule.extensions.annotations.capability.Xml;
import org.mule.extensions.annotations.param.Optional;

/**
 * Validations extension
 *
 * @since 3.7.0
 */
@Extension(name = "validation", version = "3.7")
@Xml(namespace = "validation")
@Operations({CommonValidations.class, AllValidation.class})
public class ValidationExtension
{

    /**
     * The canonical name of the exception class to be thrown each time a validation fails.
     * This is optional in case that you want to customize the exception type. If not provided
     * it will default to {@link org.mule.extension.validation.exception.ValidationException}
     */
    @Parameter
    @Optional(defaultValue = "org.mule.extension.validation.exception.ValidationException")
    private String defaultExceptionClass;


    public String getDefaultExceptionClass()
    {
        return defaultExceptionClass;
    }

    public void setDefaultExceptionClass(String defaultExceptionClass)
    {
        this.defaultExceptionClass = defaultExceptionClass;
    }
}
