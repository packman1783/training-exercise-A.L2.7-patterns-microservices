**Spring Boot service Gateway Service which implements several patterns:**  

 - API Gateway is a single entry point GatewayController for all client requests 
 - Service Discovery dynamic determination of service addresses from the application.properties
 - Circuit Breaker after three requests errors "breaks the chain" for 5 seconds, then tries to connect again.
 - External Configuration route configuration via application.properties without recompilation




The gateway listens on http://localhost:8082.  
A GET /api/users/1 request is routed to the UserService → http://localhost:8080  
A POST /api/notifications request is routed to the NotificationService → http://localhost:8081

Check the operation:
```
curl http://localhost:8082/api/users/1
curl -X POST http://localhost:8082/api/notifications -d '{"text1": "text2"}' -H "Content-Type: application/json"
```

Thanks to this project it was possible to learn:  

- work with REST API
- Spring Boot