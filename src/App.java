import java.util.List;
import java.util.Scanner;

import application.input.AdicionarVeiculoInput;
import application.input.AvaliarViagemInput;
import application.input.BuscarViagensInput;
import application.input.CriarUsuarioInput;
import application.input.CriarViagemInput;
import application.input.FinalizarViagemInput;
import application.input.LoginInput;
import application.input.RegistrarMotoristaInput;
import application.input.ReservarViagemInput;
import application.output.RelatorioGeralOutput;
import application.output.RelatorioMotoristaOutput;
import application.output.RelatorioPassageiroOutput;
import application.output.UsuarioOutput;
import application.output.VeiculoOutput;
import application.output.ViagemOutput;
import application.usecase.RelatorioUseCase;
import application.usecase.UsuarioUseCase;
import application.usecase.VeiculoUseCase;
import application.usecase.ViagemUseCase;
import infrastructure.BancoDeDados;

public class App {
    private static Scanner scanner = new Scanner(System.in);
    private static UsuarioOutput usuarioLogado = null;
    private static UsuarioUseCase usuarioUC = new UsuarioUseCase();
    private static VeiculoUseCase veiculoUC = new VeiculoUseCase();
    private static ViagemUseCase viagemUC = new ViagemUseCase();
    private static RelatorioUseCase relatorioUC = new RelatorioUseCase();

