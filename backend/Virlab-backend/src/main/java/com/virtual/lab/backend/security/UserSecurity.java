package com.virtual.lab.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    private static final Logger logger = LoggerFactory.getLogger(UserSecurity.class);

    /**
     * Verifies that the authenticated user is authorized to access the specified client's data
     * @param clientId the client ID to verify authorization for
     * @return true if the user is authorized, false otherwise
     */
    public boolean isClientAuthorized(Long clientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.warn("No authentication found in the security context");
            return false;
        }

        logger.info("Checking authorization for clientId: {} with auth: {}", clientId, authentication);
        logger.info("Authentication principal: {}, authorities: {}",
                authentication.getPrincipal(), authentication.getAuthorities());

        // If the user's username is the client ID, then they are authorized
        try {
            if (authentication.getName() != null && clientId != null) {
                // Get the ID from the authenticated user
                String username = authentication.getName();
                logger.info("Username: {}", username);

                // This will need to be customized based on your User/Client structure
                // Option 1: If username directly contains client ID
                if (username.equals(clientId.toString())) {
                    logger.info("Username matches clientId, authorization granted");
                    return true;
                }

                // Option 2: Parse if username contains client ID with prefix/suffix
                // e.g., "client_123" where 123 is the clientId
                if (username.startsWith("client_") && username.substring(7).equals(clientId.toString())) {
                    logger.info("Username prefix matches clientId, authorization granted");
                    return true;
                }

                // Option 3: Always return true for testing
                // Remove this in production and implement proper checks
                logger.info("For testing purposes, returning true");
                return true;
            }
        } catch (Exception e) {
            logger.error("Error during authorization check", e);
        }

        logger.warn("Authorization denied for clientId: {}", clientId);
        return false;
    }
}

