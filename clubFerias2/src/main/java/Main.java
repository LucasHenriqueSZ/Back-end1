import data.SocioDao;
import domain.socio.Socio;
import domain.socio.documentos.Cpf;
import presentation.MenuPrincipal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

//        Socio socio = new Socio("Joaokgsdss", new Cpf("67232908016"));
//
//        SocioDao socioDao = new SocioDao();
//
//      //  socioDao.salvar(socio);
//
//        //socioDao.find("abc");
//
//       // socioDao.carregarSocios();
//
//        //Socio  socio1 = new Socio("lucas", new Rg("256891175"));
//
//       // socioDao.salvar(socio1);
//
////        Socio sociobusca = socioDao.buscarPorDocumentoOuNome("256891175").get();
////
////        socioDao.deletar("PT5U1HWB");
//
//       //System.out.println(sociobusca.getNome());
//        socioDao.listaPaginada(5);
//
//        socioDao.atualizar(new Socio("socio update", new Rg("274639476")), "JNPXHVNF");
//        SocioDao socioDao = new SocioDao();
//        socioDao.salvar(new Socio("socio update", new Cpf("67232908016")));

        try {
            MenuPrincipal.menuPricipal(new Scanner(System.in));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}