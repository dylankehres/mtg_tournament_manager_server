package com.djk.tournament_manager.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class FirebaseInit {

    @PostConstruct
    public void init() throws FileNotFoundException {
    try {
        FileInputStream serviceAccount =
                new FileInputStream("./djk-mtg-tournament-manager-firebase-adminsdk-t4pip-bf5f773d1d.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://djk-mtg-tournament-manager.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    } catch (Exception e) {
        e.printStackTrace();
        }
    }
}
