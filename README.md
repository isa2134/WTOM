# WTOM
Descrição do projeto.

## Equipe de Desenvolvimento
| Ordem | Nome                                 |
|:------|:-------------------------------------|
| 1     | CAIO FILLIPE SOUZA DA SILVA          |
| 2     | GABRIEL CEZAR DEOLINDO               |
| 3     | ISABELLA RODRIGUES OLIVEIRA DE SOUZA |
| 4     | LUÍS FILLIPE DIAS DE OLIVEIRA        |
| 5     | OTAVIO COTA GOMES QUINTAO            |
| 6     | PEDRO VIDAL SILVA                    |


## Atores do Sistema
| Ator          | Definição                                                                                                                         |
|:--------------|:----------------------------------------------------------------------------------------------------------------------------------|
| Aluno         | O aluno é o principal beneficiado pelo sistema. Ele faz parte da equipe TOM e participa de atividades ligadas às olimpíadas de matemática promovidas pela escola. Pode utilizar funções como: inscrever-se em olimpíadas; visualizar avisos e datas importantes; participar de fóruns de discussão; enviar respostas para desafios semanais; acessar conteúdos na biblioteca/videoteca; acompanhar seu desempenho.                 |---------------------------------------------------------------------------------------------------------------------------------------------------
| Professor     | O professor atua como orientador, mentor ou organizador das atividades olímpicas. Ele ajuda a planejar e executar as ações do TOM. Suas funções no sistema incluem: gerenciar olimpíadas; criar e corrigir desafios semanais; compartilhar materiais e conteúdos de estudo; moderar o fórum de discussão; acompanhar o desempenho dos alunos; agendar sessões de estudo ou encontros com a equipe.                
| Administrador | O administrador é responsável por manter o sistema funcionando e organizado. Pode ser um coordenador do projeto ou um membro da equipe com permissões técnicas e operacionais. Ele pode: cadastrar alunos e professores; gerenciar permissões de acesso; criar e editar avisos e lembretes; monitorar todas as atividades do sistema; manter a base de dados organizada e segura.                                                    |



