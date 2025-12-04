package application.usecase;

import application.input.CriarUsuarioInput;
import application.input.LoginInput;
import application.input.RegistrarMotoristaInput;
import application.output.UsuarioOutput;
import application.repository.MotoristaRepository;
import application.repository.PassageiroRepository;
import application.repository.UsuarioRepository;

public class UsuarioUseCase {

    private UsuarioRepository usuarioRepo = new UsuarioRepository();
    private MotoristaRepository motoristaRepo = new MotoristaRepository();
    private PassageiroRepository passageiroRepo = new PassageiroRepository();

    public UsuarioOutput criarUsuario(CriarUsuarioInput input) {
        if (input.nome == null || input.nome.isEmpty()) {
            return null;
        }
        if (input.email == null || input.email.isEmpty()) {
            return null;
        }
        if (input.senha == null || input.senha.length() < 6) {
            return null;
        }
        if (usuarioRepo.emailExiste(input.email)) {
            return null;
        }

        int id = usuarioRepo.salvar(input.nome, input.email, input.senha, input.telefone, input.endereco);
        if (id > 0) {
            return new UsuarioOutput(id, input.nome, input.email, input.telefone, input.endereco, false, false, 0, 0);
        }
        return null;
    }

    public UsuarioOutput login(LoginInput input) {
        if (input.email == null || input.senha == null) {
            return null;
        }
        return usuarioRepo.buscarPorEmailSenha(input.email, input.senha);
    }

    public int registrarMotorista(RegistrarMotoristaInput input) {
        if (input.cnh == null || input.cnh.isEmpty()) {
            return 0;
        }
        if (motoristaRepo.cnhExiste(input.cnh)) {
            return 0;
        }
        return motoristaRepo.salvar(input.usuarioId, input.cnh);
    }

    public int registrarPassageiro(int usuarioId) {
        int existente = passageiroRepo.buscarPorUsuarioId(usuarioId);
        if (existente > 0) {
            return existente;
        }
        return passageiroRepo.salvar(usuarioId);
    }

    public int buscarMotoristaId(int usuarioId) {
        return motoristaRepo.buscarPorUsuarioId(usuarioId);
    }

    public int buscarPassageiroId(int usuarioId) {
        return passageiroRepo.buscarPorUsuarioId(usuarioId);
    }
}
