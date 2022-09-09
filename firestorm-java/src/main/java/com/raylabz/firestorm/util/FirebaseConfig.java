package com.raylabz.firestorm.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Stores Firebase configuration data.
 *  @author Nicos Kasenides
 *  @version 1.1.0
 */
public class FirebaseConfig {
    private String type;
    private String project_id;
    private String private_key_id;
    private String private_key;
    private String client_email;
    private String client_id;
    private String auth_uri;
    private String token_uri;
    private String auth_provider_x509_cert_url;
    private String client_x509_cert_url;

    /**
     * Constructs a new Firebase configuration.
     * @param type The type
     * @param project_id The project id.
     * @param private_key_id The private key ID of the config.
     * @param private_key The private key of the config.
     * @param client_email The client email.
     * @param client_id The client ID.
     * @param auth_uri The auth URI.
     * @param token_uri The token URI.
     * @param auth_provider_x509_cert_url The auth provider certificate URL.
     * @param client_x509_cert_url The client certification URL.
     */
    public FirebaseConfig(String type, String project_id, String private_key_id, String private_key, String client_email, String client_id, String auth_uri, String token_uri, String auth_provider_x509_cert_url, String client_x509_cert_url) {
        this.type = type;
        this.project_id = project_id;
        this.private_key_id = private_key_id;
        this.private_key = private_key;
        this.client_email = client_email;
        this.client_id = client_id;
        this.auth_uri = auth_uri;
        this.token_uri = token_uri;
        this.auth_provider_x509_cert_url = auth_provider_x509_cert_url;
        this.client_x509_cert_url = client_x509_cert_url;
    }

    /**
     * Decodes a JSON-formatted String into a FirebaseConfig object.
     * @param string The string to decode.
     * @return Returns a FirebaseConfig object.
     * @throws JsonSyntaxException thrown when the string cannot be decoded into a FirebaseConfig object.
     */
    public static FirebaseConfig fromString(String string) throws JsonSyntaxException {
        return (new Gson()).fromJson(string, FirebaseConfig.class);
    }

    /**
     * Converts the FirebaseConfig into a JSON-formatted string and returns an input stream containing its contents.
     * @return Returns an input stream.
     */
    public InputStream toInputStream() {
        return new ByteArrayInputStream(this.toJson().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts a FirebaseConfig object into its JSON format.
     * @return Returns a JSON-formatted string.
     */
    public String toJson() {
        return (new Gson()).toJson(this);
    }

    /**
     * Returns the type.
     * @return Returns a string.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type.
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the project ID.
     * @return Returns a string.
     */
    public String getProject_id() {
        return this.project_id;
    }

    /**
     * Sets the project ID.
     * @param project_id The project ID to set.
     */
    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    /**
     * Retrieves the private key ID.
     * @return Returns a String.
     */
    public String getPrivate_key_id() {
        return this.private_key_id;
    }

    /**
     * Sets the private key ID.
     * @param private_key_id The private key ID to set.
     */
    public void setPrivate_key_id(String private_key_id) {
        this.private_key_id = private_key_id;
    }

    /**
     * Retrieves the private key.
     * @return Returns a String.
     */
    public String getPrivate_key() {
        return this.private_key;
    }

    /**
     * Sets the private key.
     * @param private_key The private key to set.
     */
    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    /**
     * Retrieves the client email.
     * @return Returns a string.
     */
    public String getClient_email() {
        return this.client_email;
    }

    /**
     * Sets the client email.
     * @param client_email The client email to set.
     */
    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    /**
     * Retrieves the client ID.
     * @return Returns a string.
     */
    public String getClient_id() {
        return this.client_id;
    }

    /**
     * Sets the client ID.
     * @param client_id The client ID to set.
     */
    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    /**
     * Retrieves the auth URI.
     * @return Returns a string.
     */
    public String getAuth_uri() {
        return this.auth_uri;
    }

    /**
     * Sets the auth URI.
     * @param auth_uri The auth URI to set.
     */
    public void setAuth_uri(String auth_uri) {
        this.auth_uri = auth_uri;
    }

    /**
     * Retrieves the token URI.
     * @return Returns a string.
     */
    public String getToken_uri() {
        return this.token_uri;
    }

    /**
     * Sets the token URI.
     * @param token_uri The token URI to set.
     */
    public void setToken_uri(String token_uri) {
        this.token_uri = token_uri;
    }

    /**
     * Retrieves the auth provider certification URL.
     * @return Returns a string.
     */
    public String getAuth_provider_x509_cert_url() {
        return this.auth_provider_x509_cert_url;
    }

    /**
     * Sets the auth provider certification URL.
     * @param auth_provider_x509_cert_url The certification URL to set.
     */
    public void setAuth_provider_x509_cert_url(String auth_provider_x509_cert_url) {
        this.auth_provider_x509_cert_url = auth_provider_x509_cert_url;
    }

    /**
     * Retrieves the client certification URL
     * @return Returns a string.
     */
    public String getClient_x509_cert_url() {
        return this.client_x509_cert_url;
    }

    /**
     * Sets the client certification URL.
     * @param client_x509_cert_url The client certification URL to set.
     */
    public void setClient_x509_cert_url(String client_x509_cert_url) {
        this.client_x509_cert_url = client_x509_cert_url;
    }

}