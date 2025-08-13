import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Loja {
    static Scanner scanner = new Scanner(System.in);
    static Map<String, Produto> produtos = new HashMap<>();
    static final String PASTA_IMAGENS = "imagens";

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Editar produto");
            System.out.println("3 - Excluir produto");
            System.out.println("4 - Listar produtos");
            System.out.println("5 - Vender produto");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> editarProduto();
                case 3 -> excluirProduto();
                case 4 -> listarProdutos();
                case 5 -> venderProduto();
                case 0 -> System.out.println("Saindo do sistema...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    static void cadastrarProduto() {
        System.out.print("Código do produto: ");
        String codigo = scanner.nextLine();

        if (produtos.containsKey(codigo)) {
            System.out.println("Produto já cadastrado.");
            return;
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Preço: R$ ");
        double preco = scanner.nextDouble();
        System.out.print("Estoque: ");
        int estoque = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        System.out.print("Caminho da imagem do produto (arquivo local): ");
        String caminhoOriginal = scanner.nextLine();

        // Criar pasta imagens se não existir
        File pastaImagens = new File(PASTA_IMAGENS);
        if (!pastaImagens.exists()) {
            pastaImagens.mkdirs();
        }

        // Pega nome do arquivo da imagem
        String nomeImagem = new File(caminhoOriginal).getName();
        String caminhoDestino = PASTA_IMAGENS + File.separator + nomeImagem;

        // Copiar imagem para pasta imagens
        try {
            Files.copy(Paths.get(caminhoOriginal),
                       Paths.get(caminhoDestino),
                       StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Imagem copiada com sucesso para: " + caminhoDestino);
        } catch (IOException e) {
            System.out.println("Erro ao copiar a imagem: " + e.getMessage());
            return;
        }

        produtos.put(codigo, new Produto(codigo, nome, preco, estoque, caminhoDestino));
        System.out.println("Produto cadastrado com sucesso!");
    }

    static void editarProduto() {
        System.out.print("Código do produto a editar: ");
        String codigo = scanner.nextLine();

        Produto produto = produtos.get(codigo);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo preço: R$ ");
        double preco = scanner.nextDouble();
        System.out.print("Novo estoque: ");
        int estoque = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Novo caminho da imagem do produto (ou ENTER para manter): ");
        String novoCaminhoImagem = scanner.nextLine();

        if (!novoCaminhoImagem.isBlank()) {
            // Copiar nova imagem
            File pastaImagens = new File(PASTA_IMAGENS);
            if (!pastaImagens.exists()) {
                pastaImagens.mkdirs();
            }
            String nomeImagem = new File(novoCaminhoImagem).getName();
            String caminhoDestino = PASTA_IMAGENS + File.separator + nomeImagem;
            try {
                Files.copy(Paths.get(novoCaminhoImagem),
                           Paths.get(caminhoDestino),
                           StandardCopyOption.REPLACE_EXISTING);
                produto.setCaminhoImagem(caminhoDestino);
                System.out.println("Imagem atualizada com sucesso!");
            } catch (IOException e) {
                System.out.println("Erro ao copiar a imagem: " + e.getMessage());
            }
        }

        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
        System.out.println("Produto atualizado com sucesso!");
    }

    static void excluirProduto() {
        System.out.print("Código do produto a excluir: ");
        String codigo = scanner.nextLine();

        if (produtos.remove(codigo) != null) {
            System.out.println("Produto excluído com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    static void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println("\n=== Lista de Produtos ===");
        for (Produto p : produtos.values()) {
            System.out.println(p);
        }
    }

    static void venderProduto() {
        System.out.print("Código do produto: ");
        String codigo = scanner.nextLine();

        Produto produto = produtos.get(codigo);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Quantidade a vender: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        if (quantidade <= 0) {
            System.out.println("Quantidade inválida.");
        } else if (quantidade > produto.getEstoque()) {
            System.out.println("Estoque insuficiente.");
        } else {
            double total = quantidade * produto.getPreco();
            produto.setEstoque(produto.getEstoque() - quantidade);
            System.out.printf("Venda realizada com sucesso! Total: R$ %.2f\n", total);
        }
    }

    static class Produto {
        private String codigo;
        private String nome;
        private double preco;
        private int estoque;
        private String caminhoImagem;

        public Produto(String codigo, String nome, double preco, int estoque, String caminhoImagem) {
            this.codigo = codigo;
            this.nome = nome;
            this.preco = preco;
            this.estoque = estoque;
            this.caminhoImagem = caminhoImagem;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getNome() {
            return nome;
        }

        public double getPreco() {
            return preco;
        }

        public int getEstoque() {
            return estoque;
        }

        public String getCaminhoImagem() {
            return caminhoImagem;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public void setPreco(double preco) {
            this.preco = preco;
        }

        public void setEstoque(int estoque) {
            this.estoque = estoque;
        }

        public void setCaminhoImagem(String caminhoImagem) {
            this.caminhoImagem = caminhoImagem;
        }

        @Override
        public String toString() {
            return String.format("Código: %s | Nome: %s | Preço: R$ %.2f | Estoque: %d | Imagem: %s",
                    codigo, nome, preco, estoque, caminhoImagem);
        }
    }
}
