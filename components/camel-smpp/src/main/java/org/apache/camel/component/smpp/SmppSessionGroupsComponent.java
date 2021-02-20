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
import org.jsmpp.session.SmppSessionGroup;
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

    private Map<String, SmppSessionGroup> smppSessionGroups = new HashMap<>();

    @Override
    public void doStart() throws Exception {
        super.doStart();

        for (SmppSessionGroupConfiguration groupConfiguration : groupConfigurations) {
            SmppSessionGroup smppSessionGroup = new SmppSessionGroup(
                    groupConfiguration.getPduProcessorCoreDegree(),
                    groupConfiguration.getPduProcessorMaxDegree(), groupConfiguration.getPduProcessorQueueCapacity());
            smppSessionGroups.put(groupConfiguration.getId(), smppSessionGroup);
        }
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        if (this.configuration == null) {
            this.configuration = new SmppConfiguration();
        }
        // create a copy of the configuration as other endpoints can adjust their copy as well
        SmppConfiguration config = this.configuration.copy();
        config.configureFromURI(new URI(uri));
        String groupId = config.getSessionGroupId();
        if (groupId == null) {
            LOG.warn("No group id set for host: {} - port: {} - system type: {} - system id: {}. " +
                     "Returning a standalone SmppEndpoint.",
                    config.getHost(),
                    config.getPort(), config.getSystemType(), config.getSystemId());
            return new SmppEndpoint(uri, this, config);
        }
        SmppSessionGroup sessionGroup = smppSessionGroups.get(groupId);
        if (sessionGroup == null) {
            LOG.warn("Group id {} has not been configured. Creating it with default parameters. " +
                     "This will most likely negatively impact performance." +
                     config.getSessionGroupId());
            sessionGroup = new SmppSessionGroup();
            smppSessionGroups.put(groupId, sessionGroup);
        }
        return new SmppSessionGroupsEndpoint(sessionGroup, uri, this, config);
    }

    /**
     * Smpp groups configuration used to generate and initialize smpp session groups.
     */
    public void setGroupConfigurations(List<SmppSessionGroupConfiguration> groupConfigurations) {
        this.groupConfigurations = groupConfigurations;
    }

    public List<SmppSessionGroupConfiguration> getGroupConfigurations() {
        return groupConfigurations;
    }

    public void setConfiguration(SmppConfiguration configuration) {
        this.configuration = configuration;
    }

    public SmppConfiguration getConfiguration() {
        return configuration;
    }
}
