package com.example.tocare.DLL;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public final class DataBase {
    private static DataBase single_instance = null;
    private FirebaseFirestore FireStore;

    public void setReference(DocumentReference reference) {
        this.reference = reference;
    }

    private DocumentReference reference;

    public FirebaseFirestore getFireStore() {
        return FireStore;
    }

    public DocumentReference getReference() {
        return reference;
    }

    private DataBase() {

        FireStore = FirebaseFirestore.getInstance();
    }

    public static DataBase getInstance() {
        if (single_instance == null)
            single_instance = new DataBase();
        return single_instance;
    }

}