## Requisitos Funcionais
| Id     | Ator(es)                 | Descrição                                                                    |
|:-------|:-------------------------|:-----------------------------------------------------------------------------|
| REQ001 | Administrador            | Cadastrar alunos, informando CPF, curso, e-mail, série e data de nascimento. |
| REQ002 | Administrador, Professor | Visualizar o perfil/dados de um aluno.                                       |
| REQ003 | Administrador            | Atualizar informações de um aluno.                                           |
| REQ004 | Administrador            | Possibilidade de excluir um aluno do sistema.                                |
| REQ005 | Administrador            | Realizar o cadastro de professores.                                          |
| REQ006 | Administrador            | Visualizar os dados de um professor.                                         |
| REQ007 | Administrador            | Atualizar o perfil/dados de um professor.                                    |
| REQ008 | Administrador            | Excluir um professor do sistema.                                             | 
| REQ009 | Administrador, Professor | Cadastrar uma nova olimpíada (nome, assunto, prazo de inscrição).            |
| REQ010 | Todos os atores          | Consultar detalhes das olimpíadas.                                           |
| REQ011 | Administrador, Professor | Editar dados de uma olimpíada.                                               |
| REQ012 | Administrador            | Excluir uma olimpíada cadastrada.                                            |
| REQ013 | Administrador, Aluno     | Realizar inscrição do aluno em uma olimpíada.                                |
| REQ014 | Todos os atores          | Visualizar inscrições feitas em uma olimpíada.                               |
| REQ015 | Administrador            | Editar dados de uma inscrição.                                               |
| REQ016 | Administrador, Aluno     | Cancelar uma inscrição em olimpíada.                                         |
| REQ017 | Administrador            | Registrar participação e prêmios de um aluno em olimpíadas.                  |
| REQ018 | Aluno, Professor         | Visualizar histórico de premiações.                                          |
| REQ019 | Administrador            | Corrigir dados de premiações.                                                |
| REQ020 | Administrador            | Remover registros incorretos de premiações.                                  |
| REQ021 | Administrador, Professor | Criar avisos para todos os usuários.                                         |
| REQ022 | Todos os atores          | Visualizar avisos disponíveis no sistema.                                    |
| REQ023 | Administrador, Professor | Editar um aviso existente.                                                   |
| REQ024 | Administrador            | Excluir um aviso existente.                                                  |
| REQ025 | Todos os atores          | Criar novo tópico de dúvida.                                                 |
| REQ026 | Todos os atores          | Ver dúvidas e respostas (restrição por aluno e professor).                   |
| REQ027 | Todos os atores          | Editar postagens próprias.                                                   |
| REQ028 | Todos os atores          | Excluir dúvidas postadas.                                                    |
| REQ029 | Administrador, Professor | Adicionar conteúdo didático.                                                 |
| REQ030 | Todos os atores          | Acessar conteúdo didático.                                                   |
| REQ031 | Administrador, Professor | Atualizar descrição/arquivo de conteúdo didático.                            |
| REQ032 | Administrador, Professor | Remover conteúdo publicado.                                                  |
| REQ033 | Administrador            | Criar conta de usuário.                                                      |
| REQ034 | Administrador            | Ver lista de usuários.                                                       |
| REQ035 | Administrador            | Alterar perfil de usuário.                                                   |
| REQ036 | Administrador            | Remover conta de usuário.                                                    |
| REQ037 | Administrador, Professor | Criar notícia de reunião online.                                             |
| REQ038 | Administrador, Professor | Alterar dados de uma reunião online.                                         |
| REQ039 | Todos os atores          | Visualizar notícia de reunião online.                                        | 
| REQ040 | Administrador, Professor | Excluir notícia de reunião online.                                           |
| REQ041 | Todos os atores          | Buscar olimpíadas com filtros por assunto.                                   |
| REQ042 | Todos os atores          | Receber notificações via e-mail.                                             |
| REQ043 | Todos os atores          | Recuperar/alterar senha via e-mail.                                          |
| REQ044 | Administrador            | Validar/autenticar usuário.                                                  |
| REQ045 | Administrador            | Gerar relatório de desempenho dos alunos em olimpíadas (PDF).                |
| REQ046 | --                       | Manter ranking dos alunos com base nas premiações.                           |
| REQ047 | Administrador, Professor | Cadastrar eventos no calendário.                                             |
| REQ048 | Todos os atores          | Visualizar eventos disponíveis no calendário.                                |
| REQ049 | Todos os atores          | Enviar feedback sobre aluno ou professor.                                    |
| REQ050 | Todos os atores          | Visualizar feedbacks próprios (ou gerais, se administrador).                 |
| REQ051 | Todos os atores          | Editar feedbacks próprios (ou gerais, se administrador).                     |
| REQ052 | Todos os atores          | Excluir feedbacks próprios (ou gerais, se administrador).                    |
| REQ053 | --                       | Gerir as permissões de cada usuário, ou seja, as funcionalidades que poderá acessar em cada tela. |
| REQ054 | --                       | Ao fazer login no sistema, os dados de login deverão ser validados e o usuário deverá ser redirecionado para sua respectiva página.|


## Regras de Negócio
| Id   | Regra de Negócio                                                                        | Descrição                                        |
|:---  |:----------------------------------------------------------------------------------------|:-------------------------------------------------|
| RN01 | Não será possível cadastrar outra conta com as mesmas informações                       | O sistema bloqueará cadastros com informações duplicadas. |
| RN02 | Apenas professores e administradores podem criar desafios, avisos e conteúdos didáticos | Somente usuários com esses perfis têm permissão   para tais ações. |
| RN03 | Inscrições apenas no período da olimpíada                                               | Não será possível se inscrever fora do intervalo definido. |
| RN04 | Apenas administradores podem excluir usuários                                           | Somente administradores têm permissão para remoção de contas. |
| RN05 | Premiações apenas ao fim da olimpíada                                                   | As premiações só poderão ser registradas após o término. |
| RN06 | Dúvidas apenas de aluno para professor                                                  | O sistema limita a comunicação de dúvidas entre alunos e professores. |
| RN07 | Olimpíadas distintas                                                                    | Não podem existir duas olimpíadas com os mesmos dados. |
| RN08 | Apenas professores/administradores podem gerir reuniões online                          | Apenas esses perfis podem cadastrar, alterar ou excluir reuniões. |
| RN09 | Avisos devem ter prazo de validade                                                      | Após o prazo, são arquivados automaticamente.    |
| RN10 | Notificações apenas para alunos                                                         | Somente alunos recebem notificações automáticas. |
| RN11 | Apenas administradores e professores controlam notificações                             | Somente esses perfis podem gerenciá-las.         |
| RN12 | Ranking exibirá apenas participantes da olimpíada                                       | Apenas alunos que participaram da competição serão listados. |
| RN13 | Permissão de visualização                                                               | Um usuário deverá apenas acessar funcionalidades/recursos que lhe cabem. |
| RN14 | Validação de dados                                                                      | Dados inseridos/utilizados no programa devem sempre passar por um processo de verificação e serem validados. |


