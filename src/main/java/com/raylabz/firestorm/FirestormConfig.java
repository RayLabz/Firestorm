package com.raylabz.firestorm;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Stores Firestore configuration - enables quick parsing of service account files.
 */
public class FirestormConfig {

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
     * Instantiates a new object of FirestormConfig, based on the service account details of Firebase.
     * @param type The type of account.
     * @param project_id The project ID of the service account.
     * @param private_key_id The private key ID of the service account.
     * @param private_key The private key of the service account.
     * @param client_email The client email of the service account.
     * @param client_id The client ID of the service account.
     * @param auth_uri The auth URI of the service account.
     * @param token_uri The token URI of the service account.
     * @param auth_provider_x509_cert_url The auth provider certification URL of the service account.
     * @param client_x509_cert_url The client certification URL of the service account.
     */
    public FirestormConfig(String type, String project_id, String private_key_id, String private_key, String client_email, String client_id, String auth_uri, String token_uri, String auth_provider_x509_cert_url, String client_x509_cert_url) {
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
     * Converts a JSON-formatted string into a FirestormConfig object.
     * @param string The string to parse.
     * @return Returns a FirestormConfig object.
     * @throws JsonSyntaxException Throws an exception if the object parsed is not a FirestormConfig object.
     */
    public static FirestormConfig fromString(final String string) throws JsonSyntaxException {
        return new Gson().fromJson(string, FirestormConfig.class);
    }

    /**
     * Converts FirestormConfig to an InputStream.
     * @return Returns InputStream object.
     */
    public InputStream toInputStream() {
        return new ByteArrayInputStream(toJson().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Returns a JSON-formatted string representing the FirestormConfig object.
     * @return Returns a JSON-formatted string.
     */
    public String toJson() {
        return new Gson().toJson(this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getPrivate_key_id() {
        return private_key_id;
    }

    public void setPrivate_key_id(String private_key_id) {
        this.private_key_id = private_key_id;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getAuth_uri() {
        return auth_uri;
    }

    public void setAuth_uri(String auth_uri) {
        this.auth_uri = auth_uri;
    }

    public String getToken_uri() {
        return token_uri;
    }

    public void setToken_uri(String token_uri) {
        this.token_uri = token_uri;
    }

    public String getAuth_provider_x509_cert_url() {
        return auth_provider_x509_cert_url;
    }

    public void setAuth_provider_x509_cert_url(String auth_provider_x509_cert_url) {
        this.auth_provider_x509_cert_url = auth_provider_x509_cert_url;
    }

    public String getClient_x509_cert_url() {
        return client_x509_cert_url;
    }

    public void setClient_x509_cert_url(String client_x509_cert_url) {
        this.client_x509_cert_url = client_x509_cert_url;
    }

}
