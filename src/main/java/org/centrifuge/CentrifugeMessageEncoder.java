package org.centrifuge;

/**
 * Encodes messages with the Centrifuge project secret and project id.
 */
public class CentrifugeMessageEncoder extends MessageEncoder {

    private String realTimeMessagingProjectId;
    private String realTimeMessagingProjectSecret;

    public void setRealTimeMessagingProjectId(String realTimeMessagingProjectId) {
        this.realTimeMessagingProjectId = realTimeMessagingProjectId;
    }

    public void setRealTimeMessagingProjectSecret(String realTimeMessagingProjectSecret) {
        this.realTimeMessagingProjectSecret = realTimeMessagingProjectSecret;
    }

    /**
     * Encodes the given message with the project's secret and id.
     *
     * @param message The message in question.
     * @return A hex encoded HMac MD5 hash.
     */
    public String encodeMessage(String message) {
        return hexEncodedHMacMD5Hash(realTimeMessagingProjectSecret, realTimeMessagingProjectId, message);
    }
}
