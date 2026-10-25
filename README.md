# Meteor Run

Jogo 2D desenvolvido em **Java/Swing**, com motor próprio baseado em `GameObject`, transformações geométricas e sistema de colisões com colisores poligonais e circulares.

## Funcionalidades

- Single-player com inimigos, boss, score e ecrãs de vitória/game over
- Multiplayer local para dois jogadores
- Power-ups de velocidade, cadência de tiro e shield
- Motor de jogo próprio
- Colisores poligonais e circulares
- Sprites, música e efeitos sonoros
- Controlos por teclado para os dois jogadores

## Tecnologias

Java · Swing/AWT · Programação Orientada a Objetos · Geometria computacional · Deteção de colisões

## Estrutura publicada

A versão completa e reproduzível do jogo está publicada na branch `develop`. Inclui:

- `files/` — código-fonte Java do jogo
- `assets/meteor-resources.zip` — imagens e áudio usados no executável
- `build.bat` — compila o código, inclui os recursos e gera `MeteorRun.jar`
- `run.bat` — gera o JAR se necessário e inicia o jogo

A branch `main` mantém uma versão mais pequena do repositório; para compilar e executar o jogo com os recursos finais, usa `develop`.

## Compilar no Windows

É necessário ter um JDK instalado e disponível no `PATH` (`java`, `javac` e `jar`), além do PowerShell usado pelo script para extrair os recursos.

```bat
git checkout develop
build.bat
```

O script compila `files\*.java`, extrai `assets\meteor-resources.zip` para o diretório de build e cria `MeteorRun.jar` com a classe principal `files.Main`.

## Executar

Depois da compilação:

```bat
java -jar MeteorRun.jar
```

ou, para compilar automaticamente quando o JAR ainda não existir:

```bat
run.bat
```

## Validação

Uma compilação concluída sem erros e a abertura do jogo através de `java -jar MeteorRun.jar` ou `run.bat` validam que o código e os recursos foram empacotados corretamente.

## Controlos

**Player 1:** WASD + Space  
**Player 2:** setas + Shift

## Contexto

Projeto académico de Programação Orientada a Objetos, desenvolvido na Universidade do Algarve.
