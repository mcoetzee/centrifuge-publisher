Centrifuge Publisher
====================
This is a Java client to publish messages to Centrifuge. 
###[Centrifuge](https://centrifuge.readthedocs.org/en/latest/index.html)###
Centrifuge is a self-hosted, open-source, real-time messaging server.
> It is built on top of Tornado, an extremely fast and mature Python async web server. Centrifuge uses ZeroMQ steroid sockets for internal communication and publish/subscribe operations. For presence and history data Centrifuge utilizes Redis - advanced and super fast in memory key-value store. To connect to Centrifuge from browser pure Websockets or **SockJS** library can be used. 

The technology stack looks great, which includes the same [SockJS](https://github.com/sockjs/sockjs-client) Javascript library as what [Pusher](http://www.pusher.com) uses. It provides a web interface for the admin to manage projects/namespaces and to publish messages to any channel (which will be nice way to publish system messages to everyone when you do a release). Centrifuge is being developed to be used in production. The main developer https://github.com/FZambia is active and is committing frequently and you can find the code here: https://github.com/FZambia/centrifuge

This Java client has been tested with version 0.3.7 of Centrifuge. Seeing as Centrifuge is still undergoing development, some updates might be required in the future in order for this client to work with the latest version of Centrifuge.

To make use of this Java client you can add this Maven repository to your pom.xml:

```XML
        <repository>
            <id>centrifuge-publisher-repo</id>
            <url>https://github.com/mcoetzee/centrifuge-publisher-repo/raw/master</url>
            <!-- use snapshot version -->
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
```
And then the dependency:
```XML
        <dependency>
            <groupId>centrifuge-publisher</groupId>
            <artifactId>centrifuge-publisher</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

Typical usage will be to wire these three classes in as Spring beans:

```XML
    <bean id="centrifugeMessagePublisher" class="org.centrifuge.CentrifugeMessagePublisher">
        <property name="centrifugeApi" value="${centrifuge.api}"/>
        <property name="centrifugeProjectId" value="${centrifuge.project.id}"/>
        <property name="centrifugePublicNamespace" value="${centrifuge.public.namespace}"/>
        <property name="centrifugePrivateNamespace" value="${centrifuge.private.namespace}"/>
        <property name="centrifugeMessageEncoder" ref="centrifugeMessageEncoder"/>
        <property name="jsonUtility" ref="jsonUtility"/>
    </bean>

    <bean id="jsonUtility" class="org.centrifuge.JsonUtility"/>
    <bean id="centrifugeMessageEncoder" class="org.centrifuge.CentrifugeMessageEncoder">
        <property name="realTimeMessagingProjectId" value="${centrifuge.project.id}"/>
        <property name="realTimeMessagingProjectSecret" value="${centrifuge.project.secret}"/>
    </bean>
```

To generate the user token to be provided when connecting to Centrifuge from the Javascript client, you can simply use the `CentrifugeMessageEncoder` class:
```Java
String userToken = centrifugeMessageEncoder.encodeMessage(Long.toString(user.getId()));
```

To publish a message to the public namespace:
```Java
centrifugeMessagePublisher.publishPublicMessage(channel, message);
```
To publish a message to the private namespace:
```Java
centrifugeMessagePublisher.publishPrivateMessage(channel, message);
```
To publish a message to a custom namespace:
```Java
centrifugeMessagePublisher.publishMessage(namespace, channel, message);
```
Note that this `message` Object will be serialized to JSON and its fields will need to have public access modifiers in order to be serialized and subsequently sent to the Centrifuge API.

