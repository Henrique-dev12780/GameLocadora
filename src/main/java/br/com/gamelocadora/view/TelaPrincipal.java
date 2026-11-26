
package br.com.gamelocadora.view;

import br.com.gamelocadora.dao.JdbcLocadoraDAO;
import br.com.gamelocadora.dao.LocadoraDAO;
import br.com.gamelocadora.exception.RegraNegocioException;
import br.com.gamelocadora.model.Cliente;
import br.com.gamelocadora.model.JogoCartucho;
import br.com.gamelocadora.model.JogoDisco;
import br.com.gamelocadora.model.Midia;
import br.com.gamelocadora.service.LocadoraService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TelaPrincipal extends JFrame {

    private final LocadoraDAO dao = new JdbcLocadoraDAO();

    private final LocadoraService service = new LocadoraService(dao);

    public TelaPrincipal() {

        super("GameLocadora - Sistema de locação de jogos físicos");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Clientes", abaClientes());
        tabs.add("Jogos", abaJogos());
        tabs.add("Exemplares", abaExemplares());
        tabs.add("Aluguéis", abaAlugueis());
        tabs.add("Consulta JOIN", abaJoin());

        setContentPane(tabs);
    }

    private JPanel painel(
        String titulo,
        JTextArea area
    ) {

        JPanel p = new JPanel(
            new BorderLayout(8, 8)
        );

        p.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        p.add(
            new JLabel(titulo),
            BorderLayout.NORTH
        );

        area.setEditable(false);

        area.setFont(
            new Font(
                Font.MONOSPACED,
                Font.PLAIN,
                12
            )
        );

        p.add(
            new JScrollPane(area),
            BorderLayout.CENTER
        );

        return p;
    }

    private interface Acao {

        void executar() throws Exception;
    }

    private JButton botao(
        String nome,
        Acao r
    ) {

        JButton b = new JButton(nome);

        b.addActionListener(
            e -> executar(r)
        );

        return b;
    }

    private void executar(Acao r) {

        try {

            r.executar();

        } catch (RegraNegocioException e) {

            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Regra de negócio",
                JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Operação não concluída: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String pedir(String rotulo) {

        return JOptionPane.showInputDialog(
            this,
            rotulo
        );
    }

    private long numero(String rotulo) {

        return Long.parseLong(
            pedir(rotulo).trim()
        );
    }

    private JPanel botoes(JButton... bs) {

        JPanel p = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        for (JButton b : bs) {
            p.add(b);
        }

        return p;
    }

    private JPanel abaClientes() {

        JTextArea area = new JTextArea();

        JPanel painel = painel(
            "Cadastro e manutenção de clientes",
            area
        );

        JTextField nome = new JTextField(18);
        JTextField email = new JTextField(22);
        JTextField telefone = new JTextField(13);

        JPanel formulario = new JPanel(
            new FlowLayout(
                FlowLayout.LEFT,
                8,
                5
            )
        );

        formulario.add(new JLabel("Nome:"));
        formulario.add(nome);

        formulario.add(new JLabel("E-mail:"));
        formulario.add(email);

        formulario.add(new JLabel("Telefone:"));
        formulario.add(telefone);

        JPanel comandos = botoes(

            botao("Inserir", () -> {

                long novo = dao.inserirCliente(
                    new Cliente(
                        null,
                        nome.getText(),
                        email.getText(),
                        telefone.getText(),
                        true
                    )
                );

                JOptionPane.showMessageDialog(
                    this,
                    "Cliente cadastrado com ID: " + novo
                );

                listar(
                    area,
                    dao.listarClientes()
                );
            }),

            botao("Listar", () -> {

                listar(
                    area,
                    dao.listarClientes()
                );
            }),

            botao("Alterar", () -> {

                long chave = numero("ID do cliente");

                dao.atualizarCliente(
                    chave,
                    new Cliente(
                        null,
                        nome.getText(),
                        email.getText(),
                        telefone.getText(),
                        true
                    )
                );

                listar(
                    area,
                    dao.listarClientes()
                );
            }),

            botao("Excluir (inativar)", () -> {

                long chave = numero("ID do cliente");

                int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Inativar cliente " + chave + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
                );

                if (resposta == JOptionPane.YES_OPTION) {

                    dao.excluirCliente(chave);

                    listar(
                        area,
                        dao.listarClientes()
                    );
                }
            }),

            botao("Limpar campos", () -> {

                nome.setText("");
                email.setText("");
                telefone.setText("");
            })
        );

        JPanel superior = new JPanel(
            new BorderLayout()
        );

        superior.add(
            formulario,
            BorderLayout.NORTH
        );

        superior.add(
            comandos,
            BorderLayout.SOUTH
        );

        painel.add(
            superior,
            BorderLayout.SOUTH
        );

        return painel;
    }

    private JPanel abaJogos() {

        JTextArea a = new JTextArea();

        JPanel p = painel(
            "Catálogo de jogos",
            a
        );

        p.add(
            botoes(

                botao("Inserir jogo", () -> {

                    Midia j = novoJogo();

                    dao.inserirJogo(j);

                    listar(
                        a,
                        dao.listarJogos()
                    );
                }),

                botao(
                    "Cadastrar jogo + 1 exemplar (transação)",
                    () -> {

                        Midia j = novoJogo();

                        String cod = pedir(
                            "Código do exemplar"
                        );

                        dao.inserirJogoComExemplar(
                            j,
                            cod,
                            "BOM",
                            ""
                        );

                        listar(
                            a,
                            dao.listarJogos()
                        );
                    }
                ),

                botao("Listar", () -> {

                    listar(
                        a,
                        dao.listarJogos()
                    );
                }),

                botao("Alterar", () -> {

                    long id = numero("ID do jogo");

                    dao.atualizarJogo(
                        id,
                        novoJogo()
                    );

                    listar(
                        a,
                        dao.listarJogos()
                    );
                }),

                botao("Excluir (inativar)", () -> {

                    dao.excluirJogo(
                        numero("ID do jogo")
                    );

                    listar(
                        a,
                        dao.listarJogos()
                    );
                })
            ),
            BorderLayout.SOUTH
        );

        return p;
    }

    private Midia novoJogo() {

        String titulo = pedir("Título");

        String plataforma = pedir("Plataforma");

        String genero = pedir("Gênero");

        String formato = pedir(
            "Formato: DISCO ou CARTUCHO"
        ).trim().toUpperCase();

        if (
            !formato.equals("DISCO") &&
            !formato.equals("CARTUCHO")
        ) {
            throw new RegraNegocioException(
                "Formato deve ser DISCO ou CARTUCHO."
            );
        }

        BigDecimal diaria = new BigDecimal(
            pedir("Valor da diária (ex.: 8.00)")
                .replace(',', '.')
        );

        return formato.equals("CARTUCHO")
            ? new JogoCartucho(
                titulo,
                plataforma,
                genero,
                diaria
            )
            : new JogoDisco(
                titulo,
                plataforma,
                genero,
                diaria
            );
    }

    private JPanel abaExemplares() {

        JTextArea a = new JTextArea();

        JPanel p = painel(
            "Cópias físicas individuais",
            a
        );

        p.add(
            botoes(

                botao("Inserir", () -> {

                    long j = numero("ID do jogo");

                    String c = pedir("Código único");

                    dao.inserirExemplar(
                        j,
                        c,
                        "BOM",
                        pedir("Observação (opcional)")
                    );

                    listar(
                        a,
                        dao.listarExemplares()
                    );
                }),

                botao("Listar", () -> {

                    listar(
                        a,
                        dao.listarExemplares()
                    );
                }),

                botao("Disponíveis", () -> {

                    listar(
                        a,
                        dao.listarDisponiveis()
                    );
                }),

                botao("Alterar", () -> {

                    long id = numero("ID exemplar");

                    long j = numero("ID jogo");

                    String c = pedir("Código");

                    String e = pedir(
                        "Estado: NOVO, BOM ou DESGASTADO"
                    );

                    String o = pedir("Observação");

                    dao.atualizarExemplar(
                        id,
                        j,
                        c,
                        e,
                        o,
                        true
                    );

                    listar(
                        a,
                        dao.listarExemplares()
                    );
                }),

                botao("Excluir (inativar)", () -> {

                    dao.excluirExemplar(
                        numero("ID exemplar")
                    );

                    listar(
                        a,
                        dao.listarExemplares()
                    );
                })
            ),
            BorderLayout.SOUTH
        );

        return p;
    }

    private JPanel abaAlugueis() {

        JTextArea a = new JTextArea();

        JPanel p = painel(
            "Locações, devoluções e multas",
            a
        );

        p.add(
            botoes(

                botao("Alugar exemplar", () -> {

                    long c = numero("ID cliente");

                    long e = numero("ID exemplar");

                    long id = service.alugar(c, e);

                    JOptionPane.showMessageDialog(
                        this,
                        "Aluguel registrado: " + id +
                        " | prazo de 7 dias."
                    );

                    listar(
                        a,
                        dao.listarAlugueis()
                    );
                }),

                botao("Devolver", () -> {

                    long id = numero("ID aluguel");

                    BigDecimal m = service.devolver(id);

                    JOptionPane.showMessageDialog(
                        this,
                        "Devolução registrada. Multa: R$ " + m
                    );

                    listar(
                        a,
                        dao.listarAlugueis()
                    );
                }),

                botao("Listar todos", () -> {

                    listar(
                        a,
                        dao.listarAlugueis()
                    );
                }),

                botao("Em aberto", () -> {

                    listar(
                        a,
                        dao.listarAbertos()
                    );
                }),

                botao("Atrasados", () -> {

                    listar(
                        a,
                        dao.listarAtrasados(LocalDate.now())
                    );
                }),

                botao("Alterar observação", () -> {

                    long id = numero("ID aluguel");

                    dao.atualizarObservacaoAluguel(
                        id,
                        pedir("Nova observação")
                    );

                    listar(
                        a,
                        dao.listarAlugueis()
                    );
                }),

                botao("Excluir registro", () -> {

                    long id = numero("ID aluguel");

                    int ok = JOptionPane.showConfirmDialog(
                        this,
                        "Excluir definitivamente o aluguel " +
                        id +
                        "? Esta ação remove o histórico.",
                        "Confirmar exclusão",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (ok == JOptionPane.YES_OPTION) {
                        dao.excluirAluguel(id);
                    }

                    listar(
                        a,
                        dao.listarAlugueis()
                    );
                })
            ),
            BorderLayout.SOUTH
        );

        return p;
    }

    private JPanel abaJoin() {

        JTextArea a = new JTextArea();

        JPanel p = painel(
            "Relatório integrado de aluguéis " +
            "(JOIN cliente + aluguel + exemplar + jogo)",
            a
        );

        p.add(
            botoes(
                botao("Executar consulta JOIN", () -> {

                    listar(
                        a,
                        dao.consultaJoin()
                    );
                })
            ),
            BorderLayout.SOUTH
        );

        return p;
    }

    private void listar(
        JTextArea a,
        java.util.List<String> dados
    ) {

        a.setText(
            String.join("\n", dados)
        );

        if (dados.isEmpty()) {
            a.setText(
                "Nenhum registro encontrado."
            );
        }
    }
}
