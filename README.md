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



## Próximos passos
- Atualize este README com screenshot da interface.
- Adicione uma licença (ex.: MIT) se for publicar como open source.
- Configure GitHub Actions para builds automáticos (opcional).
