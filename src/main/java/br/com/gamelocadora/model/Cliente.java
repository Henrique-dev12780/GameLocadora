
package br.com.gamelocadora.model;

import br.com.gamelocadora.exception.RegraNegocioException;

public class Cliente implements Validavel, Resumivel {

    private final Long clienteId;

    private String clienteNome;
    private String clienteEmail;
    private String clienteTelefone;
    private boolean clienteAtivo;

    public Cliente(
        String nome,
        String email
    ) {
        this(
            null,
            nome,
            email,
            null,
            true
        );
    }

    public Cliente(
        Long id,
        String nome,
        String email,
        String telefone,
        boolean ativo
    ) {

        clienteId = id;
        clienteNome = nome;
        clienteEmail = email;
        clienteTelefone = telefone;
        clienteAtivo = ativo;

        validar();
    }

    @Override
    public void validar() {

        validarNome(clienteNome);
        validarEmail(clienteEmail);
    }

    private void validarNome(String v) {

        if (v == null || v.trim().length() < 3) {
            throw new RegraNegocioException(
                "Nome deve ter ao menos 3 caracteres."
            );
        }
    }

    private void validarEmail(String v) {

        if (
            v == null ||
            !v.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        ) {
            throw new RegraNegocioException(
                "E-mail inválido."
            );
        }
    }

    public Long getClienteId() {

        return clienteId;
    }

    public String getClienteNome() {

        return clienteNome;
    }

    public String getClienteEmail() {

        return clienteEmail;
    }

    public String getClienteTelefone() {

        return clienteTelefone;
    }

    public boolean isClienteAtivo() {

        return clienteAtivo;
    }

    public void setClienteNome(String v) {

        validarNome(v);
        clienteNome = v;
    }

    public void setClienteEmail(String v) {

        validarEmail(v);
        clienteEmail = v;
    }

    public void setClienteTelefone(String v) {

        clienteTelefone = v;
    }

    public void setClienteAtivo(boolean v) {

        clienteAtivo = v;
    }

    @Override
    public String resumo() {

        return clienteId +
               " | " + clienteNome +
               " | " + clienteEmail;
    }
}
