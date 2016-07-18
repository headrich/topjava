package ru.headrich.topjava.util.converters;

import com.sun.istack.internal.Nullable;
import org.mindrot.jbcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * Hash passwords for storage, and test passwords against password tokens.
 *
 * Instances of this class can be used concurrently by multiple threads.
 *
 * @author erickson
 * @see <a href="http://stackoverflow.com/a/2861125/3474">StackOverflow</a>
 */
/**
 * Created by Montana on 30.06.2016.
 */
public class PasswordEncryption {

    static PasswordEncryptor encryptor1 =new PasswordHasher1();
    static PasswordEncryptor encryptor2 =new PasswordHasher2();
    static PasswordEncryptor encryptor3 =new BCryptHasher();


    public static void setEncryptor(int type){
        switch(type){
            case 1:    encryptor1 =new PasswordHasher1();
            case 2:   encryptor1 =new PasswordHasher2();
            case 3:   encryptor1 =new BCryptHasher();
            default: encryptor1 =new PasswordHasher1();
        }

    }
    public static PasswordEncryptor getEncryptor(){
        return encryptor1;
    }



    static final  class PasswordHasher1 implements PasswordEncryptor
    {

        /**
         * Each token produced by this class uses this identifier as a prefix.
         */
        public static final String ID = "$31$";

        /**
         * The minimum recommended cost, used by default
         */
        public static final int DEFAULT_COST = 16;

        private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

        private static final int SIZE = 128;

        private  final Pattern layout = Pattern.compile("\\$31\\$(\\d\\d?)\\$(.{43})");

        private final SecureRandom random;

        private final int cost;

        public PasswordHasher1()
        {
            this(DEFAULT_COST);
        }

        /**
         * Create a password manager with a specified cost
         *
         * @param cost the exponential computational cost of hashing a password, 0 to 30
         */
        public PasswordHasher1(int cost)
        {
            iterations(cost); /* Validate cost */
            this.cost = cost;
            this.random = new SecureRandom();
        }

        private  int iterations(int cost)
        {
            if ((cost & ~0x1F) != 0)
                throw new IllegalArgumentException("cost: " + cost);
            return 1 << cost;
        }

        /**
         * Hash a password for storage.
         *
         * @return a secure authentication token to be stored for later authentication
         */
        public String getHash(char[] password)
        {
            byte[] salt = new byte[SIZE / 8];
            random.nextBytes(salt);
            byte[] dk = pbkdf2(password, salt, 1 << cost);
            byte[] hash = new byte[salt.length + dk.length];
            System.arraycopy(salt, 0, hash, 0, salt.length);
            System.arraycopy(dk, 0, hash, salt.length, dk.length);
            Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
            return ID + cost + '$' + enc.encodeToString(hash);
        }

        /**
         * Authenticate with a password and a stored password token.
         *
         * @return true if the password and token match
         */
        public boolean authenticate(char[] password, String token)
        {

            Matcher m = layout.matcher(token);
            if (!m.matches())
                throw new IllegalArgumentException("Invalid token format");
            int iterations = iterations(Integer.parseInt(m.group(1)));
            byte[] hash = Base64.getUrlDecoder().decode(m.group(2));
            byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
            byte[] check = pbkdf2(password, salt, iterations);
            int zero = 0;
            for (int idx = 0; idx < check.length; ++idx)
                zero |= hash[salt.length + idx] ^ check[idx];
            return zero == 0;
        }


        private  byte[] pbkdf2(char[] password, byte[] salt, int iterations)
        {
            KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
            try {
                SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
                return f.generateSecret(spec).getEncoded();
            }
            catch (NoSuchAlgorithmException ex) {
                throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
            }
            catch (InvalidKeySpecException ex) {
                throw new IllegalStateException("Invalid SecretKeyFactory", ex);
            }
        }

        /**
         * Hash a password in an immutable {@code String}.
         *
         * <p>Passwords should be stored in a {@code char[]} so that it can be filled
         * with zeros after use instead of lingering on the heap and elsewhere.
         *
         * @deprecated Use {@link #getHash(char[])} instead
         */
        @Deprecated
        public String getHash(String password)
        {
            return (password!=null) ? getHash(password.toCharArray()) : "";
        }

        /**
         * Authenticate with a password in an immutable {@code String} and a stored
         * password token.
         *
         * @deprecated Use {@link #authenticate(char[],String)} instead.
         * @see #getHash(String)
         */
        @Deprecated
        public boolean authenticate(String password, String token)
        {
            return authenticate(password.toCharArray(), token);
        }

        @Override
        public String decode(String hash, @Nullable String salt) {
            return null;
        }

    }


    static final  class PasswordHasher2 implements PasswordEncryptor{
        // The higher the number of iterations the more
        // expensive computing the hash is for us and
        // also for an attacker.
        private static final int iterations = 20*1000;
        private static final int saltLen = 32;
        private static final int desiredKeyLen = 256;

        /** Computes a salted PBKDF2 hash of given plaintext password
         suitable for storing in a database.
         Empty passwords are not supported. */
        public  String getHash(String password) throws Exception {
            byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
            // store the salt with the password
            return Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt);
        }

        /** Checks whether given plaintext password corresponds
         to a stored salted hash of the password. */
        public  boolean authenticate(String password, String stored) throws Exception{
            String[] saltAndPass = stored.split("\\$");
            if (saltAndPass.length != 2) {
                throw new IllegalStateException(
                        "The stored password have the form 'salt$hash'");
            }
            String hashOfInput = hash(password, Base64.getDecoder().decode(saltAndPass[0]));
            return hashOfInput.equals(saltAndPass[1]);
        }

        @Override
        public String decode(String hash, @Nullable String salt) {
            return null;
        }

        // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
        // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
        private  String hash(String password, byte[] salt) throws Exception {
            if (password == null || password.length() == 0)
                throw new IllegalArgumentException("Empty passwords are not supported.");
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey key = f.generateSecret(new PBEKeySpec(
                    password.toCharArray(), salt, iterations, desiredKeyLen)
            );
            return Base64.getEncoder().encodeToString(key.getEncoded());
        }
    }

    static final  class BCryptHasher implements PasswordEncryptor{

    //some settings

        @Override
        public String getHash(String password) throws Exception {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        }

        @Override
        public boolean authenticate(String password, String salt) throws Exception {
            return BCrypt.checkpw(password, salt);
        }

        @Override
        public String decode(String hash, @Nullable String salt) {
            return null;
        }
    }


}