## Casos de Uso
| Id    | Nome                                        | Requisitos                                     | Regras de Negócio |
|:------|:--------------------------------------------|:-----------------------------------------------|:------------------|
| CSU01 | Gestão de usuários                          | REQ001, REQ002, REQ003, REQ004, REQ005, REQ006, REQ007, REQ008, REQ033, REQ034, REQ035, REQ036 |RN01, RN04        |
| CSU02 | Gestão de login                             | REQ054                                         | RN14              |
| CSU03 | Gestão de olimpíadas                        | REQ009, REQ010, REQ011, REQ012                 | RN07              |
| CSU04 | Gestão de inscrições em olimpíadas          | REQ001, REQ010, REQ013, REQ014, REQ015, REQ016 | RN03              |
| CSU05 | Gestão de premiações                        | REQ002, REQ017, REQ018, REQ019, REQ020         | RN05              |
| CSU06 | Gestão de avisos                            | REQ002, REQ021, REQ022, REQ023, REQ024         | RN02, RN09        |
| CSU07 | Gestão de dúvidas                           | REQ002, REQ025, REQ026, REQ027, REQ028         | RN06              |
| CSU08 | Gestão de conteúdos didáticos               | REQ002, REQ029, REQ030, REQ031, REQ032         | RN02              |
| CSU09 | Gestão de desafios matemáticos              | REQ003                                         | RN02              |
| CSU10 | Gestão de permissão                         | REQ053                                         | RN13              |
| CSU11 | Gestão de reuniões online                   | REQ003, REQ037, REQ038, REQ039, REQ040         | RN08              |
| CSU12 | Gestão de notificações                      | REQ003                                         | RN10, RN11        |
| CSU13 | Filtro de pesquisa para olimpíadas          | REQ004, REQ041                                 | —                 |
| CSU14 | Gerar relatório de desempenho               | REQ004, REQ045                                 | RN05              |
| CSU15 | Gestão de ranking de alunos                 | REQ004, REQ046                                 | RN12              |
| CSU16 | Gestão de cronograma (eventos e olimpíadas) | REQ004, REQ047, REQ048                         | —                 |
| CSU17 | Redefinição de senha                        | REQ043                                         | —                 |
| CSU18 | Gestão de feedbacks                         | REQ049, REQ050, REQ051, REQ052                 | —                 |


## Planejamento
| Sprint | Caso de Uso | Desenvolvedor |
|:-------|:------------|:--------------|
| 1      | CSU01       |      2        |
| 1      | CSU02       |      5        |
| 1      | CSU03       |      1        |
| 1      | CSU10       |      3        |
| 1      | CSU08       |      4        |
| 1      | CSU12       |      6        |
| 2      | CSU04       |      1        |
| 2      | CSU05       |      2        |
| 2      | CSU06       |      6        |
| 2      | CSU07       |      5        |
| 2      | CSU09       |      3        |
| 2      | CSU11       |      4        |
| 3      | CSU13       |      5        |
| 3      | CSU14       |      1        |
| 3      | CSU15       |      6        |
| 3      | CSU16       |      3        |
| 3      | CSU17       |      2        |
| 3      | CSU18       |      4        |


