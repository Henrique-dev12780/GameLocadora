package br.com.gamelocadora.model;

import br.com.gamelocadora.exception.RegraNegocioException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Aluguel implements Resumivel {
    public static final int DIAS_ALUGUEL = 7;
    public static final BigDecimal MULTA_DIA = new BigDecimal("0.02");

    private final Long aluguelId;
    private final Cliente cliente;
    private final Exemplar exemplar;
    private final LocalDate dataRetirada;
    private final LocalDate dataPrevistaDevolucao;
    private final BigDecimal valorTotal;
    private LocalDate dataDevolucaoReal;
    private BigDecimal valorMulta;

    // Construtor de um novo aluguel.
    public Aluguel(Cliente cliente, Exemplar exemplar, LocalDate retirada) {
        this(null, cliente, exemplar, retirada, retirada.plusDays(DIAS_ALUGUEL),
             exemplar.getMidia().getJogoValorDiaria().multiply(BigDecimal.valueOf(DIAS_ALUGUEL)),
             null, BigDecimal.ZERO);
    }

    // Construtor sobrecarregado para reconstruir um aluguel persistido.
    public Aluguel(Long id, Cliente cliente, Exemplar exemplar, LocalDate retirada,
                   LocalDate prevista, BigDecimal valorTotal,
                   LocalDate devolucaoReal, BigDecimal valorMulta) {
        if (cliente == null || exemplar == null || retirada == null || prevista == null)
            throw new RegraNegocioException("Dados obrigatórios do aluguel não foram informados.");
        if (!prevista.equals(retirada.plusDays(DIAS_ALUGUEL)))
            throw new RegraNegocioException("Todo aluguel deve durar exatamente 7 dias.");
        if (valorTotal == null || valorTotal.compareTo(BigDecimal.ZERO) <= 0)
            throw new RegraNegocioException("O valor total do aluguel deve ser positivo.");

        this.aluguelId = id;
        this.cliente = cliente;
        this.exemplar = exemplar;
        this.dataRetirada = retirada;
        this.dataPrevistaDevolucao = prevista;
        this.valorTotal = valorTotal;
        this.dataDevolucaoReal = devolucaoReal;
        this.valorMulta = valorMulta == null ? BigDecimal.ZERO : valorMulta;
    }

    public BigDecimal calcularMulta(LocalDate dataDevolucao) {
        if (dataDevolucao == null) throw new RegraNegocioException("Informe a data de devolução.");
        long diasAtraso = Math.max(0, ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataDevolucao));
        return valorTotal.multiply(MULTA_DIA)
                .multiply(BigDecimal.valueOf(diasAtraso))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Sobrecarga do método para trabalhar com data e hora.
    public BigDecimal calcularMulta(java.time.LocalDateTime dataDevolucao) {
        return calcularMulta(dataDevolucao.toLocalDate());
    }

    public void registrarDevolucao(LocalDate data) {
        if (dataDevolucaoReal != null)
            throw new RegraNegocioException("Este aluguel já foi devolvido.");
        if (data.isBefore(dataRetirada))
            throw new RegraNegocioException("A devolução não pode ser anterior à retirada.");
        this.valorMulta = calcularMulta(data);
        this.dataDevolucaoReal = data;
    }

    public Long getAluguelId() { return aluguelId; }
    public Cliente getCliente() { return cliente; }
    public Exemplar getExemplar() { return exemplar; }
    public LocalDate getDataRetirada() { return dataRetirada; }
    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public BigDecimal getValorMulta() { return valorMulta; }

    @Override
    public String resumo() {
        return "Aluguel " + aluguelId + " | Cliente: " + cliente.getClienteNome()
            + " | Exemplar: " + exemplar.getExemplarCodigo()
            + " | Devolver até: " + dataPrevistaDevolucao;
    }
}
