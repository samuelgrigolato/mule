/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.validation.internal;

import org.mule.extension.validation.Locale;
import org.mule.extension.validation.Validator;

/**
 * Base class for validators that test that a String can be parsed into a numeric value
 */
abstract class NumberValidator implements Validator
{

    private static final String VALIDATION_NOT_VALID = "VALIDATION_NOT_VALID";
    private static final String VALIDATION_LOWER_BOUNDARY = "VALIDATION_LOWER_BOUNDARY";
    private static final String VALIDATION_UPPER_BOUNDARY = "VALIDATION_UPPER_BOUNDARY";

    private final NumberValidationOptions options;
    private String errorType;

    public NumberValidator(NumberValidationOptions options)
    {
        this.options = options;
    }

    protected abstract Number validateWithPattern(String value, String pattern, Locale locale);

    protected abstract Number validateWithoutPattern(String value, Locale locale);

    protected abstract Class<? extends Number> getNumberType();

    @Override
    public boolean isValid()
    {
        Number newValue;

        if (options.getPattern() != null)
        {
            newValue = validateWithPattern(options.getValue(), options.getPattern(), options.getLocale());
        }
        else
        {
            newValue = validateWithoutPattern(options.getValue(), options.getLocale());
        }

        if (newValue == null)
        {
            errorType = VALIDATION_NOT_VALID;
            return false;
        }

        if (options.getMinValue() != null)
        {
            if (newValue.doubleValue() < options.getMinValue().doubleValue())
            {
                errorType = VALIDATION_LOWER_BOUNDARY;
                return false;
            }
        }

        if (options.getMaxValue() != null)
        {
            if (newValue.doubleValue() > options.getMaxValue().doubleValue())
            {
                errorType = VALIDATION_UPPER_BOUNDARY;
                return false;
            }
        }

        return true;
    }

    @Override
    public final String getErrorMessage()
    {
        if (VALIDATION_NOT_VALID.equals(this.errorType))
        {
            return String.format("'%s' is not a valid %s value", options.getValue(), getNumberType().getName());
        }
        else if (VALIDATION_LOWER_BOUNDARY.equals(this.errorType))
        {
            return String.format("'%s' is lower that %s", options.getValue(), options.getMinValue().toString());
        }
        else if (VALIDATION_UPPER_BOUNDARY.equals(this.errorType))
        {
            return String.format("'%s' is greater than %s", options.getValue(), options.getMaxValue().toString());
        }
        else
        {
            return String.format("Unknown validation error for value '%s' with locale %s", options.getValue(), options.getLocale());
        }
    }
}
