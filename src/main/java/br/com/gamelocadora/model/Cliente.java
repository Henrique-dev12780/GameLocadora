package br.com.gamelocadora.model;

import br.com.gamelocadora.exception.RegraNegocioException;

public class Cliente implements Validavel, Resumivel {
    private final Long clienteId;
    private String clienteNome;
    private String clienteEmail;
    private String clienteTelefone;
    private boolean clienteAtivo;

    // Construtor para novo cadastro.
    public Cliente(String nome, String email) {
        this(null, nome, email, null, true);
    }

    // Construtor sobrecarregado para carregar registro existente.
    public Cliente(Long id, String nome, String email, String telefone, boolean ativo) {
        this.clienteId = id; // Sem setter: a identidade é definida pelo banco.
        this.clienteNome = nome;
        this.clienteEmail = email;
        this.clienteTelefone = telefone;
        this.clienteAtivo = ativo;
        validar();
    }

    @Override
    public void validar() {
        validarNome(clienteNome);
        validarEmail(clienteEmail);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new RegraNegocioException("O nome deve possuir pelo menos 3 caracteres.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new RegraNegocioException("Informe um e-mail válido.");
        }
    }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public String getClienteEmail() { return clienteEmail; }
    public String getClienteTelefone() { return clienteTelefone; }
    public boolean isClienteAtivo() { return clienteAtivo; }

    public void setClienteNome(String nome) {
        validarNome(nome);
        this.clienteNome = nome;
    }

    public void setClienteEmail(String email) {
        validarEmail(email);
        this.clienteEmail = email;
    }

    public void setClienteTelefone(String telefone) { this.clienteTelefone = telefone; }
    public void setClienteAtivo(boolean ativo) { this.clienteAtivo = ativo; }

    @Override
    public String resumo() {
        return clienteId + " | " + clienteNome + " | " + clienteEmail;
    }
}
