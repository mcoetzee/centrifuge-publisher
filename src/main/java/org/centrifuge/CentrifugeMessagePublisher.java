package org.centrifuge;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Publishes messages to the Centrifuge API.
 */
public class CentrifugeMessagePublisher {

    private Logger logger = Logger.getLogger(getClass());

    private String centrifugeApi;
    private String centrifugeProjectId;
    private String centrifugePublicNamespace;
    private String centrifugePrivateNamespace;

    private CentrifugeMessageEncoder centrifugeMessageEncoder;
    private JsonUtility jsonUtility;

    public void setCentrifugeApi(String centrifugeApi) {
        this.centrifugeApi = centrifugeApi;
    }

    public void setCentrifugeProjectId(String centrifugeProjectId) {
        this.centrifugeProjectId = centrifugeProjectId;
    }

    public void setCentrifugePublicNamespace(String centrifugePublicNamespace) {
        this.centrifugePublicNamespace = centrifugePublicNamespace;
    }

    public void setCentrifugePrivateNamespace(String centrifugePrivateNamespace) {
        this.centrifugePrivateNamespace = centrifugePrivateNamespace;
    }

    public void setCentrifugeMessageEncoder(CentrifugeMessageEncoder centrifugeMessageEncoder) {
        this.centrifugeMessageEncoder = centrifugeMessageEncoder;
    }

    public void setJsonUtility(JsonUtility jsonUtility) {
        this.jsonUtility = jsonUtility;
    }

    /**
     * Publishes the given message Object to the given channel's public namespace. The Object's fields need to
     * have public access modifiers for it to be serialized.
     *
     * @param channel The channel in question.
     * @param message The message Object in question.
     */
    public void publishPublicMessage(String channel, Object message) {
        publishMessage(centrifugePublicNamespace, channel, message);
    }

    /**
     * Publishes the given message Object to the given channel's private namespace. The Object's fields need to
     * have public access modifiers for it to be serialized to JSON.
     *
     * @param channel The channel in question.
     * @param message The message Object in question.
     */
    public void publishPrivateMessage(String channel, Object message) {
        publishMessage(centrifugePrivateNamespace, channel, message);
    }

    /**
     * Publishes the given message Object to the given channel and namespace. The Object's fields need to
     * have public access modifiers for it to be serialized to JSON.
     *
     * @param namespace The namespace in question.
     * @param channel The channel in question.
     * @param message The message Object in question.
     */
    public void publishMessage(String namespace, String channel, Object message) {
        try {
            HttpClient httpClient  = new DefaultHttpClient();
            HttpPost httpPost  = new HttpPost(centrifugeApi + "/" + centrifugeProjectId);

            CarrierPigeon carrierPigeon = new CarrierPigeon("publish", namespace, channel, message);
            String carrierPigeonJsonString = jsonUtility.serializeToJson(carrierPigeon);
            logger.debug("Publishing message to Centrifuge: " + carrierPigeonJsonString);

            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("data", carrierPigeonJsonString));
            postParameters.add(new BasicNameValuePair("sign", centrifugeMessageEncoder.encodeMessage(carrierPigeonJsonString)));
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            httpClient.execute(httpPost);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * This class gets serialized to JSON by {@link JsonUtility} and hence requires its fields to have
     * public access modifiers.
     */
    @SuppressWarnings("unused")
    private class CarrierPigeon {
        private String method;
        private Params params;

        public CarrierPigeon(String method, String namespace, String channel, Object data) {
            this.method = method;
            this.params = new Params(namespace, channel, data);
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Params getParams() {
            return params;
        }

        public void setParams(Params params) {
            this.params = params;
        }
    }

    /**
     * This class gets serialized to JSON by {@link JsonUtility} and hence requires its fields to have
     * public access modifiers.
     */
    @SuppressWarnings("unused")
    private class Params {
        private String namespace;
        private String channel;
        private Object data;

        public Params(String namespace, String channel, Object data) {
            this.namespace = namespace;
            this.channel = channel;
            this.data = data;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
