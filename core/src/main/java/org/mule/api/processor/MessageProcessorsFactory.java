/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.api.processor;

import org.mule.api.MuleException;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.endpoint.OutboundEndpoint;

public interface MessageProcessorsFactory
{
    public MessageProcessor[] createInboundMessageProcessors(InboundEndpoint endpoint);

    public MessageProcessor[] createInboundResponseMessageProcessors(InboundEndpoint endpoint);

    public MessageProcessor[] createOutboundMessageProcessors(OutboundEndpoint endpoint) throws MuleException;

    public MessageProcessor[] createOutboundResponseMessageProcessors(OutboundEndpoint endpoint) throws MuleException;
}


