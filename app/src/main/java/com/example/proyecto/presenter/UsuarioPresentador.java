package com.example.proyecto.presenter;
import com.example.proyecto.interfaces.IUsuario;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.model.UsuarioModel;
import com.facebook.AccessToken;

public class UsuarioPresentador implements IUsuario.presenter {
    private IUsuario.view view;
    private IUsuario.model model;
    public UsuarioPresentador (IUsuario.view view) {
        this.view = view;
        model = new UsuarioModel(this, this.view);
    }
    @Override
    public void registerUser(Usuario usuario) {
        if (usuario != null) {
            model.registerUser(usuario);
        }
    }
    @Override
    public void showRegisterSuccess(String result, Usuario usuario) {
        view.showResultSuccess(result, usuario);
    }
    @Override
    public void showRegisterError(String result) {
        view.showResultError(result);
    }
    @Override
    public void loginUser(String email, String password) {
        model.loginUser(email, password);
    }

    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        model.handleFacebookAccessToken(token);
    }

    @Override
    public void firebaseAuthWithGoogle(String idToken) {
        model.firebaseAuthWithGoogle(idToken);
    }

    @Override
    public void getUserAuth() {
        model.getUserAuth();
    }
    @Override
    public void signOutUser() {
        model.signOutUser();
    }
}
