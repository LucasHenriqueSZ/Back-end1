package dao;

import model.Cpf;
import model.Document;
import model.Member;
import model.Rg;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MemberDao {

    private static final String FILE_PATH = "members.txt";

    public void save(Member member) {
        try {
            if (checkDocumentExists(member.getDocument().getDocumentSting()))
                throw new IllegalArgumentException("Documento ja cadastrado!");

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(getLineMember(member));
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar o cadastro: " + e.getMessage());
        }
    }

    public Optional<Member> find(String documentOrName) {
        try {
            if (documentOrName == null || documentOrName.isEmpty())
                throw new IllegalArgumentException("Nome ou documento não pode ser nulo ou vazio!");
            if (documentOrName.length() < 3)
                throw new IllegalArgumentException("Nome deve conter no mínimo 3 caracteres!");
            if (documentOrName.length() > 50)
                throw new IllegalArgumentException("Nome deve conter no máximo 50 caracteres!");
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(documentOrName)) {
                    reader.close();
                    return Optional.of(convertLineToMember(line));
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
        }
        return Optional.empty();
    }

    public void update(Member member, String cardNumber) {
        try {
            if (member == null)
                throw new IllegalArgumentException("Membro não pode ser nulo!");
            if (cardNumber == null || cardNumber.isEmpty())
                throw new IllegalArgumentException("Numero do cartão não pode ser nulo ou vazio!");

            File tempFile = new File(FILE_PATH + ".tmp");
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            BufferedWriter writerTempFile = new BufferedWriter(new FileWriter(tempFile));
            member.setCardNumber(cardNumber);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(cardNumber)) {
                    writerTempFile.write(getLineMember(member));
                } else {
                    writerTempFile.write(line);
                }
                writerTempFile.newLine();
            }

            reader.close();
            writerTempFile.close();

            Files.move(tempFile.toPath(), Paths.get(FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha durante o update: " + e.getMessage());
        }
    }

    public void delete(String cardNumber) {
        try {
            File tempFile = new File(FILE_PATH + ".tmp");
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            BufferedWriter writerTempFile = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(cardNumber)) {
                    writerTempFile.write(line);
                    writerTempFile.newLine();
                }
            }

            reader.close();
            writerTempFile.close();

            Files.move(tempFile.toPath(), Paths.get(FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha durante a exclusão: " + e.getMessage());
        }
    }

    public List<Member> listAll(int page, int size) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            List<String> lines = new ArrayList<>();

            for (int i = 0; i < page * size; i++) {
                reader.readLine();
            }

            for (int i = 0; i < size; i++) {
                String line = reader.readLine();
                if (line != null && !line.isEmpty()) {
                    lines.add(line);
                } else {
                    break;
                }
            }

            reader.close();

            List<Member> members = convertListLinesToListMembers(lines);

            return members;
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
        }
    }

    public int totalMembers() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            int total = 0;
            while (reader.readLine() != null) {
                total++;
            }
            reader.close();
            return total;
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao realizar a busca: " + e.getMessage());
        }
    }

    private List<Member> convertListLinesToListMembers(List<String> lines) {
        return lines.stream().map(this::convertLineToMember).collect(Collectors.toList());
    }

    private Member convertLineToMember(String line) {
        Member member = new Member();
        String[] data = line.split(";");
        member.setCardNumber(data[0].split(":")[1].trim());
        member.setName(data[1].split(":")[1].trim());
        member.setDateAssociation(LocalDate.parse(data[3].split(":")[1].trim()));
        member.setDocument(convertDocumentLine(data));

        return member;
    }

    private Document convertDocumentLine(String[] data) {
        String documentType = data[2].split(":")[1].trim();

        switch (documentType) {
            case "CPF":
                return new Cpf(data[2].split(":")[2].trim());
            case "RG":
                return new Rg(data[2].split(":")[2].trim());
            default:
                throw new IllegalArgumentException("Tipo de documento inválido!");
        }
    }

    private String getLineMember(Member member) {
        return "Card number: " + member.getCardNumber() + "; Name: " + member.getName() + "; Document: " + member.getDocument().getDocumentSting()
                + "; Date association: " + member.getDateAssociation();
    }

    private boolean checkDocumentExists(String document) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(document)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao ler o arquivo: " + e.getMessage());
        }
        return false;
    }
}
