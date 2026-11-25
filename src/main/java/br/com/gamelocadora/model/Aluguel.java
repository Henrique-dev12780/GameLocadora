
package br.com.gamelocadora.model;

import br.com.gamelocadora.exception.RegraNegocioException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Aluguel implements Resumivel {

    public static final int DIAS_ALUGUEL = 7;

    public static final BigDecimal MULTA_DIA = new BigDecimal("0.02");

    private final Long aluguelId;
    private final Cliente cliente;
    private final Exemplar exemplar;
    private final LocalDate dataRetirada;
    private final LocalDate dataPrevista;
    private final BigDecimal valorTotal;

    private LocalDate dataDevolucao;
    private BigDecimal valorMulta;

    public Aluguel(
        Cliente c,
        Exemplar e,
        LocalDate retirada
    ) {
        this(
            null,
            c,
            e,
            retirada,
            retirada.plusDays(DIAS_ALUGUEL),
            e.getMidia()
                .getJogoValorDiaria()
                .multiply(BigDecimal.valueOf(DIAS_ALUGUEL)),
            null,
            BigDecimal.ZERO
        );
    }

    public Aluguel(
        Long id,
        Cliente c,
        Exemplar e,
        LocalDate r,
        LocalDate p,
        BigDecimal total,
        LocalDate d,
        BigDecimal multa
    ) {

        if (
            c == null ||
            e == null ||
            r == null ||
            p == null ||
            !p.equals(r.plusDays(DIAS_ALUGUEL)) ||
            total == null ||
            total.signum() <= 0
        ) {
            throw new RegraNegocioException(
                "Dados inválidos: aluguel exige cliente, exemplar, " +
                "prazo de 7 dias e valor positivo."
            );
        }

        aluguelId = id;
        cliente = c;
        exemplar = e;
        dataRetirada = r;
        dataPrevista = p;
        valorTotal = total;
        dataDevolucao = d;
        valorMulta = multa == null ? BigDecimal.ZERO : multa;
    }

    public BigDecimal calcularMulta(LocalDate d) {

        if (d == null) {
            throw new RegraNegocioException(
                "Data de devolução obrigatória."
            );
        }

        LocalDate limite =
            dataPrevista.getDayOfWeek() == DayOfWeek.SUNDAY
                ? dataPrevista.plusDays(1)
                : dataPrevista;

        long atraso = Math.max(
            0,
            ChronoUnit.DAYS.between(limite, d)
        );

        return valorTotal
            .multiply(MULTA_DIA)
            .multiply(BigDecimal.valueOf(atraso))
            .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularMulta(LocalDateTime d) {

        return calcularMulta(d.toLocalDate());
    }

    public void registrarDevolucao(LocalDate d) {

        if (dataDevolucao != null) {
            throw new RegraNegocioException(
                "Aluguel já devolvido."
            );
        }

        if (d.isBefore(dataRetirada)) {
            throw new RegraNegocioException(
                "Devolução anterior à retirada."
            );
        }

        valorMulta = calcularMulta(d);
        dataDevolucao = d;
    }

    public Long getAluguelId() {

        return aluguelId;
    }

    public Cliente getCliente() {

        return cliente;
    }

    public Exemplar getExemplar() {

        return exemplar;
    }

    public LocalDate getDataRetirada() {

        return dataRetirada;
    }

    public LocalDate getDataPrevistaDevolucao() {

        return dataPrevista;
    }

    public BigDecimal getValorTotal() {

        return valorTotal;
    }

    public LocalDate getDataDevolucaoReal() {

        return dataDevolucao;
    }

    public BigDecimal getValorMulta() {

        return valorMulta;
    }

    @Override
    public String resumo() {

        return "Aluguel " + aluguelId +
               " | " + cliente.getClienteNome() +
               " | " + exemplar.getExemplarCodigo() +
               " | vence " + dataPrevista;
    }
}
