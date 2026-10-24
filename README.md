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

## Estrutura

- `files/` — código-fonte Java do jogo
- `assets/meteor-resources.zip` — imagens e áudio usados no executável
- `build.bat` — compila o código, inclui os recursos e gera `MeteorRun.jar`
- `run.bat` — gera o JAR se necessário e inicia o jogo

## Build no Windows

É necessário ter um JDK instalado e disponível no `PATH` (`java`, `javac` e `jar`). Depois:

```bat
build.bat
```

O resultado é `MeteorRun.jar`. Para executar:

```bat
java -jar MeteorRun.jar
```

ou simplesmente:

```bat
run.bat
```

## Controlos

**Player 1:** WASD + Space  
**Player 2:** setas + Shift

## Contexto

Projeto académico de Programação Orientada a Objetos, desenvolvido na Universidade do Algarve.
