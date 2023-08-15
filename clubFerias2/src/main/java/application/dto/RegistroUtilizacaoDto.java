package application.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class RegistroUtilizacaoDto {

    private String codigoRegistro;

    private LocalDate dataUtilizacao;

    private LocalTime horaEntrada;

    private LocalTime horaSaida;

    private String nomeSocio;

    private String nomeEspacoClub;

    public RegistroUtilizacaoDto(LocalDate dataUtilizacao, LocalTime horaEntrada, LocalTime horaSaida, String socio, String codigoEspaco, String codigoRegistro) {
        setDataUtilizacao(dataUtilizacao);
        setHoraEntrada(horaEntrada);
        setHoraSaida(horaSaida);
        setNomeSocio(socio);
        setNomeEspacoClub(codigoEspaco);
        setCodigoRegistro(codigoRegistro);
    }

    public RegistroUtilizacaoDto() {
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

    public String getNomeSocio() {
        return nomeSocio;
    }

    public void setNomeSocio(String nomeSocio) {
        this.nomeSocio = nomeSocio;
    }

    public String getNomeEspacoClub() {
        return nomeEspacoClub;
    }

    public void setNomeEspacoClub(String nomeEspacoClub) {
        this.nomeEspacoClub = nomeEspacoClub;
    }

    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(String codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }
}
