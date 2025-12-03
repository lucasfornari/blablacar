package application;

import application.input.*;
import application.output.*;
import domain.entities.*;
import domain.repositories.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioUseCase {
    private final IUsuarioRepository usuarioRepo;
    private final IMotoristaRepository motoristaRepo;
    private final IPassageiroRepository passageiroRepo;
    private final IVeiculoRepository veiculoRepo;

    public UsuarioUseCase(IUsuarioRepository usuarioRepo, IMotoristaRepository motoristaRepo,
                          IPassageiroRepository passageiroRepo, IVeiculoRepository veiculoRepo) {
        this.usuarioRepo = usuarioRepo;
        this.motoristaRepo = motoristaRepo;
        this.passageiroRepo = passageiroRepo;
        this.veiculoRepo = veiculoRepo;
    }

    public Resposta<UsuarioOutput> criarUsuario(CriarUsuarioInput input) {
        if (!input.isValido()) {
            return Resposta.erro("Dados invalidos");
        }
        if (usuarioRepo.existePorEmail(input.getEmail())) {
            return Resposta.erro("Email ja cadastrado");
        }

        Usuario usuario = new Usuario(
            input.getNome(), 
            input.getEmail(), 
            input.getSenha(), 
            input.getTelefone(), 
            input.getEndereco()
        );
        Usuario salvo = usuarioRepo.salvar(usuario);
        return Resposta.sucesso("Usuario criado", toUsuarioOutput(salvo));
    }

    public Resposta<UsuarioOutput> login(LoginInput input) {
        if (!input.isValido()) {
            return Resposta.erro("Email e senha obrigatorios");
        }

        Optional<Usuario> usuario = usuarioRepo.login(input.getEmail(), input.getSenha());
        if (usuario.isPresent()) {
            return Resposta.sucesso("Login realizado", toUsuarioOutput(usuario.get()));
        }
        return Resposta.erro("Email ou senha invalidos");
    }

    public Resposta<MotoristaOutput> registrarMotorista(RegistrarMotoristaInput input) {
        if (!input.isValido()) {
            return Resposta.erro("Dados invalidos");
        }
        if (motoristaRepo.existePorCnh(input.getCnh())) {
            return Resposta.erro("CNH ja cadastrada");
        }

        Motorista motorista = new Motorista(input.getCnh());
        Motorista salvo = motoristaRepo.salvar(motorista, input.getUsuarioId());
        return Resposta.sucesso("Motorista registrado", toMotoristaOutput(salvo));
    }

    public Resposta<PassageiroOutput> registrarPassageiro(int usuarioId) {
        Optional<Passageiro> existente = passageiroRepo.buscarPorUsuarioId(usuarioId);
        if (existente.isPresent()) {
            return Resposta.sucesso("Ja e passageiro", toPassageiroOutput(existente.get()));
        }

        Passageiro passageiro = new Passageiro();
        Passageiro salvo = passageiroRepo.salvar(passageiro, usuarioId);
        return Resposta.sucesso("Passageiro registrado", toPassageiroOutput(salvo));
    }

    public Resposta<VeiculoOutput> adicionarVeiculo(AdicionarVeiculoInput input) {
        if (!input.isValido()) {
            return Resposta.erro("Dados invalidos");
        }
        if (veiculoRepo.existePorPlaca(input.getPlaca())) {
            return Resposta.erro("Placa ja cadastrada");
        }

        Veiculo veiculo = new Veiculo(
            input.getMarca(), 
            input.getModelo(), 
            input.getPlaca(), 
            input.getAno(), 
            input.getCor()
        );
        Veiculo salvo = veiculoRepo.salvar(veiculo, input.getMotoristaId());
        return Resposta.sucesso("Veiculo adicionado", toVeiculoOutput(salvo));
    }

    public Optional<MotoristaOutput> buscarMotorista(int usuarioId) {
        return motoristaRepo.buscarPorUsuarioId(usuarioId).map(this::toMotoristaOutput);
    }

    public Optional<PassageiroOutput> buscarPassageiro(int usuarioId) {
        return passageiroRepo.buscarPorUsuarioId(usuarioId).map(this::toPassageiroOutput);
    }

    public List<VeiculoOutput> listarVeiculos(int motoristaId) {
        return veiculoRepo.buscarPorMotoristaId(motoristaId)
            .stream()
            .map(this::toVeiculoOutput)
            .collect(Collectors.toList());
    }

    private UsuarioOutput toUsuarioOutput(Usuario u) {
        return new UsuarioOutput(
            u.getId(),
            u.getNome(),
            u.getEmail(),
            u.getTelefone(),
            u.getEndereco(),
            u.ehMotorista(),
            u.ehPassageiro(),
            u.getMotorista() != null ? u.getMotorista().getId() : 0,
            u.getPassageiro() != null ? u.getPassageiro().getId() : 0
        );
    }

    private MotoristaOutput toMotoristaOutput(Motorista m) {
        return new MotoristaOutput(m.getId(), m.getCnh(), m.getNumeroDeViagens());
    }

    private PassageiroOutput toPassageiroOutput(Passageiro p) {
        return new PassageiroOutput(p.getId(), p.getNumeroDeViagens());
    }

    private VeiculoOutput toVeiculoOutput(Veiculo v) {
        return new VeiculoOutput(v.getId(), v.getMarca(), v.getModelo(), v.getPlaca(), v.getAno(), v.getCor());
    }
}
