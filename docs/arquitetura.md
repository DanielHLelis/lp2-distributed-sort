## Arquitetura

O sistema seguirá a arquitetura *n* clientes, *n* servidores.

### Cliente

O cliente receberá alguns parâmetros por lista de comando:

- O arquivo de entrada
- O arquivo de saída
- Lista de trabalhadores disponíveis
    - Formato:
    
  ```
  host1:porta
  host2:porta
  host3:porta
  ```
  
O cliente irá distribuir as sub-listas entre os servidores a medida que os servi
dores ficarem disponíveis. Caso seja possível estabelecer mais de uma conexão
simultânea com um host, basta repeti-lo **n** vezes na lista de trabalhadores. 

### Servidor

Os servidores utilizaram um algoritmo de ordenação *single thread* para cada
lista recebida. Aceitando até *n* conexões simultâneas.

O servidor receberá os seguintes parâmetros:

- Host
- Porta
- Número de processos

