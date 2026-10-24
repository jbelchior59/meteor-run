# Meteor Run assets

Esta pasta deve conter `meteor-resources.zip`, com a estrutura:

```text
resources/
  Images/
  Sounds/
```

O `build.bat` extrai automaticamente este pacote para o diretório de build e gera `MeteorRun.jar` com todos os recursos embutidos.

O pacote usado para o executável contém as imagens finais do projeto e as músicas em WAV PCM otimizado para reduzir o tamanho mantendo compatibilidade com Java Sound.
