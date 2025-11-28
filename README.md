# Pokédex JavaFX

Aplicativo desktop que simula uma Pokédex usando JavaFX. A interface replica o visual clássico, exibe dados básicos de Pokémons e demonstra o uso de FXML, CSS customizado e chamadas HTTP/JSON em Java 23.

## Pré-requisitos
- Java 23 JDK (`java --version` para conferir)
- Maven 3.9+
- Git instalado e autenticado com o GitHub (HTTPS ou SSH)

## Rodando localmente
```powershell
# instalar dependências e iniciar a aplicação
mvn clean javafx:run
```

O Maven baixa o JavaFX e inicia o `MainApp` configurado no plugin `org.openjfx:javafx-maven-plugin`.

## Estrutura
- `src/main/java/br/unijui/pokedexjavafx` – classes Java (`MainApp`, `MainController`)
- `src/main/resources/view` – layout FXML principal
- `src/main/resources/style.css` – tema customizado
- `pom.xml` – dependências JavaFX e JSON

## Publicando no GitHub
1. Verifique o estado do repositório:
   ```powershell
   cd "C:\projeto quarkus\pokedex"
   git status -sb
   ```
2. Adicione e faça o commit inicial (caso ainda não exista):
   ```powershell
   git add .
   git commit -m "feat: bootstrap Pokedex JavaFX"
   ```
3. Crie um repositório vazio no GitHub (sem README) e copie a URL HTTPS ou SSH.
4. Configure o branch principal e o remoto:
   ```powershell
   git branch -M main
   git remote add origin <URL_DO_REPO>
   ```
5. Envie o código:
   ```powershell
   git push -u origin main
   ```

## Próximos passos
- Atualize este README com screenshot da interface.
- Adicione uma licença (ex.: MIT) se for publicar como open source.
- Configure GitHub Actions para builds automáticos (opcional).
