package com.raylabz.firestorm;

import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class FirestormObject {

    @Exclude private String documentID;
    @Exclude private ArrayList<ListenerRegistration> listeners = new ArrayList<>();

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    @Exclude
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
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
