## Protocolo do sistema

A comunicação será realizada por meio de sockets TCP/IP seguindo o seguinte protocolo:

## Servidor

### Recebe

- *byte* de sinalização `-128`
- *int* **n** com o tamanho da lista
- **n** *ints* a serem ordenados

### Envia

- *byte* de sinalização `127`
- *int* **n** com o tamanho da lista
- **n** *ints* ordenados