/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mule.module.extensions.HealthStatus.DEAD;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.extensions.introspection.Describer;
import org.mule.module.extensions.HeisenbergExtension;
import org.mule.module.extensions.HeisenbergOperations;
import org.mule.module.extensions.internal.introspection.AnnotationsBasedDescriber;
import org.mule.module.extensions.internal.runtime.resolver.ValueResolver;
import org.mule.tck.junit4.ExtensionsFunctionalTestCase;

import org.junit.Test;

public class OperationParserTestCase extends ExtensionsFunctionalTestCase
{

    private static final String GUSTAVO_FRING = "Gustavo Fring";
    private static final String SECRET_PACKAGE = "secretPackage";
    private static final String METH = "meth";

    @Override
    protected Describer[] getManagedDescribers()
    {
        return new Describer[] {new AnnotationsBasedDescriber(HeisenbergExtension.class)};
    }

    @Override
    protected String getConfigFile()
    {
        return "heisenberg-operation-config.xml";
    }

    /**
     * Temporal hack until injector in place
     */
    @Override
    protected void doSetUp() throws Exception
    {
        HeisenbergOperations.configHolder.set(getConfig("heisenberg"));
    }

    @Test
    public void argLessOperationWithReturnValue() throws Exception
    {
        runFlowAndExpect("sayMyName", "Heisenberg");
    }

    @Test
    public void voidArgLessOperation() throws Exception
    {
        MuleEvent originalEvent = getTestEvent(EMPTY);
        MuleEvent responseEvent = runFlow("die", originalEvent);
        assertThat(responseEvent.getMessageAsString(), is(EMPTY));
        assertThat(getConfig("heisenberg").getFinalHealth(), is(DEAD));
    }

    @Test
    public void getFixedEnemy() throws Exception
    {
        runFlowAndExpect("getFixedEnemy", GUSTAVO_FRING);
    }

    @Test
    public void getExpressionEnemy() throws Exception
    {
        doTestExpressionEnemy(0);
    }

    @Test
    public void getExpressionEnemyWithParamTransformation() throws Exception
    {
        doTestExpressionEnemy("0");
    }

    @Test
    public void operationWithInjectedEvent() throws Exception
    {
        MuleEvent event = getTestEvent(EMPTY);
        HeisenbergOperations.eventHolder.set(event);
        event = runFlow("hideInEvent", event);
        assertThat((String) event.getFlowVariable(SECRET_PACKAGE), is(METH));
    }

    @Test
    public void operationWithInjectedMessage() throws Exception
    {
        MuleEvent event = getTestEvent(EMPTY);
        MuleMessage message = event.getMessage();
        HeisenbergOperations.messageHolder.set(message);
        runFlow("hideInMessage", getTestEvent(EMPTY));
        assertThat((String) message.getInvocationProperty(SECRET_PACKAGE), is(METH));
    }

    private void doTestExpressionEnemy(Object enemyIndex) throws Exception
    {
        MuleEvent event = getTestEvent(EMPTY);
        event.setFlowVariable("enemy", enemyIndex);
        event = runFlow("expressionEnemy", event);
        assertThat(event.getMessageAsString(), is(GUSTAVO_FRING));
    }

    private HeisenbergExtension getConfig(String name) throws Exception
    {
        ValueResolver<HeisenbergExtension> config = muleContext.getRegistry().lookupObject(name);
        return config.resolve(getTestEvent(EMPTY));
    }
}
