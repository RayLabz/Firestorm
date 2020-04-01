package com.raylabz.firestorm;

import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import com.google.gson.Gson;

import java.util.ArrayList;

@IgnoreExtraProperties
public class FirestormObject {

    /**
     * The document ID of the object after it is created in Firestore.
     * This field receives a value once the object has been written into Firestore.
     */
    private String id;

    /**
     * A list of listeners listening for changes to this object on Firestore.
     */
    @Exclude private transient ArrayList<ListenerRegistration> listeners = new ArrayList<>();

    /**
     * Retrieves the document ID <b>id</b> of this object.
     * @return Returns the ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the document ID for this object.
     * @param id The ID.
     */
    void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the object's listeners.
     * @return Returns an ArrayList of ListenerRegistration.
     */
    @Exclude
    public ArrayList<ListenerRegistration> getListeners() {
        return listeners;
    }

    /**
     * Adds a new listener to this object.
     * @param listenerRegistration The listener to add.
     */
    @Exclude
    public void addListener(ListenerRegistration listenerRegistration) {
        listeners.add(listenerRegistration);
    }

    /**
     * Removes a given listener from this object.
     * @param listenerRegistration The listener to remove.
     */
    @Exclude
    public void removeListener(ListenerRegistration listenerRegistration) {
        listeners.remove(listenerRegistration);
        listenerRegistration.remove();
    }

    /**
     * Converts the object to a JSON-formatted string.
     * @return Returns a JSON-formatted string.
     */
    public String toJson() {
        return new Gson().toJson(this);
    }

}
