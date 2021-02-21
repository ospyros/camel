package org.apache.camel.component.smpp;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.annotations.Component;
import org.apache.camel.support.DefaultComponent;
import org.jsmpp.session.SMPPSessionGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component for creating @{link org.jsmpp.session.SmppSessionGroup}s and generating corresponding endpoints.
 */
@Component("smpp-grp,smpps-grp")
public class SmppSessionGroupsComponent extends DefaultComponent {
    private static final Logger LOG = LoggerFactory.getLogger(SmppSessionGroupsComponent.class);

    @Metadata(label = "advanced")
    private List<SmppSessionGroupConfiguration> groupConfigurations = new ArrayList<>();

    private SmppConfiguration configuration;

    private Map<String, SMPPSessionGroup> smppSessionGroups = new HashMap<>();

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        if (this.configuration == null) {
            this.configuration = new SmppConfiguration();
        }
        // create a copy of the configuration as other endpoints can adjust their copy as well
        SmppConfiguration config = this.configuration.copy();
        config.configureFromURI(new URI(uri));
        String groupId = (String) parameters.get(SmppConstants.SESSION_GROUP_ID);
        if (groupId == null) {
            LOG.warn("No group id set for host: {} - port: {} - system type: {} - system id: {}. " +
                     "Returning a standalone SmppEndpoint.",
                    config.getHost(),
                    config.getPort(), config.getSystemType(), config.getSystemId());
            return new SmppEndpoint(uri, this, config);
        }
        SMPPSessionGroup sessionGroup = smppSessionGroups.get(groupId);
        if (sessionGroup == null) {
            LOG.warn("Group id {} has not been configured. Creating it with default parameters. " +
                     "This will most likely negatively impact performance." +
                     config.getSessionGroupId());
            sessionGroup = new SMPPSessionGroup();
            smppSessionGroups.put(groupId, sessionGroup);
        }
        return new SmppSessionGroupsEndpoint(sessionGroup, uri, this, config);
    }

    /**
     * Smpp groups configuration used to generate and initialize smpp session groups.
     */
    public void setComponentConfiguration(List<SmppSessionGroupConfiguration> groupConfigurations) {
        this.groupConfigurations = groupConfigurations;

        for (SmppSessionGroupConfiguration groupConfiguration : groupConfigurations) {
            SMPPSessionGroup smppSessionGroup = new SMPPSessionGroup(
                    groupConfiguration.getPduProcessorCoreDegree(),
                    groupConfiguration.getPduProcessorMaxDegree(), groupConfiguration.getPduProcessorQueueCapacity());
            smppSessionGroups.put(groupConfiguration.getId(), smppSessionGroup);
        }
    }

    public List<SmppSessionGroupConfiguration> getComponentConfiguration() {
        return groupConfigurations;
    }

    public void setConfiguration(SmppConfiguration configuration) {
        this.configuration = configuration;
    }

    public SmppConfiguration getConfiguration() {
        return configuration;
    }
}
