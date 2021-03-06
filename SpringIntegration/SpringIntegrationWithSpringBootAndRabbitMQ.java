	Spring Integration with Spring Boot and RabbotMQ:
						Gary Russel

Link: https://www.youtube.com/watch?v=RMD42XixCxY&t=1202s

Spring Integration: implements EIP
0.This is a slide: {
  - Integration Styles:
    + File Transfer
    + Shared Database
    + Remoting
    + Messaging
}
1.Introduction: {
  - Integrating Enterprise Applications can be done in many ways:
  - Each way has its own pros and cons
  - Best solution depends on requirements
  - Things to consider:
    + Coupling (logical, temporal)
    + Synchronous or asynchronous
    + Overhead
    + Data formats
    + Reliability
    + Security
}
2.Spring Integration: {
  - Spring Integration allows you to:
    + Let application components exchange data through in-memory messaging
    + Integrate with external applications in a variety of ways through adapters
  - Origins:
    + Builds on Enterprise Integration Patterns for both
    + Builds on the Spring portfolio & programming model
    + http://projects.spring.io/spring-integration
}
3.Example: {
  - Messaging
}
4.Benefits: {
  - Loose coupling between components
    + Small, focused components
    + Eases testing, reuse, etc.
  - Event-driven architecture
    + No hard-coded process flow
    + Easy to change or expand
  - Separates integration and processing logic
    + Framework handles routing, transformation, etc.
    + Easily switch between sync & async processing 
}
5.Introduction: {
  - Channel Adapter: One way integration
    + message enters or leaves application
    + Called 'inbound' or 'outbound'
  - Gateway: Two way integration:
	+ Bring message into application and wait for response(inbound), or
	invoke external system and feed respose back into application (outbound)
	+ Service Activator
	+ Call method and wrap result in response message
	+ Basically outbound gateway for invoking bean method
}
6.Demo: {
  - github/spring-projects/spring-integration-samples
  - github/spring-projects/spring-integration-extension(s)
  - Java DSL
  6.1.Xml: {
    - Server: servlet-config.xml: {
      <int-http:inbound-gateway request-channel="receiveChannel"
				path="/receiveGateway"
				supported-methods="POST"/>

      <int:channel id="receiveChannel"/>

      <int:chain input-channel="receiveChannel">
        <int:transformer expression="payload + ' from the other side'"/>
        <int:transformer expression="payload.toUpperCase()"/>
      </int:chain>
    }
    - Client: http-outbound-config.xml: {
      <int:gateway id="requestGateway"
		   service-interface="org.springframework.integration.samples.http.RequestGateway"
		   default-request-channel="requestChannel"/>

      <int:channel id="requestChannel"/>
 
      <int-http:outbound-gateway request-channel-"requestChannel"
				 url="http://localhost:8080/http/receiveGateway"
				 http-method="POST"
				 expeced-response-type="java.lang.String"/>

    }
  }
}
24:25
