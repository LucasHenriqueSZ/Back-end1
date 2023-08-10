package domain;

import util.GeradorCodigo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class RegistroUtilizacao {

    private String codigoRegistro;

    private LocalDate dataUtilizacao;

    private LocalTime horaEntrada;

    private LocalTime horaSaida;

    private String carteirinhaSocio;

    private String codigoEspaco;

    public RegistroUtilizacao(LocalDate dataUtilizacao, LocalTime horaEntrada, LocalTime horaSaida, String socio, String codigoEspaco) {
        setDataUtilizacao(dataUtilizacao);
        setHoraEntrada(horaEntrada);
        setHoraSaida(horaSaida);
        setCarteirinhaSocio(socio);
        setCodigoEspaco(codigoEspaco);
        setCodigoRegistro(GeradorCodigo.getCodigo());
    }

    public RegistroUtilizacao() {
    }

    public LocalDate getDataUtilizacao() {
        return dataUtilizacao;
    }

   public Duration getTempoDeUso() {
       return Duration.between(horaEntrada, horaSaida);
   }

    public void setDataUtilizacao(LocalDate dataUtilizacao) {
        if (dataUtilizacao == null)
            throw new IllegalArgumentException("Data de utilização não pode ser nula!");
        this.dataUtilizacao = dataUtilizacao;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        if (horaEntrada == null)
            throw new IllegalArgumentException("Hora de entrada não pode ser nula!");
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(LocalTime horaSaida) {
        if (horaSaida == null)
            throw new IllegalArgumentException("Hora de saída não pode ser nula!");
        this.horaSaida = horaSaida;
    }

    public String getCarteirinhaSocio() {
        return carteirinhaSocio;
    }

    public void setCarteirinhaSocio(String carteirinhaSocio) {
        if (carteirinhaSocio == null || carteirinhaSocio.isEmpty())
            throw new IllegalArgumentException("Carteirinha do sócio não pode ser nula!");
        if (carteirinhaSocio.length() != 8)
            throw new IllegalArgumentException("Carteirinha do sócio deve conter 8 caracteres!");
        this.carteirinhaSocio = carteirinhaSocio;
    }

    public String getCodigoEspaco() {
        return codigoEspaco;
    }

    public void setCodigoEspaco(String codigoEspaco) {
        if (codigoEspaco == null || codigoEspaco.isEmpty())
            throw new IllegalArgumentException("Código do espaço não pode ser nulo!");
        if (codigoEspaco.length() != 5)
            throw new IllegalArgumentException("Código do espaço deve conter 5 caracteres!");
        this.codigoEspaco = codigoEspaco;
    }

    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(String codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }
}