    public static void main(String[] args) {
        System.out.println("Iniciando...");
        
        if (BancoDeDados.testar()) {
            System.out.println("Banco conectado!");
            BancoDeDados.criarTabelas();
        } else {
            System.out.println("ERRO: Sem conexao com banco!");
        }
        
        System.out.println("\n========================================");
        System.out.println("         BLABLACAR - CARONAS");
        System.out.println("========================================");

        while (true) {
            if (usuarioLogado == null) menuInicial();
            else menuPrincipal();
        }
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
            case 0 -> System.exit(0);
        }
    }

    private static void menuPrincipal() {
        System.out.println("\n--- MENU [" + usuarioLogado.nome + "] ---");
        System.out.println("1. Ofertar Carona");
        System.out.println("2. Buscar Caronas");
        System.out.println("3. Minhas Caronas (Motorista)");
        System.out.println("4. Minhas Viagens (Passageiro)");
        System.out.println("5. Finalizar Viagem");
        System.out.println("6. Avaliar Viagem");
        System.out.println("7. Cadastrar Veiculo");
        System.out.println("8. Meus Veiculos");
        System.out.println("9. Relatorios");
        System.out.println("10. Meu Perfil");
        System.out.println("11. Logout");
        System.out.println("0. Sair");
        System.out.print("Opcao: ");

        switch (lerInt()) {
            case 1 -> ofertarCarona();
            case 2 -> buscarCaronas();
            case 3 -> minhasCaronas();
            case 4 -> minhasViagensPassageiro();
            case 5 -> finalizarViagem();
            case 6 -> avaliarViagem();
            case 7 -> cadastrarVeiculo();
            case 8 -> meusVeiculos();
            case 9 -> menuRelatorios();
            case 10 -> meuPerfil();
            case 11 -> { usuarioLogado = null; System.out.println("Logout!"); }
            case 0 -> System.exit(0);
        }
    }

    private static void login() {
        System.out.println("\n--- LOGIN --- (Enter para voltar)");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        if (email.isEmpty()) return;
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        if (senha.isEmpty()) return;

        UsuarioOutput usuario = usuarioUC.login(new LoginInput(email, senha));
        if (usuario != null) {
            usuarioLogado = usuario;
            System.out.println("Bem-vindo, " + usuario.nome + "!");
        } else {
            System.out.println("Email ou senha invalidos!");
        }
    }

    private static void criarConta() {
        System.out.println("\n--- CRIAR CONTA --- (Enter para voltar)");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        if (nome.isEmpty()) return;
        System.out.print("Email: ");
        String email = scanner.nextLine();
        if (email.isEmpty()) return;
        System.out.print("Senha (min 6): ");
        String senha = scanner.nextLine();
        if (senha.isEmpty()) return;
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Endereco: ");
        String endereco = scanner.nextLine();

        UsuarioOutput usuario = usuarioUC.criarUsuario(new CriarUsuarioInput(nome, email, senha, telefone, endereco));
        if (usuario != null) {
            usuarioLogado = usuario;
            System.out.println("Conta criada!");
        } else {
            System.out.println("Erro ao criar conta!");
        }
    }

    private static void ofertarCarona() {
        System.out.println("\n--- OFERTAR CARONA --- (Enter para voltar)");
        
        int motoristaId = usuarioUC.buscarMotoristaId(usuarioLogado.id);
        if (motoristaId == 0) {
            System.out.print("Voce nao e motorista. Cadastrar? (S/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("S")) return;
            System.out.print("CNH: ");
            String cnh = scanner.nextLine();
            if (cnh.isEmpty()) return;
            motoristaId = usuarioUC.registrarMotorista(new RegistrarMotoristaInput(usuarioLogado.id, cnh));
            if (motoristaId == 0) { System.out.println("Erro!"); return; }
            usuarioLogado.ehMotorista = true;
            usuarioLogado.motoristaId = motoristaId;
        }

        List<VeiculoOutput> veiculos = veiculoUC.listar(motoristaId);
        if (veiculos.isEmpty()) {
            System.out.print("Nenhum veiculo. Cadastrar? (S/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("S")) return;
            if (!cadastrarVeiculoParaMotorista(motoristaId)) return;
            veiculos = veiculoUC.listar(motoristaId);
        }

        System.out.print("Origem: ");
        String origem = scanner.nextLine();
        if (origem.isEmpty()) return;
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        if (destino.isEmpty()) return;
        System.out.print("Data (dd/mm/aaaa): ");
        String data = scanner.nextLine();
        if (data.isEmpty()) return;
        System.out.print("Vagas: ");
        int vagas = lerInt();
        if (vagas <= 0) return;
        System.out.print("Preco R$: ");
        double preco = lerDouble();

        System.out.println("Veiculos:");
        for (VeiculoOutput v : veiculos) System.out.println("  " + v.id + " - " + v.marca + " " + v.modelo);
        System.out.print("ID Veiculo (0 voltar): ");
        int veiculoId = lerInt();
        if (veiculoId <= 0) return;

        int id = viagemUC.criar(new CriarViagemInput(veiculoId, motoristaId, origem, destino, preco, data, vagas));
        System.out.println(id > 0 ? "Viagem criada!" : "Erro!");
    }

    private static void buscarCaronas() {
        System.out.println("\n--- BUSCAR CARONAS --- (Enter para voltar)");
        System.out.print("Origem: ");
        String origem = scanner.nextLine();
        if (origem.isEmpty()) return;
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        if (destino.isEmpty()) return;
        System.out.print("Data (Enter = todas): ");
        String data = scanner.nextLine();

        List<ViagemOutput> viagens = viagemUC.buscar(new BuscarViagensInput(origem, destino, data.isEmpty() ? null : data));
        if (viagens.isEmpty()) { System.out.println("Nenhuma carona."); return; }

        System.out.println("\n--- CARONAS DISPONIVEIS ---");
        for (ViagemOutput v : viagens) {
            System.out.println("ID:" + v.id + " | " + v.cidadeOrigem + " -> " + v.cidadeDestino +
                " | " + v.dataViagem + " | Vagas:" + v.vagasDisponiveis + "/" + v.vagas +
                " | R$" + String.format("%.2f", v.preco) + " | " + v.nomeMotorista);
        }

        if (usuarioLogado != null) {
            System.out.print("\nReservar (ID ou 0): ");
            int id = lerInt();
            if (id > 0) reservarCarona(id);
        }
    }

    private static void reservarCarona(int viagemId) {
        int passageiroId = usuarioUC.buscarPassageiroId(usuarioLogado.id);
        if (passageiroId == 0) {
            passageiroId = usuarioUC.registrarPassageiro(usuarioLogado.id);
            usuarioLogado.ehPassageiro = true;
            usuarioLogado.passageiroId = passageiroId;
        }

        System.out.print("Quantos lugares? (0 voltar): ");
        int lugares = lerInt();
        if (lugares <= 0) return;

        String resultado = viagemUC.reservar(new ReservarViagemInput(viagemId, passageiroId, lugares));
        System.out.println(resultado);
    }

    private static void minhasCaronas() {
        int motoristaId = usuarioUC.buscarMotoristaId(usuarioLogado.id);
        if (motoristaId == 0) { System.out.println("Voce nao e motorista."); return; }

        List<ViagemOutput> viagens = viagemUC.listarPorMotorista(motoristaId);
        if (viagens.isEmpty()) { System.out.println("Nenhuma carona."); return; }

        System.out.println("\n--- MINHAS CARONAS (MOTORISTA) ---");
        for (ViagemOutput v : viagens) {
            System.out.println("ID:" + v.id + " | " + v.cidadeOrigem + " -> " + v.cidadeDestino +
                " | " + v.dataViagem + " | " + v.status + 
                " | Vagas:" + v.vagasDisponiveis + "/" + v.vagas);
        }
    }

    private static void minhasViagensPassageiro() {
        int passageiroId = usuarioUC.buscarPassageiroId(usuarioLogado.id);
        if (passageiroId == 0) { System.out.println("Voce nao e passageiro."); return; }

        List<ViagemOutput> viagens = viagemUC.listarPorPassageiro(passageiroId);
        if (viagens.isEmpty()) { System.out.println("Nenhuma viagem."); return; }

        System.out.println("\n--- MINHAS VIAGENS (PASSAGEIRO) ---");
        for (ViagemOutput v : viagens) {
            String avaliacao = v.minhaAvaliacao > 0 ? " | Avaliacao:" + v.minhaAvaliacao + "/5" : " | Nao avaliada";
            System.out.println("ID:" + v.id + " | " + v.cidadeOrigem + " -> " + v.cidadeDestino +
                " | " + v.dataViagem + " | " + v.status + " | " + v.nomeMotorista + avaliacao);
        }
    }

    private static void finalizarViagem() {
        int motoristaId = usuarioUC.buscarMotoristaId(usuarioLogado.id);
        if (motoristaId == 0) { System.out.println("Voce nao e motorista."); return; }

        List<ViagemOutput> viagens = viagemUC.listarPorMotorista(motoristaId);
        List<ViagemOutput> pendentes = viagens.stream()
            .filter(v -> !v.status.equals("FINALIZADA") && !v.status.equals("CANCELADA"))
            .toList();

        if (pendentes.isEmpty()) { System.out.println("Nenhuma viagem para finalizar."); return; }

        System.out.println("\n--- FINALIZAR VIAGEM ---");
        for (ViagemOutput v : pendentes) {
            System.out.println("ID:" + v.id + " | " + v.cidadeOrigem + " -> " + v.cidadeDestino +
                " | " + v.dataViagem + " | " + v.status);
        }

        System.out.print("\nID da viagem (0 voltar): ");
        int viagemId = lerInt();
        if (viagemId <= 0) return;

        String resultado = viagemUC.finalizar(new FinalizarViagemInput(viagemId, motoristaId));
        System.out.println(resultado);
    }

    private static void avaliarViagem() {
        int passageiroId = usuarioUC.buscarPassageiroId(usuarioLogado.id);
        if (passageiroId == 0) { System.out.println("Voce nao e passageiro."); return; }

        List<ViagemOutput> viagens = viagemUC.listarPorPassageiro(passageiroId);
        List<ViagemOutput> finalizadas = viagens.stream()
            .filter(v -> v.status.equals("FINALIZADA") && v.minhaAvaliacao == 0)
            .toList();

        if (finalizadas.isEmpty()) { System.out.println("Nenhuma viagem para avaliar."); return; }

        System.out.println("\n--- AVALIAR VIAGEM ---");
        for (ViagemOutput v : finalizadas) {
            System.out.println("ID:" + v.id + " | " + v.cidadeOrigem + " -> " + v.cidadeDestino +
                " | " + v.dataViagem + " | Motorista: " + v.nomeMotorista);
        }

        System.out.print("\nID da viagem (0 voltar): ");
        int viagemId = lerInt();
        if (viagemId <= 0) return;

        System.out.print("Nota (1-5): ");
        int nota = lerInt();

        String resultado = viagemUC.avaliar(new AvaliarViagemInput(viagemId, passageiroId, nota));
        System.out.println(resultado);
    }

    private static void menuRelatorios() {
        System.out.println("\n--- RELATORIOS ---");
        System.out.println("1. Meu Relatorio (Motorista)");
        System.out.println("2. Meu Relatorio (Passageiro)");
        System.out.println("3. Relatorio Geral do Sistema");
        System.out.println("0. Voltar");
        System.out.print("Opcao: ");

        switch (lerInt()) {
            case 1 -> relatorioMotorista();
            case 2 -> relatorioPassageiro();
            case 3 -> relatorioGeral();
        }
    }

    private static void relatorioMotorista() {
        int motoristaId = usuarioUC.buscarMotoristaId(usuarioLogado.id);
        if (motoristaId == 0) { System.out.println("Voce nao e motorista."); return; }

        RelatorioMotoristaOutput r = relatorioUC.relatorioMotorista(motoristaId, usuarioLogado.nome);
        if (r == null) { System.out.println("Erro ao gerar relatorio."); return; }

        System.out.println("\n========================================");
        System.out.println("      RELATORIO DO MOTORISTA");
        System.out.println("========================================");
        System.out.println("Nome: " + r.nome);
        System.out.println("Total de Viagens: " + r.totalViagens);
        System.out.println("Viagens Finalizadas: " + r.viagensFinalizadas);
        System.out.println("Viagens Canceladas: " + r.viagensCanceladas);
        System.out.println("Media de Avaliacao: " + String.format("%.1f", r.mediaAvaliacao) + "/5");
        System.out.println("Total Faturado: R$ " + String.format("%.2f", r.totalFaturado));
        System.out.println("========================================");
    }

    private static void relatorioPassageiro() {
        int passageiroId = usuarioUC.buscarPassageiroId(usuarioLogado.id);
        if (passageiroId == 0) { System.out.println("Voce nao e passageiro."); return; }

        RelatorioPassageiroOutput r = relatorioUC.relatorioPassageiro(passageiroId, usuarioLogado.nome);
        if (r == null) { System.out.println("Erro ao gerar relatorio."); return; }

        System.out.println("\n========================================");
        System.out.println("      RELATORIO DO PASSAGEIRO");
        System.out.println("========================================");
        System.out.println("Nome: " + r.nome);
        System.out.println("Total de Viagens: " + r.totalViagens);
        System.out.println("Avaliacoes Feitas: " + r.avaliacoesFeitas);
        System.out.println("Total Gasto: R$ " + String.format("%.2f", r.totalGasto));
        System.out.println("========================================");
    }

    private static void relatorioGeral() {
        RelatorioGeralOutput r = relatorioUC.relatorioGeral();
        if (r == null) { System.out.println("Erro ao gerar relatorio."); return; }

        System.out.println("\n========================================");
        System.out.println("      RELATORIO GERAL DO SISTEMA");
        System.out.println("========================================");
        System.out.println("Total de Usuarios: " + r.totalUsuarios);
        System.out.println("Total de Motoristas: " + r.totalMotoristas);
        System.out.println("Total de Passageiros: " + r.totalPassageiros);
        System.out.println("----------------------------------------");
        System.out.println("Total de Viagens: " + r.totalViagens);
        System.out.println("Viagens Finalizadas: " + r.viagensFinalizadas);
        System.out.println("Viagens Pendentes: " + r.viagensPendentes);
        System.out.println("----------------------------------------");
        System.out.println("Faturamento Total: R$ " + String.format("%.2f", r.faturamentoTotal));
        System.out.println("========================================");
    }

    private static void cadastrarVeiculo() {
        int motoristaId = usuarioUC.buscarMotoristaId(usuarioLogado.id);
        if (motoristaId == 0) {
            System.out.print("Voce nao e motorista. Cadastrar? (S/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("S")) return;
            System.out.print("CNH: ");
            String cnh = scanner.nextLine();
            if (cnh.isEmpty()) return;
            motoristaId = usuarioUC.registrarMotorista(new RegistrarMotoristaInput(usuarioLogado.id, cnh));
            if (motoristaId == 0) { System.out.println("Erro!"); return; }
            usuarioLogado.ehMotorista = true;
            usuarioLogado.motoristaId = motoristaId;
        }
        cadastrarVeiculoParaMotorista(motoristaId);
    }

    private static boolean cadastrarVeiculoParaMotorista(int motoristaId) {
        System.out.print("Marca: ");
        String marca = scanner.nextLine();
        if (marca.isEmpty()) return false;
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        if (modelo.isEmpty()) return false;
        System.out.print("Placa: ");
        String placa = scanner.nextLine();
        if (placa.isEmpty()) return false;
        System.out.print("Ano: ");
        int ano = lerInt();
        if (ano <= 0) return false;
        System.out.print("Cor: ");
        String cor = scanner.nextLine();

        int id = veiculoUC.adicionar(new AdicionarVeiculoInput(motoristaId, marca, modelo, placa, ano, cor));
        System.out.println(id > 0 ? "Veiculo cadastrado!" : "Erro!");
        return id > 0;
    }

    private static void meusVeiculos() {
        int motoristaId = usuarioUC.buscarMotoristaId(usuarioLogado.id);
        if (motoristaId == 0) { System.out.println("Voce nao e motorista."); return; }
        List<VeiculoOutput> veiculos = veiculoUC.listar(motoristaId);
        if (veiculos.isEmpty()) { System.out.println("Nenhum veiculo."); return; }
        System.out.println("\n--- MEUS VEICULOS ---");
        for (VeiculoOutput v : veiculos) System.out.println(v.id + " - " + v.getDescricao());
    }

    private static void meuPerfil() {
        System.out.println("\n--- PERFIL ---");
        System.out.println("Nome: " + usuarioLogado.nome);
        System.out.println("Email: " + usuarioLogado.email);
        System.out.println("Telefone: " + usuarioLogado.telefone);
        System.out.println("Motorista: " + (usuarioLogado.ehMotorista ? "Sim" : "Nao"));
        System.out.println("Passageiro: " + (usuarioLogado.ehPassageiro ? "Sim" : "Nao"));
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