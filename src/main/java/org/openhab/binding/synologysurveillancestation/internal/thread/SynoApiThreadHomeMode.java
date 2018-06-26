/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.synologysurveillancestation.internal.thread;

import static org.openhab.binding.synologysurveillancestation.SynoBindingConstants.CHANNEL_HOMEMODE;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Channel;
import org.openhab.binding.synologysurveillancestation.handler.SynoBridgeHandler;
import org.openhab.binding.synologysurveillancestation.internal.webapi.response.HomeModeResponse;

/**
 * Thread for getting Surveillance Station Home Mode state
 *
 * @author Pavion
 */
@NonNullByDefault
public class SynoApiThreadHomeMode extends SynoApiThread<SynoBridgeHandler> {
    // private final Logger logger = LoggerFactory.getLogger(SynoApiThreadHomeMode.class);

    public SynoApiThreadHomeMode(SynoBridgeHandler handler, int refreshRate) {
        super(SynoApiThread.THREAD_HOMEMODE, handler, refreshRate);
    }

    @Override
    public boolean isNeeded() {
        return (getSynoHandler().isLinked(CHANNEL_HOMEMODE));
    }

    @Override
    public boolean refresh() throws Exception {

        SynoBridgeHandler brdigeHandler = getSynoHandler();
        HomeModeResponse response = brdigeHandler.getSynoWebApiHandler().getHomeModeResponse();
        if (response.isSuccess()) {
            if (getSynoHandler().isLinked(CHANNEL_HOMEMODE)) {
                Channel channel = getSynoHandler().getThing().getChannel(CHANNEL_HOMEMODE);
                getSynoHandler().updateState(channel.getUID(), response.isHomeMode() ? OnOffType.ON : OnOffType.OFF);
            }
            return true;
        } else {
            return false;
        }
    }

}
