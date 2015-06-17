package com.eleks.securedatastorage.sdk.security;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.spec.IvParameterSpec;

/**
 * This class is used to create an Encryption instance, you should provide ALL data or start
 * with the Default Builder provided by the getDefaultBuilder method
 */
public class Builder {

    private byte[] mIv;
    private int mKeyLength;
    private int mBase64Mode;
    private int mIterationCount;
    private String mSalt;
    private String mKey;
    private String mAlgorithm;
    private String mCharsetName;
    private String mSecretKeyType;
    private String mDigestAlgorithm;
    private String mSecureRandomAlgorithm;
    private SecureRandom mSecureRandom;
    private IvParameterSpec mIvParameterSpec;

    /**
     * @return an default builder with the follow defaults:
     * the default char set is UTF-8
     * the default base mode is Base64
     * the Secret Key Type is the PBKDF2WithHmacSHA1
     * the default salt is "some_salt" but can be anything
     * the default length of key is 128
     * the default iteration count is 65536
     * the default algorithm is AES in CBC mode and PKCS 5 Padding
     * the default secure random algorithm is SHA1PRNG
     * the default message digest algorithm SHA1
     */
    public static Builder getDefaultBuilder(String key, String salt, byte[] iv) {
        return new Builder()
                .setIv(iv)
                .setKey(key)
                .setSalt(salt)
                .setKeyLength(128)
                .setCharsetName("UTF8")
                .setIterationCount(65536)
                .setDigestAlgorithm("SHA1")
                .setBase64Mode(Base64.DEFAULT)
                .setAlgorithm("AES/CBC/PKCS5Padding")
                .setSecureRandomAlgorithm("SHA1PRNG")
                .setSecretKeyType("PBKDF2WithHmacSHA1");
    }

    /**
     * Build the Encryption with the provided information
     *
     * @return a new Encryption instance with provided information
     * @throws NoSuchAlgorithmException if the specified SecureRandomAlgorithm is not available
     * @throws NullPointerException     if the SecureRandomAlgorithm is {@code null} or if the
     *                                  IV byte array is null
     */
    public Encryption build() throws NoSuchAlgorithmException {
        setSecureRandom(SecureRandom.getInstance(getSecureRandomAlgorithm()));
        setIvParameterSpec(new IvParameterSpec(getIv()));
        return new Encryption(this);
    }

    //region getters and setters

    /**
     * @return the charset name
     */
    String getCharsetName() {
        return mCharsetName;
    }

    /**
     * @param charsetName the new charset name
     * @return this instance to follow the Builder patter
     */
    public Builder setCharsetName(String charsetName) {
        mCharsetName = charsetName;
        return this;
    }

    /**
     * @return the algorithm
     */
    String getAlgorithm() {
        return mAlgorithm;
    }

    /**
     * @param algorithm the algorithm to be used
     * @return this instance to follow the Builder patter
     */
    public Builder setAlgorithm(String algorithm) {
        mAlgorithm = algorithm;
        return this;
    }

    /**
     * @return the Base 64 mode
     */
    int getBase64Mode() {
        return mBase64Mode;
    }

    /**
     * @param base64Mode set the base 64 mode
     * @return this instance to follow the Builder patter
     */
    public Builder setBase64Mode(int base64Mode) {
        mBase64Mode = base64Mode;
        return this;
    }

    /**
     * @return the type of aes key that will be created, on KITKAT+ the API has changed, if you
     * are getting problems please @see <a href="http://android-developers.blogspot.com.br/2013/12/changes-to-secretkeyfactory-api-in.html">http://android-developers.blogspot.com.br/2013/12/changes-to-secretkeyfactory-api-in.html</a>
     */
    String getSecretKeyType() {
        return mSecretKeyType;
    }

    /**
     * @param secretKeyType the type of AES key that will be created, on KITKAT+ the API has
     *                      changed, if you are getting problems please @see <a href="http://android-developers.blogspot.com.br/2013/12/changes-to-secretkeyfactory-api-in.html">http://android-developers.blogspot.com.br/2013/12/changes-to-secretkeyfactory-api-in.html</a>
     * @return this instance to follow the Builder patter
     */
    public Builder setSecretKeyType(String secretKeyType) {
        mSecretKeyType = secretKeyType;
        return this;
    }

    /**
     * @return the value used for salting
     */
    String getSalt() {
        return mSalt;
    }

    /**
     * @param salt the value used for salting
     * @return this instance to follow the Builder patter
     */
    public Builder setSalt(String salt) {
        mSalt = salt;
        return this;
    }

    /**
     * @return the key
     */
    String getKey() {
        return mKey;
    }

    /**
     * @param key the key.
     * @return this instance to follow the Builder patter
     */
    public Builder setKey(String key) {
        mKey = key;
        return this;
    }

    /**
     * @return the length of key
     */
    int getKeyLength() {
        return mKeyLength;
    }

    /**
     * @param keyLength the length of key
     * @return this instance to follow the Builder patter
     */
    public Builder setKeyLength(int keyLength) {
        mKeyLength = keyLength;
        return this;
    }

    /**
     * @return the number of times the password is hashed
     */
    int getIterationCount() {
        return mIterationCount;
    }

    /**
     * @param iterationCount the number of times the password is hashed
     * @return this instance to follow the Builder patter
     */
    public Builder setIterationCount(int iterationCount) {
        mIterationCount = iterationCount;
        return this;
    }

    /**
     * @return the algorithm used to generate the secure random
     */
    private String getSecureRandomAlgorithm() {
        return mSecureRandomAlgorithm;
    }

    /**
     * @param secureRandomAlgorithm the algorithm to generate the secure random
     * @return this instance to follow the Builder patter
     */
    public Builder setSecureRandomAlgorithm(String secureRandomAlgorithm) {
        mSecureRandomAlgorithm = secureRandomAlgorithm;
        return this;
    }

    /**
     * @return the IvParameterSpec bytes array
     */
    private byte[] getIv() {
        return mIv;
    }

    /**
     * @param iv the byte array to create a new IvParameterSpec
     * @return this instance to follow the Builder patter
     */
    public Builder setIv(byte[] iv) {
        mIv = iv;
        return this;
    }

    /**
     * @return the SecureRandom
     */
    SecureRandom getSecureRandom() {
        return mSecureRandom;
    }

    /**
     * @param secureRandom the Secure Random
     * @return this instance to follow the Builder patter
     */
    public Builder setSecureRandom(SecureRandom secureRandom) {
        mSecureRandom = secureRandom;
        return this;
    }

    /**
     * @return the IvParameterSpec
     */
    IvParameterSpec getIvParameterSpec() {
        return mIvParameterSpec;
    }

    /**
     * @param ivParameterSpec the IvParameterSpec
     * @return this instance to follow the Builder patter
     */
    public Builder setIvParameterSpec(IvParameterSpec ivParameterSpec) {
        mIvParameterSpec = ivParameterSpec;
        return this;
    }

    /**
     * @return the message digest algorithm
     */
    String getDigestAlgorithm() {
        return mDigestAlgorithm;
    }

    /**
     * @param digestAlgorithm the algorithm to be used to get message digest instance
     * @return this instance to follow the Builder patter
     */
    public Builder setDigestAlgorithm(String digestAlgorithm) {
        mDigestAlgorithm = digestAlgorithm;
        return this;
    }

    //endregion

}