package org.apache.camel.component.smpp;

import org.apache.camel.spi.Metadata;

@Metadata
public class SmppSessionGroupConfiguration {
    @Metadata(required = true)
    private String id;
    @Metadata(required = true)
    private int pduProcessorCoreDegree;
    @Metadata(required = true)
    private int pduProcessorMaxDegree;
    @Metadata(required = true)
    private int pduProcessorKeepAliveMillis;
    @Metadata(required = true)
    private int pduProcessorQueueCapacity;
    private String metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public int getPduProcessorCoreDegree() {
        return pduProcessorCoreDegree;
    }

    public void setPduProcessorCoreDegree(int pduProcessorCoreDegree) {
        this.pduProcessorCoreDegree = pduProcessorCoreDegree;
    }

    public int getPduProcessorMaxDegree() {
        return pduProcessorMaxDegree;
    }

    public void setPduProcessorMaxDegree(int pduProcessorMaxDegree) {
        this.pduProcessorMaxDegree = pduProcessorMaxDegree;
    }

    public int getPduProcessorKeepAliveMillis() {
        return pduProcessorKeepAliveMillis;
    }

    public void setPduProcessorKeepAliveMillis(int pduProcessorKeepAliveMillis) {
        this.pduProcessorKeepAliveMillis = pduProcessorKeepAliveMillis;
    }

    public int getPduProcessorQueueCapacity() {
        return pduProcessorQueueCapacity;
    }

    public void setPduProcessorQueueCapacity(int pduProcessorQueueCapacity) {
        this.pduProcessorQueueCapacity = pduProcessorQueueCapacity;
    }
}
