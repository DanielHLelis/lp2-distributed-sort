# Ordenação paralela e distribuída em Java

Este repositório consiste em uma atividade acadêmica da disciplina de LP2, tendo
como objetivo implementar um sistema de ordenação paralela em Java utilizando
técnicas de Threads e sistemas distribuídos.

## Como executar

Utilizar o `gradle` wrapper embutido no projeto para realizar as operações básicas.

## Estrutura do projeto

O projeto atualmente é dividido em dois sub-projetos: `sorter`, responsável
pelos algoritmos de ordenação paralela e IO dos arquivos `data.in` e `data.out`.

### Comandos úteis

_Obs.: no **Windows**, substituir `./gradlew` por `.\gradlew.bat`_

- Compilar
    - `./gradlew build`
- Executar testes
    - `./gradlew test`
- Executar a CLI do `sorter`
    - `./gradlew sorter:run --args="-i data.in -o data.out -a batch"`
        - `-i`: arquivo de entrada (listas de inteiros separados por 
          quebras de linhas, múltiplas listas devem ser separadas por uma 
          linha em branco)
        - `-o`: arquivo de saída seguindo o mesmo formato da entrada
        - `-a`: algoritmo a ser usado
            - `batch`: inicia múltiplos ordenadores seriais em paralelo para cada lista
            - `parallel`: inicia um ordenador paralelo para cada lista, uma de cada vez
            - `serial`: inicia um ordenador serial para cada lista, uma de cada vez

## Autoria

- Daniel H. Lelis
- Ana Luisa
