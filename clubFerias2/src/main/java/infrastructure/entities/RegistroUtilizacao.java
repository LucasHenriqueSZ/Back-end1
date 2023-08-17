package infrastructure.entities;

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

    public RegistroUtilizacao(LocalDate dataUtilizacao, LocalTime horaEntrada, LocalTime horaSaida, String socio, String codigoEspaco, String codigo) {
        setDataUtilizacao(dataUtilizacao);
        setHoraEntrada(horaEntrada);
        setHoraSaida(horaSaida);
        setCarteirinhaSocio(socio);
        setCodigoEspaco(codigoEspaco);
        setCodigoRegistro(codigo);
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
        this.dataUtilizacao = dataUtilizacao;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(LocalTime horaSaida) {
        this.horaSaida = horaSaida;
    }

    public String getCarteirinhaSocio() {
        return carteirinhaSocio;
    }

    public void setCarteirinhaSocio(String carteirinhaSocio) {
        this.carteirinhaSocio = carteirinhaSocio;
    }

    public String getCodigoEspaco() {
        return codigoEspaco;
    }

    public void setCodigoEspaco(String codigoEspaco) {
        this.codigoEspaco = codigoEspaco;
    }

    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(String codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }
}
