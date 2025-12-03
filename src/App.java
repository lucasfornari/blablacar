import application.*;
import application.input.*;
import application.output.*;
import domain.repositories.*;
import infrastructure.database.*;
import infrastructure.persistence.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static UsuarioOutput usuarioLogado = null;

    private static UsuarioUseCase usuarioUC;
    private static ViagemUseCase viagemUC;

    public static void main(String[] args) {
        inicializar();
        
        System.out.println("\n========================================");
        System.out.println("         BLABLACAR - CARONAS");
        System.out.println("========================================");

        while (true) {
            if (usuarioLogado == null) {
                menuInicial();
            } else {
                menuPrincipal();
            }
        }
    }

    private static void inicializar() {
        System.out.println("Iniciando...");

        if (ConexaoBanco.testarConexao()) {
            System.out.println("Banco conectado!");
            Migracao.executar();
        } else {
            System.out.println("ERRO: Sem conexao com banco!");
        }

        IUsuarioRepository usuarioRepo = new UsuarioRepositoryPostgres();
        IMotoristaRepository motoristaRepo = new MotoristaRepositoryPostgres();
        IPassageiroRepository passageiroRepo = new PassageiroRepositoryPostgres();
        IVeiculoRepository veiculoRepo = new VeiculoRepositoryPostgres();
        IViagemRepository viagemRepo = new ViagemRepositoryPostgres();
        IPassageiroViagemRepository pvRepo = new PassageiroViagemRepositoryPostgres();

        usuarioUC = new UsuarioUseCase(usuarioRepo, motoristaRepo, passageiroRepo, veiculoRepo);
        viagemUC = new ViagemUseCase(viagemRepo, veiculoRepo, pvRepo);
    }

    private static void menuInicial() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Login");
        System.out.println("2. Criar Conta");
        System.out.println("3. Buscar Caronas");
        System.out.println("0. Sair");
        System.out.print("Opcao: ");

        switch (lerInt()) {
            case 1 -> login();
            case 2 -> criarConta();
            case 3 -> buscarCaronas();
            case 0 -> sair();
            default -> System.out.println("Opcao invalida!");
        }
    }

    private static void menuPrincipal() {
        System.out.println("\n--- MENU [" + usuarioLogado.getNome() + "] ---");
        System.out.println("1. Ofertar Carona");
        System.out.println("2. Buscar Caronas");
        System.out.println("3. Cadastrar Veiculo");
        System.out.println("4. Meus Veiculos");
        System.out.println("5. Minhas Caronas");
        System.out.println("6. Meu Perfil");
        System.out.println("7. Logout");
        System.out.println("0. Sair");
        System.out.print("Opcao: ");

        switch (lerInt()) {
            case 1 -> ofertarCarona();
            case 2 -> buscarCaronas();
            case 3 -> cadastrarVeiculo();
            case 4 -> meusVeiculos();
            case 5 -> minhasCaronas();
            case 6 -> meuPerfil();
            case 7 -> { usuarioLogado = null; System.out.println("Logout!"); }
            case 0 -> sair();
            default -> System.out.println("Opcao invalida!");
        }
    }

    private static void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        LoginInput input = new LoginInput(email, senha);
        Resposta<UsuarioOutput> resp = usuarioUC.login(input);
        
        if (resp.ok()) {
            usuarioLogado = resp.getDados();
            System.out.println("Bem-vindo, " + usuarioLogado.getNome() + "!");
        } else {
            System.out.println("Erro: " + resp.getMensagem());
        }
    }

    private static void criarConta() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha (min 6): ");
        String senha = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Endereco: ");
        String endereco = scanner.nextLine();

        CriarUsuarioInput input = new CriarUsuarioInput(nome, email, senha, telefone, endereco);
        Resposta<UsuarioOutput> resp = usuarioUC.criarUsuario(input);
        
        if (resp.ok()) {
            usuarioLogado = resp.getDados();
            System.out.println("Conta criada!");
        } else {
            System.out.println("Erro: " + resp.getMensagem());
        }
    }

    private static void ofertarCarona() {
        Optional<MotoristaOutput> motoristaOpt = usuarioUC.buscarMotorista(usuarioLogado.getId());
        
        if (motoristaOpt.isEmpty()) {
            System.out.print("Voce nao e motorista. Cadastrar? (S/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("S")) return;
            
            System.out.print("CNH: ");
            String cnh = scanner.nextLine();
            RegistrarMotoristaInput input = new RegistrarMotoristaInput(usuarioLogado.getId(), cnh);
            Resposta<MotoristaOutput> resp = usuarioUC.registrarMotorista(input);
            
            if (!resp.ok()) {
                System.out.println("Erro: " + resp.getMensagem());
                return;
            }
            usuarioLogado.setEhMotorista(true);
            usuarioLogado.setMotoristaId(resp.getDados().getId());
            motoristaOpt = Optional.of(resp.getDados());
        }

        int motoristaId = motoristaOpt.get().getId();
        List<VeiculoOutput> veiculos = usuarioUC.listarVeiculos(motoristaId);

        if (veiculos.isEmpty()) {
            System.out.print("Nenhum veiculo. Cadastrar? (S/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("S")) return;
            cadastrarVeiculoParaMotorista(motoristaId);
            veiculos = usuarioUC.listarVeiculos(motoristaId);
            if (veiculos.isEmpty()) return;
        }

        System.out.println("\n--- OFERTAR CARONA ---");
        System.out.print("Origem: ");
        String origem = scanner.nextLine();
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        System.out.print("Data (dd/mm/aaaa): ");
        String data = scanner.nextLine();
        System.out.print("Vagas: ");
        int vagas = lerInt();
        System.out.print("Preco R$: ");
        double preco = lerDouble();

        System.out.println("Veiculos:");
        for (VeiculoOutput v : veiculos) {
            System.out.println("  " + v.getId() + " - " + v.getMarca() + " " + v.getModelo());
        }
        System.out.print("ID Veiculo: ");
        int veiculoId = lerInt();

        CriarViagemInput input = new CriarViagemInput(veiculoId, motoristaId, origem, destino, preco, data, vagas);
        Resposta<ViagemOutput> resp = viagemUC.criarViagem(input);
        System.out.println(resp.getMensagem());
    }

    private static void buscarCaronas() {
        System.out.print("Origem: ");
        String origem = scanner.nextLine();
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        System.out.print("Data (Enter = todas): ");
        String data = scanner.nextLine();

        BuscarViagensInput input = new BuscarViagensInput(origem, destino, data.isEmpty() ? null : data);
        List<ViagemOutput> viagens = viagemUC.buscarViagens(input);

        if (viagens.isEmpty()) {
            System.out.println("Nenhuma carona encontrada.");
            return;
        }

        System.out.println("\n--- CARONAS ---");
        for (ViagemOutput v : viagens) {
            System.out.println("ID:" + v.getId() + " | " + v.getCidadeOrigem() + " -> " + v.getCidadeDestino() +
                    " | " + v.getDataViagem() + " | Vagas:" + v.getVagasDisponiveis() + "/" + v.getVagas() +
                    " | R$" + String.format("%.2f", v.getPreco()));
        }

        if (usuarioLogado != null) {
            System.out.print("\nReservar (ID ou 0): ");
            int id = lerInt();
            if (id > 0) reservarCarona(id);
        }
    }

    private static void reservarCarona(int viagemId) {
        Optional<PassageiroOutput> passageiroOpt = usuarioUC.buscarPassageiro(usuarioLogado.getId());
        
        if (passageiroOpt.isEmpty()) {
            Resposta<PassageiroOutput> resp = usuarioUC.registrarPassageiro(usuarioLogado.getId());
            if (!resp.ok()) {
                System.out.println("Erro: " + resp.getMensagem());
                return;
            }
            usuarioLogado.setEhPassageiro(true);
            usuarioLogado.setPassageiroId(resp.getDados().getId());
            passageiroOpt = Optional.of(resp.getDados());
        }

        System.out.print("Quantos lugares? ");
        int lugares = lerInt();

        ReservarViagemInput input = new ReservarViagemInput(viagemId, passageiroOpt.get().getId(), lugares);
        Resposta<Void> resp = viagemUC.reservarViagem(input);
        System.out.println(resp.getMensagem());
    }

    private static void cadastrarVeiculo() {
        Optional<MotoristaOutput> motoristaOpt = usuarioUC.buscarMotorista(usuarioLogado.getId());
        
        if (motoristaOpt.isEmpty()) {
            System.out.print("Voce nao e motorista. Cadastrar? (S/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("S")) return;
            
            System.out.print("CNH: ");
            String cnh = scanner.nextLine();
            RegistrarMotoristaInput input = new RegistrarMotoristaInput(usuarioLogado.getId(), cnh);
            Resposta<MotoristaOutput> resp = usuarioUC.registrarMotorista(input);
            
            if (!resp.ok()) {
                System.out.println("Erro: " + resp.getMensagem());
                return;
            }
            usuarioLogado.setEhMotorista(true);
            usuarioLogado.setMotoristaId(resp.getDados().getId());
            motoristaOpt = Optional.of(resp.getDados());
        }

        cadastrarVeiculoParaMotorista(motoristaOpt.get().getId());
    }

    private static void cadastrarVeiculoParaMotorista(int motoristaId) {
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Placa: ");
        String placa = scanner.nextLine();
        System.out.print("Ano: ");
        int ano = lerInt();
        System.out.print("Cor: ");
        String cor = scanner.nextLine();

        AdicionarVeiculoInput input = new AdicionarVeiculoInput(motoristaId, marca, modelo, placa, ano, cor);
        Resposta<VeiculoOutput> resp = usuarioUC.adicionarVeiculo(input);
        System.out.println(resp.getMensagem());
    }

    private static void meusVeiculos() {
        Optional<MotoristaOutput> motorista = usuarioUC.buscarMotorista(usuarioLogado.getId());
        if (motorista.isEmpty()) {
            System.out.println("Voce nao e motorista.");
            return;
        }

        List<VeiculoOutput> veiculos = usuarioUC.listarVeiculos(motorista.get().getId());
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veiculo.");
            return;
        }

        System.out.println("\n--- MEUS VEICULOS ---");
        for (VeiculoOutput v : veiculos) {
            System.out.println(v.getId() + " - " + v.getDescricao());
        }
    }

    private static void minhasCaronas() {
        Optional<MotoristaOutput> motorista = usuarioUC.buscarMotorista(usuarioLogado.getId());
        if (motorista.isEmpty()) {
            System.out.println("Voce nao e motorista.");
            return;
        }

        List<ViagemOutput> viagens = viagemUC.listarPorMotorista(motorista.get().getId());
        if (viagens.isEmpty()) {
            System.out.println("Nenhuma carona.");
            return;
        }

        System.out.println("\n--- MINHAS CARONAS ---");
        for (ViagemOutput v : viagens) {
            System.out.println(v.getId() + " | " + v.getCidadeOrigem() + " -> " + v.getCidadeDestino() +
                    " | " + v.getDataViagem() + " | " + v.getStatus());
        }
    }

    private static void meuPerfil() {
        System.out.println("\n--- PERFIL ---");
        System.out.println("Nome: " + usuarioLogado.getNome());
        System.out.println("Email: " + usuarioLogado.getEmail());
        System.out.println("Telefone: " + usuarioLogado.getTelefone());
        System.out.println("Motorista: " + (usuarioLogado.isEhMotorista() ? "Sim" : "Nao"));
        System.out.println("Passageiro: " + (usuarioLogado.isEhPassageiro() ? "Sim" : "Nao"));
    }

    private static void sair() {
        System.out.println("Ate logo!");
        ConexaoBanco.fecharPool();
        System.exit(0);
    }

    private static int lerInt() {
        try { return Integer.parseInt(scanner.nextLine()); }
        catch (Exception e) { return 0; }
    }

    private static double lerDouble() {
        try { return Double.parseDouble(scanner.nextLine().replace(",", ".")); }
        catch (Exception e) { return 0; }
    }
}
