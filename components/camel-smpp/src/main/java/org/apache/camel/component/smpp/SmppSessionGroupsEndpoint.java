package org.apache.camel.component.smpp;

import org.apache.camel.*;
import org.apache.camel.spi.UriEndpoint;
import org.jsmpp.session.SMPPSessionGroup;

@UriEndpoint(firstVersion = "3.7.0", scheme = "smpp-grp,smpps-grp", title = "SMPP-GROUP", syntax = "smpp:host:port",
             category = { Category.MOBILE }, lenientProperties = true)
public class SmppSessionGroupsEndpoint extends SmppEndpoint {
    private final SMPPSessionGroup smppSessionGroup;

    public SmppSessionGroupsEndpoint(SMPPSessionGroup smppSessionGroup, String endpointUri, Component component,
                                     SmppConfiguration configuration) {
        super(endpointUri, component, configuration);
        if (smppSessionGroup == null) {
            throw new IllegalArgumentException("smppSessionGroup can not be null");
        }
        this.smppSessionGroup = smppSessionGroup;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        SmppConsumer result = new SmppConsumer(
                this, getConfiguration(), processor,
                smppConfiguration -> smppSessionGroup.createSession(SmppConnectionFactory.getInstance(smppConfiguration)));
        configureConsumer(result);
        return result;
    }
}
