package com.example.proyecto.interfaces;

import com.example.proyecto.model.Usuario;
import com.facebook.AccessToken;

public interface IUsuario {
    interface view {
        void showResultSuccess(String result, Usuario usuario);
        void showResultError(String result);
    }

    interface presenter {
        void registerUser(Usuario usuario);
        void showRegisterSuccess(String result, Usuario usuario);
        void showRegisterError(String result);
        void loginUser(String email, String password);
        void handleFacebookAccessToken(AccessToken token);
        void firebaseAuthWithGoogle(String idToken);
        void getUserAuth();
        void signOutUser();
    }

    interface model {
        void registerUser(Usuario usuario);
        void loginUser(String email, String password);
        void handleFacebookAccessToken(AccessToken token);
        void firebaseAuthWithGoogle(String idToken);
        void getUserAuth();
        void signOutUser();
    }
}
