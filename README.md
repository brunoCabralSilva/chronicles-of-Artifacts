# Chronicles of Artifacts - Gerenciamento de Banco de Dados de RPG

Seja bem-vindo ao Chronicles of Artifacts, uma aplicação Java poderosa que permite administrar um mundo de aventuras e equipamentos para jogos de RPG. Este projeto oferece uma solução abrangente para gerenciar classes de personagens, armas, armaduras e muito mais. Ele foi criado para demonstrar minhas habilidades em desenvolvimento de software e modelagem de banco de dados.

## Características Destacadas

- **Classes de Personagens:** Explore uma ampla variedade de classes, desde guerreiros destemidos até feiticeiros misteriosos. Cada classe possui sua função única no mundo do RPG.
- **Armas e Armaduras:** Equipe seus personagens com uma vasta seleção de armas e armaduras. Crie combinações personalizadas para otimizar o estilo de luta de cada personagem.
- **Propriedades de Armas:** Personalize suas armas com propriedades únicas, como alcance estendido, versatilidade e mais, para criar estratégias de combate diversificadas.
- **Artefatos Mágicos:** Descubra armas e armaduras lendárias com habilidades especiais. Esses artefatos podem mudar o curso das batalhas e adicionar um toque épico à sua jornada.

## Como Utilizar

1. **Configuração:** Clone este repositório e configure o banco de dados de acordo com as instruções no arquivo `README.md`.
2. **Exploração:** Use a aplicação para explorar as classes, armas, armaduras e propriedades disponíveis. Personalize e crie combinações únicas para seus personagens.
3. **Artefatos:** Descubra os artefatos mágicos mais poderosos do mundo do RPG. Cada um deles traz consigo uma história fascinante e habilidades excepcionais.

## Tecnologias Demonstradas

- **Java:** O projeto é desenvolvido em Java, demonstrando minhas habilidades de programação orientada a objetos e manipulação de bancos de dados.
- **MySQL:** Utiliza o MySQL como banco de dados relacional para armazenar e gerenciar todas as informações do mundo do RPG.
- **Modelagem de Dados:** Apresenta uma modelagem de dados cuidadosa e bem planejada, destacando minha habilidade em projetar estruturas complexas de banco de dados.
- **Tratamento de Exceções:** Implementa um tratamento robusto de exceções para garantir a integridade dos dados e a estabilidade da aplicação.

Sinta-se à vontade para explorar o projeto, contribuir e usá-lo como inspiração para suas próprias aventuras no mundo dos jogos de RPG.

## Relacionamentos do Banco de Dados Chronicles of Artifacts

A seguir, estão os relacionamentos entre as tabelas do banco de dados Chronicles of Artifacts. Cada tabela descreve um aspecto importante do mundo de RPG que está sendo gerenciado pela aplicação.

<div align="center">
  <img align="center" src="./src/images/schema.png" alt= "Esquema do Banco de dados do projeto" />
</div>

1. **Tabela `classes`**: Armazena informações sobre as classes de personagens no jogo de RPG.

2. **Tabela `categoryArmors`**: Armazena informações sobre as categorias de armaduras.

3. **Tabela `categoryWeapons`**: Armazena informações sobre as categorias de armas.

4. **Tabela `properties`**: Armazena informações sobre as propriedades das armas.

5. **Tabela `weapons`**: Armazena informações sobre as armas disponíveis no jogo. Relaciona-se com a tabela `categoryWeapons`.

6. **Tabela `armors`**: Armazena informações sobre as armaduras disponíveis no jogo. Relaciona-se com a tabela `categoryArmors`.

7. **Tabela `weaponClasses`**: Relacionamento entre classes e categorias de armas. Associa classes com categorias de armas.

8. **Tabela `armorClasses`**: Relacionamento entre classes e categorias de armaduras. Associa classes com categorias de armaduras.

9. **Tabela `weaponProperties`**: Relacionamento entre armas e propriedades. Associa armas com propriedades específicas.

10. **Tabela `artifactWeapons`**: Armazena informações sobre armas mágicas (artefatos). Relaciona-se com a tabela `weapons`.

11. **Tabela `artifactArmors`**: Armazena informações sobre armaduras mágicas (artefatos). Relaciona-se com a tabela `armors`.

12. **Tabela `artifactItems`**: Armazena informações sobre itens mágicos (artefatos).

**Autor:** Bruno Gabryell Cabral da Silva
**Contato:** bruno.cabral.silva2018@gmail.com
