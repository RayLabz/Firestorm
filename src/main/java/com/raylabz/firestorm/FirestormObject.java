package com.raylabz.firestorm;

import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class FirestormObject {

    private String id;
    @Exclude private transient ArrayList<ListenerRegistration> listeners = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public ArrayList<ListenerRegistration> getListeners() {
        return listeners;
    }

    @Exclude
    public void addListener(ListenerRegistration listenerRegistration) {
        listeners.add(listenerRegistration);
    }

    @Exclude
    public void removeListener(ListenerRegistration listenerRegistration) {
        listeners.remove(listenerRegistration);
    }

}
