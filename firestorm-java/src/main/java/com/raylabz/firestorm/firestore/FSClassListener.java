package com.raylabz.firestorm.firestore;

import com.google.cloud.firestore.*;
import com.raylabz.firestorm.api.ClassListener;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements logic for Firestore update events.
 * @author Nicos Kasenides
 * @version 2.0.0
 */
public class FSClassListener<T> extends ClassListener<T> {

    public FSClassListener(Class<T> objectClass, RealtimeUpdateCallback<List<FSObjectChange<T>>> callback) {
        super(objectClass, callback);
    }

    /**
     * Executes when an update to an object is made.
     * @param querySnapshot The query snapshot retrieved upon update.
     * @param e An exception thrown by Firestore if the data retrieval was not successful.
     */
    @Override
    public final void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirestoreException e) {
        if (e != null) {
            callback.onError(e);
            return;
        }

        if (querySnapshot != null) {
            List<DocumentChange> documentChanges = querySnapshot.getDocumentChanges();
            ArrayList<FSObjectChange<T>> objectChanges = new ArrayList<>();
            for (DocumentChange documentChange : documentChanges) {
                QueryDocumentSnapshot document = documentChange.getDocument();
                FSObjectChange<T> objectChange = new FSObjectChange<T>(document.toObject(objectClass), document, documentChange.getOldIndex(), documentChange.getNewIndex(), FSObjectChange.Type.fromDocumentChangeType(documentChange.getType()));
                objectChanges.add(objectChange);
            }
            callback.onUpdate(objectChanges);

        }
        else {
            callback.onError(new RuntimeException("Failed to retrieve update to collection."));
        }
    }

}
