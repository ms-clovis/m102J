package course;

/**
 * Author Mike Clovis
 * Date: 8/15/2015
 * Time: 2:26 PM
 */
public class Session {
    private String sessionID;
    private String username;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionID='" + sessionID + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (!getSessionID().equals(session.getSessionID())) return false;
        return getUsername().equals(session.getUsername());

    }

    @Override
    public int hashCode() {
        int result = getSessionID().hashCode();
        result = 31 * result + getUsername().hashCode();
        return result;
    }
}
