/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package course;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;

public class UserDAO {
    private final MongoCollection<Document> usersCollection;
    private Random random = new SecureRandom();

    public UserDAO(final MongoDatabase blogDatabase) {

        usersCollection = blogDatabase.getCollection("users");
    }



    // validates that username is unique and insert into db
    public boolean addUser(final String username,final String password,final String email) {
        boolean addedUser = false;

        // this is just how I decided to do it using a User class
        // It can just as easily be Document user
        // because at this point in our learning
        // User user
        // user.setUsername(username)
        // is the same as
        // Document user
        // user.append("username",username)


        User user = new User();
        String passwordHash = makePasswordHash(password, Integer.toString(random.nextInt()));

        user.setUsername(username);
        user.setPassword(passwordHash);
        // create an object suitable for insertion into the user collection
        // be sure to add username and hashed password to the document. problem instructions
        // will tell you the schema that the documents must follow.



        if (email != null && !email.equals("")) {
            user.setEmail(email);
            // if there is an email address specified, add it to the document too.
        }

        try {
            Document doc =  new Document(user.getFields());
            usersCollection.insertOne(doc);

            // insert the document into the user collection here


            addedUser = true;
        } catch (MongoWriteException e) {
            if (e.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("Username already in use: " + username);
                addedUser = false;
            }
            throw e;
        }finally {
            // close something
            return addedUser;
        }
    }

    public Document validateLogin(String username, String password) {
        Document user = null;


        user = usersCollection.find(new Document("_id",username)).first();
        // assign the result to the user variable.

        if (user == null) {
            System.out.println("User not in database");
            return null;
        }

        String hashedAndSalted = user.get("password").toString();

        String salt = hashedAndSalted.split(",")[1];

        if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
            System.out.println("Submitted password is not a match");
            return null;
        }

        return user;
    }


    private String makePasswordHash(String password, String salt) {
        try {
            String saltedAndHashed = password + "," + salt;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(saltedAndHashed.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
            return encoder.encode(hashedBytes) + "," + salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 is not available", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
        }
    }
}
