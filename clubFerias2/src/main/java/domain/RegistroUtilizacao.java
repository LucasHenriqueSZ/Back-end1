package domain;

import domain.socio.Socio;

import java.time.LocalDate;
import java.time.LocalTime;

public class RegistroUtilizacao {

    private LocalDate dataUtilizacao;

    private LocalTime horaEntrada;

    private LocalTime horaSaida;

    private Socio carteirinhaSocio;

    private Espaco espaco;

    public RegistroUtilizacao(LocalDate dataUtilizacao, LocalTime horaEntrada, LocalTime horaSaida, Socio socio, Espaco espaco) {
        setDataUtilizacao(dataUtilizacao);
        setHoraEntrada(horaEntrada);
        setHoraSaida(horaSaida);
        setCarteirinhaSocio(socio);
        setEspaco(espaco);
    }

    public RegistroUtilizacao() {
    }

    public LocalDate getDataUtilizacao() {
        return dataUtilizacao;
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

    public Socio getCarteirinhaSocio() {
        return carteirinhaSocio;
    }

    public void setCarteirinhaSocio(Socio carteirinhaSocio) {
        this.carteirinhaSocio = carteirinhaSocio;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }
}
