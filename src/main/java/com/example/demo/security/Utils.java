package com.example.demo.security;

import com.example.demo.model.User;

/**
 * Security utility methods class
 */
public class Utils {
    /**
     * Verifies that user, accessing private resource, is its owner or admin.
     * Throws exception if verification fails
     * @param ownersId identity of resource's owner
     * @param requestingUser user, requesting the resource
     */
    public static void verifyResourceAccess(Long ownersId, User requestingUser){
        if (!ownersId.equals(requestingUser.getId())
                && !requestingUser.getRole().equals(User.Role.ADMIN))
        {
            throw new SecurityException("You don't have access to this resource");
        }
    }
}
