package com.trainreservation;

public final class UserSession {

    private static long userId;
    private static String fullName;
    private static String role;
    private static boolean loggedIn;

    private UserSession() {
    }

    public static void start(
        long id,
        String name,
        String userRole
    ) {
        userId = id;
        fullName = name;
        role = userRole;
        loggedIn = true;
    }

    public static void clear() {
        userId = 0;
        fullName = null;
        role = null;
        loggedIn = false;
    }

    public static long getUserId() {
        return userId;
    }

    public static String getFullName() {
        return fullName;
    }

    public static String getRole() {
        return role;
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static boolean isAdmin() {
        return loggedIn
            && "ADMIN".equalsIgnoreCase(role);
    }

    public static boolean isCustomer() {
        return loggedIn
            && "CUSTOMER".equalsIgnoreCase(role);
    }
}