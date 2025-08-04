# Spring Authorization Server 1.0 with LDAP Integration

A demonstration project showing how to integrate Spring Authorization Server 1.0 with LDAP authentication using Spring Security 6.

📘 Blog Post: [Spring Authorization Server 1.0 with LDAP](https://jarmx.blogspot.com/2023/01/spring-authorization-server-10-with.html)

## Overview

This project demonstrates the implementation of an OAuth 2.1 and OpenID Connect 1.0 authorization server using Spring Authorization Server, integrated with LDAP for user authentication. The server provides a secure, lightweight, and customizable foundation for building OAuth2 Authorization Server products.

## Features

- **OAuth 2.1 & OpenID Connect 1.0** compliance
- **LDAP Authentication** integration
- **Spring Security 6** implementation
- **JWT Token** generation and validation
- **Authorization Code Flow** support
- **Client Credentials Flow** support
- **Refresh Token** support

## Technology Stack

- **Java 17** (minimum requirement)
- **Spring Boot 3.0.1**
- **Spring Authorization Server 1.0**
- **Spring Security 6**
- **Spring LDAP**
- **Maven**
- **Thymeleaf** (for web templates)
- **UnboundID LDAP SDK** (embedded LDAP server)

## Prerequisites

- Java 17 or higher Runtime Environment
- Maven 3.6+
- IDE (IntelliJ IDEA recommended)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/
│   │       ├── config/
│   │       │   ├── AuthorizationServerConfig.java
│   │       │   └── DefaultSecurityConfig.java
│   │       └── controller/
│   │           ├── AuthorizationController.java
│   │           └── HelloResource.java
│   └── resources/
│       ├── application.yml
│       ├── test-server.ldif
│       └── templates/
│           └── landing.html
```

## Dependencies

Add the following dependencies to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-oauth2-authorization-server</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ldap</groupId>
        <artifactId>spring-ldap-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-ldap</artifactId>
    </dependency>
    <dependency>
        <groupId>com.unboundid</groupId>
        <artifactId>unboundid-ldapsdk</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
</dependencies>
```

## Configuration

### Application Configuration (`application.yml`)

```yaml
server:
  port: 9000

spring:
  ldap:
    embedded:
      base-dn: dc=springframework,dc=org
      ldif: classpath:test-server.ldif
      port: 8389
      url: ldap://localhost:8389/

logging:
  level:
    root: INFO
    org.springframework.web: INFO
    org.springframework.security: INFO
    org.springframework.security.oauth2: INFO
```

### LDAP Configuration

The project uses an embedded LDAP server with test data from `test-server.ldif`. The LDAP authentication is configured to:

- Search for users with pattern: `uid={0},ou=people`
- Search for groups in: `ou=groups`
- Use BCrypt password encoding
- Connect to: `ldap://localhost:8389/dc=springframework,dc=org`

### OAuth2 Client Configuration

The authorization server is pre-configured with a test client:

- **Client ID**: `messaging-client`
- **Client Secret**: `secret` (using `{noop}` for demo purposes)
- **Grant Types**: Authorization Code, Refresh Token, Client Credentials
- **Scopes**: `openid`, `profile`, `message.read`, `message.write`
- **Redirect URIs**: 
  - `http://127.0.0.1:9000/login/oauth2/code/messaging-client-oidc`
  - `http://127.0.0.1:9000/authorized`

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/HenryXiloj/spring-security6-spring-authorization-server1.0-LDAP.git
cd spring-security6-spring-authorization-server1.0-LDAP
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run directly from your IDE.

### 4. Access the Application

Open your browser and navigate to:

```
http://localhost:9000/landing
```

### 5. Test Authentication

Use the following test credentials:

- **Username**: `henry`
- **Password**: `123`

## API Endpoints

### OAuth2 Authorization Server Endpoints

- **Authorization Endpoint**: `http://localhost:9000/oauth2/authorize`
- **Token Endpoint**: `http://localhost:9000/oauth2/token`
- **JWK Set Endpoint**: `http://localhost:9000/oauth2/jwks`
- **Token Introspection**: `http://localhost:9000/oauth2/introspect`
- **Token Revocation**: `http://localhost:9000/oauth2/revoke`
- **OpenID Configuration**: `http://localhost:9000/.well-known/openid_configuration`

### Application Endpoints

- **Landing Page**: `http://localhost:9000/landing`
- **Hello Resource**: `http://localhost:9000/hello`

## Testing the Authorization Flow

### 1. Authorization Code Flow

1. Navigate to the authorization endpoint:
   ```
   http://localhost:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=openid%20profile&redirect_uri=http://127.0.0.1:9000/authorized
   ```

2. Login with credentials (`henry` / `123`)

3. Grant consent for the requested scopes

4. You'll be redirected with an authorization code

### 2. Exchange Code for Token

Use the authorization code to get an access token:

```bash
curl -X POST http://localhost:9000/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ=" \
  -d "grant_type=authorization_code&code=YOUR_CODE&redirect_uri=http://127.0.0.1:9000/authorized"
```

## Key Components

### Authorization Server Configuration

- **OAuth2AuthorizationServerConfiguration**: Applies default OAuth2 security configuration
- **RegisteredClientRepository**: Manages OAuth2 client registrations
- **JWKSource**: Provides keys for JWT token signing
- **JwtDecoder**: Decodes and validates JWT tokens
- **AuthorizationServerSettings**: Configures server endpoints and settings

### Security Configuration

- **LDAP Authentication**: Configured to authenticate users against embedded LDAP server
- **Password Encoding**: Uses BCrypt for password hashing
- **Form Login**: Handles user authentication with login forms

## Customization

### Adding New OAuth2 Clients

Modify the `RegisteredClientRepository` bean in `AuthorizationServerConfig.java`:

```java
@Bean
public RegisteredClientRepository registeredClientRepository() {
    RegisteredClient newClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("your-client-id")
        .clientSecret("{noop}your-secret")
        // ... other configurations
        .build();
    
    return new InMemoryRegisteredClientRepository(existingClient, newClient);
}
```

### LDAP Server Configuration

To use an external LDAP server, update the `application.yml`:

```yaml
spring:
  ldap:
    urls: ldap://your-ldap-server:389
    base: dc=yourcompany,dc=com
    username: cn=admin,dc=yourcompany,dc=com
    password: your-password
```

## Security Considerations

- **Production Secrets**: Replace `{noop}` password encoding with proper password encoders
- **HTTPS**: Use HTTPS in production environments
- **Key Management**: Use proper key management solutions for JWT signing keys
- **LDAP Security**: Secure LDAP connections with SSL/TLS

## Troubleshooting

### Common Issues

1. **Port Conflicts**: Ensure ports 9000 and 8389 are available
2. **LDAP Connection**: Verify embedded LDAP server starts correctly
3. **JWT Keys**: Check that RSA key generation works properly

### Logging

Enable debug logging for troubleshooting:

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.boot.autoconfigure: DEBUG
```

## References

- [Spring Authorization Server Documentation](https://spring.io/projects/spring-authorization-server)
- [Spring Security LDAP Authentication](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/ldap.html)
- [Spring Boot LDAP Guide](https://spring.io/guides/gs/authenticating-ldap/)
- [OAuth 2.1 Specification](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-01)
- [OpenID Connect 1.0 Specification](https://openid.net/specs/openid-connect-core-1_0.html)

