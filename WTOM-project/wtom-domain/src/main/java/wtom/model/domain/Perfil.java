/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wtom.model.domain;

/**
 *
 * @author Luis Dias
 ***/

import java.util.ArrayList;
import java.util.List;

public class Perfil {
    private String nome;
    private List<Permissao> permissoes;

    public Perfil(String nome) {
        this.nome = nome;
        this.permissoes = new ArrayList<>();
    }

    public void adicionarPermissao(Permissao permissao) {
        permissoes.add(permissao);
    }

    public boolean temPermissao(String nomePermissao) {
        for (Permissao p : permissoes) {
            if (p.getNome().equalsIgnoreCase(nomePermissao)) {
                return true;
            }
        }
        return false;
    }


    public String getNome() {
        return nome;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }

    @Override
    public String toString() {
        return nome + " - Permissões: " + permissoes;
    }
}
