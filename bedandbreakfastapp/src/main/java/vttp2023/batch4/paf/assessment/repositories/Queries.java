package vttp2023.batch4.paf.assessment.repositories;

public class Queries {
    
    // check if user exists in users db 
    // SELECT EXISTS
	// (SELECT * FROM users
	// 	WHERE email = "fred@gmail.com")
	// 	AS RESULT;
    public static final String SQL_CHECK_USER_EXISTS =
    """
        SELECT EXISTS
            (SELECT * FROM users 
                WHERE email = ?)
            AS result
    """;

    // create user in db 
    // INSERT INTO users (email, name)
	// VALUES ("joyie@gmail.com", "jo yie")
    public static final String SQL_CREATE_USER = 
    """
        INSERT INTO USERS (email, name)
            VALUES (?, ?)
    """;

    // create booking in bookings db 
    // INSERT INTO bookings (booking_id, listing_id, duration, email)
    // VALUES (?, ?, ?, ?)
    public static final String SQL_CREATE_BOOKING = 
    """
        INSERT INTO bookings (booking_id, listing_id, duration, email)
            VALUES (?, ?, ?, ?) 
    """;

}
