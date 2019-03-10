/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.synologysurveillancestation.internal.webapi.request;

import static org.openhab.binding.synologysurveillancestation.SynoBindingConstants.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.synologysurveillancestation.internal.SynoConfig;
import org.openhab.binding.synologysurveillancestation.internal.webapi.WebApiException;
import org.openhab.binding.synologysurveillancestation.internal.webapi.response.HomeModeResponse;
import org.openhab.binding.synologysurveillancestation.internal.webapi.response.SimpleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SYNO.SurveillanceStation.Notification.Filter
 *
 * Event Detection related WebAPI. e.g. Enumerate detection parameters or long polling for alarm status or save
 * detection parameters.
 *
 * @author b-r-y - Initial contribution
 * @author Pavion - b-r-y copied his stuff well :)
 *
 */
@NonNullByDefault
public class SynoApiNotificationFilter extends SynoApiRequest<HomeModeResponse> {
    private final Logger logger = LoggerFactory.getLogger(SynoApiNotificationFilter.class);

    // API configuration
    private static final String API_NAME = "SYNO.SurveillanceStation.Notification.Filter";
    private static final SynoApiConfig API_CONFIG = new SynoApiConfig(API_NAME, API_VERSION_01, API_SCRIPT_ENTRY);

    /**
     * @param config
     */
    public SynoApiNotificationFilter(SynoConfig config, HttpClient httpClient) {
        super(API_CONFIG, config, httpClient);
    }

    /**
     * Get API events
     *
     * @return
     * @throws WebApiException
     */
    public HomeModeResponse getHomeModeResponse() {
        try {
            Map<String, String> params = new HashMap<>();
            return callApi(METHOD_GETINFO, params);
        } catch (WebApiException e) {
            return new HomeModeResponse("{\"data\":{},\"success\":false}");
        }
    }

    public SimpleResponse setNotificationFilter(String channel, String notificationFilter) throws WebApiException {
        if (!(notificationFilter.equals("0") || notificationFilter.equals("1") || notificationFilter.equals("2")
                || notificationFilter.equals("3") || notificationFilter.equals("4") || notificationFilter.equals("5")
                || notificationFilter.equals("6") || notificationFilter.equals("7"))) {
            return new SimpleResponse("{\"data\":{},\"success\":false}");
        }
        String eventType = selectEventType(channel);
        Map<String, String> params = new HashMap<>();
        params.put(eventType, notificationFilter);
        return callApi(METHOD_SET, params);
    }

    /**
     * Implements the Notification Filter API selection
     *
     * @param channel
     * @return
     */
    private String selectEventType(String channel) {
        String eventType;
        switch (channel) {
            case CHANNEL_NF_CAMCONLOST:
                eventType = "3";
                break;
            case CHANNEL_NF_CAMCONRESUMED:
                eventType = "4";
                break;
            case CHANNEL_NF_MOTIONDETECTED:
                eventType = "5";
                break;
            case CHANNEL_NF_DIGINPUTDETECTED:
                eventType = "6";
                break;
            case CHANNEL_NF_AUDIODETECTED:
                eventType = "8";
                break;
            case CHANNEL_NF_TAMPERINGDETECTED:
                eventType = "9";
                break;
            case CHANNEL_NF_ARCHIVELIMITREACHED:
                eventType = "10";
                break;
            default:
                eventType = "0";
                break;
        }
        return eventType;
    }

}
