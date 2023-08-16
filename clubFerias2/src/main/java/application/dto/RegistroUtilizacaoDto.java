package application.dto;

import application.dto.socioDto.SocioDto;

import java.time.LocalDate;
import java.time.LocalTime;

public class RegistroUtilizacaoDto {

    private String codigoRegistro;

    private LocalDate dataUtilizacao;

    private LocalTime horaEntrada;

    private LocalTime horaSaida;

    private SocioDto socio;

    private EspacoClubDto espacoClub;

    public RegistroUtilizacaoDto(LocalDate dataUtilizacao, LocalTime horaEntrada, LocalTime horaSaida, SocioDto socio, EspacoClubDto espaco, String codigoRegistro) {
        setDataUtilizacao(dataUtilizacao);
        setHoraEntrada(horaEntrada);
        setHoraSaida(horaSaida);
        setSocio(socio);
        setEspacoClub(espaco);
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

    public SocioDto getSocio() {
        return socio;
    }

    public void setSocio(SocioDto socio) {
        this.socio = socio;
    }

    public EspacoClubDto getEspacoClub() {
        return espacoClub;
    }

    public void setEspacoClub(EspacoClubDto espacoClub) {
        this.espacoClub = espacoClub;
    }

    public String getCodigoRegistro() {
        return codigoRegistro;
    }

    public void setCodigoRegistro(String codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }
}
