import dao.MemberDao;
import model.Cpf;
import model.Member;
import model.Rg;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        MemberDao dao = new MemberDao();

        String textoMenuPrincipal = "Bem vindo! \nClub de ferias New Go\n\n" +
                "1 - Cadastrar Membro\n" +
                "2 - Buscar Membro\n" +
                "3 - Atualizar ou deletar Membro com numero da carteirinha\n" +
                "4 - Listar Membros\n" +
                "5 - Sair\n\n" +
                "Digite a opção desejada: ";

        while (true) {

            String opcaoSelecionada = JOptionPane.showInputDialog(textoMenuPrincipal);

            if (opcaoSelecionada.equals("5")) {
                break;
            } else if (opcaoSelecionada.equals("1")) {
                String nome = JOptionPane.showInputDialog("Digite o nome do membro: ");
                String opcaoDocumento = JOptionPane.showInputDialog("Digite o tipo de documento: \n1 - RG\n2 - CPF");
                try {
                    if (opcaoDocumento.equals("1")) {
                        String documento = JOptionPane.showInputDialog("Digite o numero do RG: ");
                        dao.save(new Member(nome, new Rg(documento)));
                        JOptionPane.showMessageDialog(null, "Membro gravado com sucesso!");
                    } else if (opcaoDocumento.equals("2")) {
                        String documento = JOptionPane.showInputDialog("Digite o numero do CPF: ");
                        dao.save(new Member(nome, new Cpf(documento)));
                        JOptionPane.showMessageDialog(null, "Membro gravado com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Opção inválida!");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Falha ao gravar membro: " + e.getMessage());
                }

            } else if (opcaoSelecionada.equals("2")) {
                String documentOrName = JOptionPane.showInputDialog("Digite o nome ou documento do membro: ");
                try {
                    Optional<Member> member = dao.find(documentOrName);
                    if (member.isPresent()) {
                        JOptionPane.showMessageDialog(null, "Membro encontrado: \n" +
                                "Nome: " + member.get().getName() + "\n" +
                                "Documento: " + member.get().getDocument().getDocumentSting() + "\n" +
                                "Data de associação: " + member.get().getDateAssociation() + "\n" +
                                "Número da carteirinha: " + member.get().getCardNumber());
                    } else {
                        JOptionPane.showMessageDialog(null, "Membro não encontrado!");
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            } else if (opcaoSelecionada.equals("3")) {
                String numCarteirinha = JOptionPane.showInputDialog("Digite o código da carteirinha: ");

                try {
                    if (numCarteirinha == null || numCarteirinha.isEmpty() || numCarteirinha.length() != 8) {
                        throw new IllegalArgumentException("Número da carteirinha inválido!");
                    }
                    Optional<Member> member = dao.find(numCarteirinha);
                    if (member.isEmpty()) {
                        throw new IllegalArgumentException("Número da carteirinha não encontrado!");
                    }
                    String opcaoEdicao = JOptionPane.showInputDialog("Digite a opção desejada: \n" +
                            "1 - Atualizar Membro\n" +
                            "2 - Deletar membro\n" +
                            "3 - Voltar ao menu principal\n\n" +
                            "Digite a opção desejada: ");

                    if (opcaoEdicao.equals("1")) {
                        String nome = JOptionPane.showInputDialog("Digite o nome do membro: ", member.get().getName()).trim();
                        String opcaoDocumento = JOptionPane.showInputDialog("Digite o tipo de documento: \n1 - RG\n2 - CPF",
                                member.get().getDocument().getDocumentSting().substring(0, 2).equals("RG") ? "1" : "2").trim();
                        try {
                            if (opcaoDocumento.equals("1")) {
                                String documento = JOptionPane.showInputDialog("Digite o numero do RG: ", (
                                        member.get().getDocument().getDocumentSting().substring(0, 2).equals("RG") ? member.get().getDocument().getNumber() : ""
                                ));
                                member.get().setName(nome);
                                member.get().setDocument(new Rg(documento));
                                dao.update(member.get(), numCarteirinha);
                                JOptionPane.showMessageDialog(null, "Membro gravado com sucesso!");
                            } else if (opcaoDocumento.equals("2")) {
                                String documento = JOptionPane.showInputDialog("Digite o numero do CPF: ", (
                                        member.get().getDocument().getDocumentSting().substring(0, 3).equals("CPF") ? member.get().getDocument().getNumber() : ""
                                ));
                                member.get().setName(nome);
                                member.get().setDocument(new Cpf(documento));
                                dao.update(member.get(), numCarteirinha);
                                JOptionPane.showMessageDialog(null, "Membro atualizado com sucesso!");
                            } else {
                                JOptionPane.showMessageDialog(null, "Opção inválida!");
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Falha ao gravar membro: " + e.getMessage());
                        }
                    } else if (opcaoEdicao.equals("2")) {
                        dao.delete(numCarteirinha);
                        JOptionPane.showMessageDialog(null, "Membro deletado com sucesso!");
                    } else if (opcaoEdicao.equals("3")) {
                    } else {
                        throw new IllegalArgumentException("Opção inválida!");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            } else if (opcaoSelecionada.equals("4")) {
                int paginaAtual = 0;
                int quantidadePorPagina = 4;
                while (true) {
                    int totalPaginas = dao.totalMembers() / quantidadePorPagina;
                    boolean primeiraPagina = paginaAtual == 0;
                    boolean ultimaPagina = paginaAtual == totalPaginas;

                    List<Member> list = dao.listAll(paginaAtual, quantidadePorPagina);
                    String texto = "";
                    for (Member member : list) {
                        texto += "Nome: " + member.getName() + "\n" +
                                "Documento: " + member.getDocument().getDocumentSting() + "\n" +
                                "Data de associação: " + member.getDateAssociation() + "\n" +
                                "Número da carteirinha: " + member.getCardNumber() + "\n\n";
                    }
                    texto += "Exibindo " + list.size() + " de " + dao.totalMembers() + " membros\n\n";
                    texto += "Página " + (paginaAtual + 1) + " de " + (totalPaginas + 1) + "\n\n";
                    texto += (ultimaPagina ? "" : "1 - Próxima página\n") +
                            (primeiraPagina ? "" : "2 - Página anterior\n") +
                            "3 - Voltar ao menu principal\n\n" +
                            "Digite a opção desejada: ";
                    String opcao = JOptionPane.showInputDialog(null, texto);

                    if (opcao.equals("1")) {
                        paginaAtual++;
                    } else if (opcao.equals("2")) {
                        paginaAtual--;
                    } else if (opcao.equals("3")) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Opção inválida!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Opção inválida!");
            }
        }
    }
}