import application.MenuPrincipal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            MenuPrincipal.getInstance().menuPricipal(new Scanner(System.in));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}