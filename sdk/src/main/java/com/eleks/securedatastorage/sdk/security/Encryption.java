/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eleks.securedatastorage.sdk.security;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A class to make more easy and simple the encrypt routines, this is the core of Encryption library
 */
public class Encryption {

    /**
     * We use this tag to log errors on LogCat, never the password or sensible data
     */
    private static final String TAG = "Encryption";

    /**
     * The Builder used to create the Encryption instance and that contains the information about
     * encryption specifications, this instance need to be private and careful managed
     */
    private final Builder mBuilder;

    /**
     * The private and unique constructor, you should use the Encryption.Builder to build your own
     * instance or get the default proving just the sensible information about encryption
     */
    Encryption(Builder builder) {
        mBuilder = builder;
    }

    /**
     * @return an default encryption instance or {@code null} if occur some Exception, you can
     * create yur own Encryption instance using the Encryption.Builder
     */
    public static Encryption getDefault(String key, String salt, byte[] iv) {
        try {
            return Builder.getDefaultBuilder(key, salt, iv).build();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static Encryption getDefault(String salt, byte[] iv) {
        try {
            return Builder.getDefaultBuilder(null, salt, iv).build();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Encrypt a String
     *
     * @param data the String to be encrypted
     * @return the encrypted String or {@code null} if you send the data as {@code null}
     * @throws UnsupportedEncodingException       if the Builder charset name is not supported or if
     *                                            the Builder charset name is not supported
     * @throws NoSuchAlgorithmException           if the Builder digest algorithm is not available
     *                                            or if this has no installed provider that can
     *                                            provide the requested by the Builder secret key
     *                                            type or it is {@code null}, empty or in an invalid
     *                                            format
     * @throws NoSuchPaddingException             if no installed provider can provide the padding
     *                                            scheme in the Builder digest algorithm
     * @throws InvalidAlgorithmParameterException if the specified parameters are inappropriate for
     *                                            the cipher
     * @throws InvalidKeyException                if the specified key can not be used to initialize
     *                                            the cipher instance
     * @throws InvalidKeySpecException            if the specified key specification cannot be used
     *                                            to generate a secret key
     * @throws BadPaddingException                if the padding of the data does not match the
     *                                            padding scheme
     * @throws IllegalBlockSizeException          if the size of the resulting bytes is not a
     *                                            multiple of the cipher block size
     * @throws NullPointerException               if the Builder digest algorithm is {@code null} or
     *                                            if the specified Builder secret key type is
     *                                            {@code null}
     * @throws IllegalStateException              if the cipher instance is not initialized for
     *                                            encryption or decryption
     */
    private String encrypt(String data) throws UnsupportedEncodingException,
            NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchPaddingException {
        if (data == null) return null;
        SecretKey secretKey = getSecretKey(hashTheKey(mBuilder.getKey()));
        return encrypt(secretKey, data);
    }

    private String encrypt(SecretKey secretKey, String data) throws UnsupportedEncodingException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] dataBytes = data.getBytes(mBuilder.getCharsetName());
        Cipher cipher = Cipher.getInstance(mBuilder.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, mBuilder.getIvParameterSpec(),
                mBuilder.getSecureRandom());
        return Base64.encodeToString(cipher.doFinal(dataBytes), mBuilder.getBase64Mode());
    }

    /**
     * This is a sugar method that calls encrypt method and catch the exceptions returning
     * {@code null} when it occurs and logging the error
     *
     * @param data the String to be encrypted
     * @return the encrypted String or {@code null} if you send the data as {@code null}
     */
    public String encryptOrNull(String data) {
        try {
            return encrypt(data);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public String encryptOrNull(SecretKey secretKey, String data) {
        try {
            return encrypt(secretKey, data);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    /**
     * This is a sugar method that calls encrypt method in background, it is a good idea to use this
     * one instead the default method because encryption can take several time and with this method
     * the process occurs in a AsyncTask, other advantage is the Callback with separated methods,
     * one for success and other for the exception
     *
     * @param data     the String to be encrypted
     * @param callback the Callback to handle the results
     */
    public void encryptAsync(String data, final Callback callback) {
        if (callback == null) return;
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String encrypt = encrypt(params[0]);
                    if (encrypt == null) {
                        callback.onError(new Exception("Encrypt return null, it normally occurs when you send a null data"));
                    }
                    return encrypt;
                } catch (Exception e) {
                    callback.onError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) callback.onSuccess(result);
            }

        }.execute(data);
    }

    /**
     * Decrypt a String
     *
     * @param data the String to be decrypted
     * @return the decrypted String or {@code null} if you send the data as {@code null}
     * @throws UnsupportedEncodingException       if the Builder charset name is not supported or if
     *                                            the Builder charset name is not supported
     * @throws NoSuchAlgorithmException           if the Builder digest algorithm is not available
     *                                            or if this has no installed provider that can
     *                                            provide the requested by the Builder secret key
     *                                            type or it is {@code null}, empty or in an invalid
     *                                            format
     * @throws NoSuchPaddingException             if no installed provider can provide the padding
     *                                            scheme in the Builder digest algorithm
     * @throws InvalidAlgorithmParameterException if the specified parameters are inappropriate for
     *                                            the cipher
     * @throws InvalidKeyException                if the specified key can not be used to initialize
     *                                            the cipher instance
     * @throws InvalidKeySpecException            if the specified key specification cannot be used
     *                                            to generate a secret key
     * @throws BadPaddingException                if the padding of the data does not match the
     *                                            padding scheme
     * @throws IllegalBlockSizeException          if the size of the resulting bytes is not a
     *                                            multiple of the cipher block size
     * @throws NullPointerException               if the Builder digest algorithm is {@code null} or
     *                                            if the specified Builder secret key type is
     *                                            {@code null}
     * @throws IllegalStateException              if the cipher instance is not initialized for
     *                                            encryption or decryption
     */
    private String decrypt(String data) throws UnsupportedEncodingException,
            NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        if (data == null) return null;
        SecretKey secretKey = getSecretKey(hashTheKey(mBuilder.getKey()));
        return decrypt(secretKey, data);
    }

    private String decrypt(SecretKey secretKey, String data) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        byte[] dataBytes = Base64.decode(data, mBuilder.getBase64Mode());
        Cipher cipher = Cipher.getInstance(mBuilder.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, mBuilder.getIvParameterSpec(),
                mBuilder.getSecureRandom());
        byte[] dataBytesDecrypted = (cipher.doFinal(dataBytes));
        return new String(dataBytesDecrypted);
    }

    /**
     * This is a sugar method that calls decrypt method and catch the exceptions returning
     * {@code null} when it occurs and logging the error
     *
     * @param data the String to be decrypted
     * @return the decrypted String or {@code null} if you send the data as {@code null}
     */
    public String decryptOrNull(String data) {
        try {
            return decrypt(data);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public String decryptOrNull(SecretKey secretKey, String data) {
        try {
            return decrypt(secretKey, data);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    /**
     * This is a sugar method that calls decrypt method in background, it is a good idea to use this
     * one instead the default method because decryption can take several time and with this method
     * the process occurs in a AsyncTask, other advantage is the Callback with separated methods,
     * one for success and other for the exception
     *
     * @param data     the String to be decrypted
     * @param callback the Callback to handle the results
     */
    public void decryptAsync(String data, final Callback callback) {
        if (callback == null) return;
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                try {
                    String decrypt = decrypt(params[0]);
                    if (decrypt == null) {
                        callback.onError(new Exception("Decrypt return null, it normally occurs when you send a null data"));
                    }
                    return decrypt;
                } catch (Exception e) {
                    callback.onError(e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) callback.onSuccess(result);
            }

        }.execute(data);
    }

    /**
     * creates a 128bit salted aes key
     *
     * @param key encoded input key
     * @return aes 128 bit salted key
     * @throws NoSuchAlgorithmException     if no installed provider that can provide the requested
     *                                      by the Builder secret key type
     * @throws UnsupportedEncodingException if the Builder charset name is not supported
     * @throws InvalidKeySpecException      if the specified key specification cannot be used to
     *                                      generate a secret key
     * @throws NullPointerException         if the specified Builder secret key type is {@code null}
     */
    private SecretKey getSecretKey(char[] key) throws NoSuchAlgorithmException,
            UnsupportedEncodingException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(mBuilder.getSecretKeyType());
        KeySpec spec = new PBEKeySpec(key, mBuilder.getSalt().getBytes(mBuilder.getCharsetName()),
                mBuilder.getIterationCount(), mBuilder.getKeyLength());
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), mBuilder.getAlgorithm());
    }

    public SecretKey getSecretKey()
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
        return getSecretKey(hashTheKey(mBuilder.getKey()));
    }

    /**
     * takes in a simple string and performs an sha1 hash
     * that is 128 bits long...we then base64 encode it
     * and return the char array
     *
     * @param key simple inputted string
     * @return sha1 base64 encoded representation
     * @throws UnsupportedEncodingException if the Builder charset name is not supported
     * @throws NoSuchAlgorithmException     if the Builder digest algorithm is not available
     * @throws NullPointerException         if the Builder digest algorithm is {@code null}
     */
    private char[] hashTheKey(String key) throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(mBuilder.getDigestAlgorithm());
        messageDigest.update(key.getBytes(mBuilder.getCharsetName()));
        return Base64.encodeToString(messageDigest.digest(), Base64.NO_PADDING).toCharArray();
    }

    /**
     * When you encrypt or decrypt in callback mode you get noticed of result using this interface
     */
    public static interface Callback {

        /**
         * Called when encrypt or decrypt job ends and the process was a success
         *
         * @param result the encrypted or decrypted String
         */
        public void onSuccess(String result);

        /**
         * Called when encrypt or decrypt job ends and has occurred an error in the process
         *
         * @param exception the Exception related to the error
         */
        public void onError(Exception exception);

    }

}