package course;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Author Mike Clovis
 * Date: 8/15/2015
 * Time: 2:23 PM
 */
public class User {
    private Map <String,Object> fields = new HashMap<String, Object>();
    private String username;
    private String password;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
        fields.put("email",email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.fields.put("password",password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.fields.put("_id",username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!getUsername().equals(user.getUsername())) return false;
        return password.equals(user.password);

    }

    @Override
    public int hashCode() {
        int result = getUsername().hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" +email    + '\'' +
                '}';
    }

    public String toJson(){
        StringBuffer sb = new StringBuffer("{ ");
        String key;
        for (Iterator iterator = fields.keySet().iterator(); iterator.hasNext(); ) {
            key =(String) iterator.next();
            sb.append(key).append(":").append(fields.get(key)).append(", ");

        }
        sb = sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("}");
        return sb.toString();

    }

    public Map<String, Object> getFields() {
        return fields;
    }
}
