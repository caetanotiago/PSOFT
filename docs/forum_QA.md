# QA Forum — Conversation Log

> **Project:** AISafe Flight Management System
> **Client:** Nuno Pereira NAP
> **Last updated:** 2026-04-27

---

## Index

| # | Date | Topic | Status | Owner |
|---|------|-------|--------|-------|
| [001](#conversation-001) | 2026-03-25 | Impacto do estado operacional dos Aeroportos nas Rotas | Resolved | TIVANE 1231948 |
| [002](#conversation-002) | 2026-03-25 | WP#3A - Flight Routes | Resolved | TIVANE 1231948 |
| [003](#conversation-003) | 2026-03-28 | US205 - State Transitions | Resolved | REMELHE 1241847 |
| [004](#conversation-004) | 2026-03-28 | US101 & 203 - Aircraft Seating Capacity | Resolved | REMELHE 1241847 |
| [005](#conversation-005) | 2026-03-28 | US226 - Maintenance Part attributes | Resolved | Diogo Nogueira 1241692 |
| [006](#conversation-006) | 2026-03-28 | US212 - Scheduled flight states | Resolved | Diogo Nogueira 1241692 |
| [007](#conversation-007) | 2026-03-28 | US113 - Route ID requirements | Resolved | Diogo Nogueira 1241692 |
| [008](#conversation-008) | 2026-03-28 | US207 - Detailed facilities information structure | Resolved | Diogo Nogueira 1241692 |
| [009](#conversation-009) | 2026-03-28 | Aircraft - Clarification on "Aircraft Type" and features | Resolved | Diogo Nogueira 1241692 |
| [010](#conversation-010) | 2026-03-28 | General - Clarification on "Fleet" and "Network" concepts | Resolved | Diogo Nogueira 1241692 |
| [011](#conversation-011) | 2026-04-01 | US211 - View Airports Grouped by Region or Country | Resolved | SANTOS 1241008 |
| [012](#conversation-012) | 2026-04-01 | Routes - Existência de Escalas | Resolved | SANTOS 1241008 |
| [013](#conversation-013) | 2026-04-01 | US216 - Search for alternative routes between airports | Resolved | SILVA 1241131 |
| [014](#conversation-014) | 2026-04-01 | US208 - Update Airport details and contact information | Resolved | SILVA 1241131 |
| [015](#conversation-015) | 2026-04-01 | US226 - Maintenance Part relationships | Resolved | Diogo Nogueira 1241692 |
| [016](#conversation-016) | 2026-04-08 | US222 - Scheduled maintenance triggers and intervals | Resolved | Diogo Nogueira 1241692 |
| [017](#conversation-017) | 2026-04-26 | US101 - Aircraft manufacturer details | Resolved | Diogo Nogueira 1241692 |

---

## Conversation 001

**Date:** 2026-03-25
**Topic:** Impacto do estado operacional dos Aeroportos nas Rotas
**Status:** `Resolved`
**Owner:** TIVANE 1231948
**Tags:** `#routes` `#airports` `#domain-model`

### Question

> O enunciado refere que as rotas só podem ser criadas entre aeroportos registados e que os aeroportos têm um estado operacional (ex: fechado, em manutenção). No Modelo de Domínio, uma ROUTE deve ser invalidada automaticamente se um dos seus aeroportos (origem ou destino) mudar para o estado "fechado"? Adicionalmente, a distância de uma rota é um valor fixo definido no momento da criação ou deve ser calculada dinamicamente com base nas coordenadas dos aeroportos?

### Client Response

> Redireccionado para: https://moodle.isep.ipp.pt/mod/forum/discuss.php?d=4090

**Received on:** 2026-03-25
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- A resposta foi redireccionada para outra thread. Ver Conversation 002 (WP#3A - Flight Routes) para a resposta completa.
- Este tópico está bloqueado — já não é possível responder.

### Action Items

- [x] Consultar thread referenciada (Conversation 002)

---

## Conversation 002

**Date:** 2026-03-25
**Topic:** WP#3A - Flight Routes
**Status:** `Resolved`
**Owner:** TIVANE 1231948
**Tags:** `#routes` `#airports` `#distance`

### Question

> O enunciado refere que as rotas só podem ser criadas entre aeroportos registados e que os aeroportos têm um estado operacional (ex: fechado, em manutenção). No Modelo de Domínio, uma ROUTE deve ser invalidada automaticamente se um dos seus aeroportos (origem ou destino) mudar para o estado "fechado"? Adicionalmente, a distância de uma rota é um valor fixo definido no momento da criação ou deve ser calculada dinamicamente com base nas coordenadas dos aeroportos?

### Client Response

> As rotas existem independentemente do estado operacional do aeroporto. Por exemplo: a rota LIS-OPO continua a existir mesmo que o aeroporto do Porto esteja temporariamente fechado para manutenção. No entanto, os voos agendados nessa rota poderiam ser afetados.
>
> Quanto à distância, deve ser um valor fixo calculado no momento da criação da rota. Creio que podemos assumir que os aeroportos não mudam de lugar.

**Received on:** 2026-03-26
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- Rotas **não** são invalidadas quando um aeroporto fecha — continuam a existir no sistema.
- Voos agendados nessa rota **podem** ser afetados pelo estado do aeroporto.
- Distância da rota = valor fixo calculado no momento da criação (não recalculado dinamicamente).

### Action Items

- [x] Confirmar no domain model que Route não tem dependência de estado do aeroporto
- [x] Garantir que o campo `distance` é calculado e persistido na criação da rota

---

## Conversation 003

**Date:** 2026-03-28
**Topic:** US205 - State Transitions
**Status:** `Resolved`
**Owner:** REMELHE 1241847
**Tags:** `#aircraft` `#state` `#US205`

### Question

> A US205 pede para ver o estado da aeronave em tempo real, incluindo "in-flight". No entanto, não há nenhuma User Story explícita para "iniciar voo" ou "concluir voo".
>
> Assim, para a US205, o estado da aeronave pode ser "in-flight". Como é que a aeronave transita para este estado e regressa a "available"? Existe alguma US implícita para o registo da partida e chegada de um Scheduled Flight, ou o estado muda automaticamente com base na data/hora do agendamento (US212)?

### Client Response

> Essas transições serão feitas manualmente e implementadas numa iteração futura do sistema.

**Received on:** 2026-03-28
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- As transições de estado da aeronave são **manuais**.
- A implementação das transições está prevista para uma **iteração futura** — não implementar agora.

### Action Items

- [x] Não implementar transições automáticas de estado nesta iteração

---

## Conversation 004

**Date:** 2026-03-28
**Topic:** US101 & 203 - Aircraft Seating Capacity
**Status:** `Resolved`
**Owner:** REMELHE 1241847
**Tags:** `#aircraft` `#seating-capacity` `#US101` `#US203`

### Question

> A US101 refere que o Aircraft Model tem uma "seating capacity". Mas a US203 diz: "The same airplane model can have different seat configuration, and therefore different capacities."
>
> Neste sentido, relativamente à capacidade de passageiros, se diferentes aviões do mesmo modelo podem ter capacidades diferentes (US203), a capacidade de lugares deve ser registada ao nível da Instância da Aeronave (Aircraft) em vez de (ou em adição a) ser registada no Modelo da Aeronave (US101)? Ou devemos criar um Modelo de Aeronave diferente para cada configuração de lugares?

### Client Response

> Aviões, mesmo sendo do mesmo modelo, podem ter capacidades diferentes.

**Received on:** 2026-03-28
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- A `seatingCapacity` deve ser atributo da **instância** da aeronave (Aircraft), não do AircraftModel.
- Não é necessário criar modelos distintos para cada configuração de lugares.

### Action Items

- [x] Adicionar `seatingCapacity` à entidade Aircraft (instância), não apenas ao AircraftModel

---

## Conversation 005

**Date:** 2026-03-28
**Topic:** US226 - Maintenance Part attributes
**Status:** `Resolved`
**Owner:** Diogo Nogueira 1241692
**Tags:** `#maintenance` `#domain-model` `#US226`

### Question

> For US226, the Maintenance Supervisor needs to track maintenance parts inventory. Since the specific attributes of a part aren't detailed in the requirements, is it safe to assume a domain model containing attributes like `modelNumber`, `stockQuantity`, `name`, and `description`?

### Client Response

> Os atributos sugeridos (partNumber ?, stockQuantity, name, description) são razoáveis como ponto de partida.

**Received on:** 2026-03-28
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- Cliente confirmou os atributos como razoáveis.
- Cliente usou `partNumber` em vez de `modelNumber` — preferir `partNumber` no modelo.

### Action Items

- [x] Usar `partNumber` (não `modelNumber`) como atributo de MaintenancePart

---

## Conversation 006

**Date:** 2026-03-28
**Topic:** US212 - Scheduled flight states
**Status:** `Resolved`
**Owner:** Diogo Nogueira 1241692
**Tags:** `#flights` `#scheduled-flight` `#state` `#US212`

### Question

> In US212, we are required to assign an aircraft to a route for a specific date and time to create a "scheduled flight".
>
> Should this scheduled flight entity maintain a status lifecycle throughout its existence (e.g., "scheduled", "delayed", "in-flight", "completed", "canceled"), or does it only represent the planning intent?

### Client Response

> Parece adequado. Estas mudanças de estado vão fazer parte de funcionalidade futura do sistema.

**Received on:** 2026-03-28
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- O ciclo de vida de estados é aprovado: scheduled, delayed, in-flight, completed, canceled.
- A implementação das mudanças de estado é para **iteração futura**.

### Action Items

- [x] Modelar enum de estados em ScheduledFlight sem implementar transições automáticas nesta fase

---

## Conversation 007

**Date:** 2026-03-28
**Topic:** US113 - Route ID requirements
**Status:** `Resolved`
**Owner:** Diogo Nogueira 1241692
**Tags:** `#routes` `#identifier` `#US113`

### Question

> For US113, the ATCC needs to view the details of a route given its "ID".
>
> Does this Route ID follow any specific business rules or formats (e.g., a standard flight number format like "TP123"), or can it be a standard system-generated numeric/UUID identifier?

### Client Response

> Um identificador gerado pelo sistema é adequado.

**Received on:** 2026-03-28
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- Route ID = identificador numérico/UUID gerado pelo sistema.
- Não há formato de negócio específico a seguir.

### Action Items

- [x] Usar ID gerado pelo sistema para Route (sem formato específico de negócio)

---

## Conversation 008

**Date:** 2026-03-28
**Topic:** US207 - Detailed facilities information structure
**Status:** `Resolved`
**Owner:** Diogo Nogueira 1241692
**Tags:** `#airports` `#facilities` `#US207`

### Question

> Regarding US207, the Backoffice Operator needs to register an airport with "detailed facilities information (terminals, gates, services)".
>
> Should this information be modeled as structured data (e.g., specific entities/Value Objects for Terminals, Gates, and Services), or is it sufficient to store this as a general text/description field for the airport?

### Client Response

> Por enquanto, não me parece haver nenhuma funcionalidade planeada que necessite desta informação. No entanto, modelar como dados estruturados parece mais coerente com o nível de detalhe solicitado.

**Received on:** 2026-03-28
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- Não há funcionalidades que consumam estes dados diretamente, mas o cliente prefere dados estruturados.
- Modelar Terminals, Gates e Services como entidades/Value Objects estruturados.

### Action Items

- [x] Modelar facilities como dados estruturados (não como texto livre)

---

## Conversation 009

**Date:** 2026-03-28
**Topic:** Aircraft - Clarification on "Aircraft Type" and features
**Status:** `Resolved`
**Owner:** Diogo Nogueira 1241692
**Tags:** `#aircraft` `#features` `#US221` `#US224`

### Question

> 1. In US221, the system mentions "aircraft type". Is this concept the same as an "Aircraft Model" (from US101), or is it a broader categorization (e.g., jet, turboprop, narrow-body, wide-body)? If it's a separate concept, what types exist in the system?
> 2. In US224, users can search by specific features (e.g., WiFi-enabled, specific engine type). Does "engine type" count as a standard feature, and if so, is there a predefined list of engine types we should model, or is this just free text?

### Client Response

> In US221, "aircraft type" is the same as an "Aircraft Model". Features could be a list of features, freely added by the user.

**Received on:** 2026-03-28
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- "Aircraft type" em US221 = AircraftModel (sem conceito separado).
- Features são uma lista livre — sem enum predefinido, o utilizador adiciona livremente.
- Engine type não é modelado separadamente; é apenas mais uma feature de texto livre.

### Action Items

- [x] Não criar entidade separada para "aircraft type" — mapear para AircraftModel
- [x] Implementar features como lista de strings sem valores predefinidos

---

## Conversation 010

**Date:** 2026-03-28
**Topic:** General - Clarification on "Fleet" and "Network" concepts
**Status:** `Resolved`
**Owner:** Diogo Nogueira 1241692
**Tags:** `#domain` `#fleet` `#network` `#US206` `#US215` `#US219`

### Question

> 1. In US206 and US219, the term "Fleet" is mentioned. In the context of AISafe, does a "Fleet" represent all the aircraft owned by a single Air Transport Company, or is it a specific grouping of aircraft?
> 2. In US215, the term "Network" is used. Is "Network" synonymous with the company's "Fleet", or does it specifically refer to the graph/collection of active routes operated by the company?

### Client Response

> A fleet is a set of all airplanes managed by an ATC. A network is the set of active routes.

**Received on:** 2026-03-28
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- **Fleet** = conjunto de todos os aviões geridos por uma Air Transport Company (ATC).
- **Network** = conjunto das rotas ativas — conceito distinto de Fleet.

### Action Items

- [x] Confirmar que o domain model distingue Fleet (aeronaves) de Network (rotas ativas)

---

## Conversation 011

**Date:** 2026-04-01
**Topic:** US211 - View Airports Grouped by Region or Country
**Status:** `Resolved`
**Owner:** SANTOS 1241008
**Tags:** `#airports` `#region` `#US211`

### Question

> A minha dúvida na US211 é sobre o significado do termo "region". Na caracterização do aeroporto são determinados o país e a cidade. "Region", para o contexto desta US, tem o mesmo significado de cidade? Ou é um parâmetro diferente, mais abrangente em termos geográficos?

### Client Response

> "Region" refere-se a uma zona geográfica mais abrangente que o país (Europa, América do Norte, Sudeste Asiático, etc.).

**Received on:** 2026-04-01
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- "Region" é um conceito **supranacional** (ex: Europa, América do Norte, Sudeste Asiático).
- Distinto de país e cidade — mais abrangente do que ambos.

### Action Items

- [x] Adicionar campo `region` ao Airport com valores supranacionais

---

## Conversation 012

**Date:** 2026-04-01
**Topic:** Routes - Existência de Escalas
**Status:** `Resolved`
**Owner:** SANTOS 1241008
**Tags:** `#routes` `#escalas` `#US110`

### Question

> No contexto das rotas, podemos considerar que uma rota pode ter uma ou várias escalas, ou consideramos as "escalas" como rotas independentes?

### Client Response

> Bom dia. Uma rota é sempre entre um aeroporto de origem e um de destino, conforme definido na US110.

**Received on:** 2026-04-01
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- Rota = sempre ponto a ponto (origin → destination), sem escalas intermédias.
- Itinerários com escalas são representados como múltiplas rotas independentes encadeadas.

### Action Items

- [x] Confirmar que Route tem apenas origin e destination (sem lista de waypoints/escalas)

---

## Conversation 013

**Date:** 2026-04-01
**Topic:** US216 - Search for alternative routes between airports
**Status:** `Resolved`
**Owner:** SILVA 1241131
**Tags:** `#routes` `#search` `#algorithm` `#US216`

### Question

> A US216 fala em procurar por rotas alternativas entre dois aeroportos. A questão é a seguinte: Quais seriam os possíveis critérios para a procura de rotas alternativas? Poderiam ser, por exemplo, custo monetário, distância, tempo de voo, número de escalas, etc.?

### Client Response

> Rotas alternativas referem-se a combinações de rotas existentes que ligam os dois aeroportos via uma ou mais escalas. De facto, podem existir diferentes abordagens para encontrar estas combinações (por exemplo, privilegiar o menor número de escalas, o menor tempo total, ou a menor distância).
>
> O sistema deve ser desenhado de modo que seja fácil adicionar ou substituir algoritmos para determinar as combinações, sem alterar o restante da lógica. Para efeito da implementação futura, basta implementar um algoritmo para encontrar estas combinações (por exemplo, o menor número de escalas).

**Received on:** 2026-04-01
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- Rotas alternativas = combinações de rotas existentes com 1 ou mais escalas intermédias.
- O sistema deve usar um padrão que permita trocar algoritmos facilmente (Strategy Pattern recomendado).
- Para esta iteração, implementar apenas **um** algoritmo (ex: menor número de escalas).

### Action Items

- [x] Implementar Strategy Pattern para algoritmos de pesquisa de rotas alternativas
- [x] Implementar algoritmo inicial: menor número de escalas

---

## Conversation 014

**Date:** 2026-04-01
**Topic:** US208 - Update Airport details and contact information
**Status:** `Resolved`
**Owner:** SILVA 1241131
**Tags:** `#airports` `#contact` `#US208`

### Question

> A US208 fala em atualizar os detalhes de um aeroporto incluindo horas em que estão operacionais e informações de contacto. Na nossa perspetiva, pensamos em informações de contacto como número de telefone do apoio ao cliente fornecido pelo aeroporto. A questão é: O cliente quererá ter a opção de ter mais que um número de telemóvel por aeroporto, ou eventualmente haver mais meios de contacto?

### Client Response

> Sim, um aeroporto pode ter múltiplos contactos. Cada contacto deve ter um tipo (telefone, email, fax, etc.), o valor e uma descrição ou um departamento associado (opcional).

**Received on:** 2026-04-01
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- Um aeroporto tem uma **lista de contactos** (1..*).
- Cada contacto: `type` (telefone, email, fax, etc.), `value`, `description`/`department` (opcional).

### Action Items

- [x] Modelar Contact com campos: type, value, description (opcional)
- [x] Airport tem coleção de Contact (1..*)

---

## Conversation 015

**Date:** 2026-04-01
**Topic:** US226 - Maintenance Part relationships
**Status:** `Resolved`
**Owner:** Diogo Nogueira 1241692
**Tags:** `#maintenance` `#domain-model` `#US226`

### Question

> Following up on a previous clarification regarding US226, we have a follow-up question regarding its relationships.
>
> Currently, our domain model has `MaintenancePart` as an isolated entity. Should `MaintenancePart` be related to other entities in the system? For example:
> 1. Should it be linked to a `MaintenanceRecord` to track which parts were actually consumed during a maintenance task (and thus automatically deduct from the stock)?
> 2. Should it be linked to an `AircraftModel` to define which parts are compatible with which models?
>
> Or should it remain a standalone inventory catalog for this phase of the project?

*Nota: Questão reformulada após cliente desconhecer o termo "Domain Model".*

> 1. When a mechanic performs maintenance on an airplane, do they need to log exactly which parts were used so the system can automatically deduct them from the inventory? Or is the inventory updated manually by a supervisor later?
> 2. Are these maintenance parts universal, or do we need the system to restrict certain parts so they can only be used on specific airplane models?

### Client Response

> 1. For this version of the system, we want to keep it simple. The inventory is managed directly by the Maintenance Supervisor (e.g., he manually edits part quantities). The system simply needs to alert when stock falls below a defined minimum threshold.
> 2. For this version of the system, we will assume there is no need to track restrictions regarding parts and specific aircraft models.

**Received on:** 2026-04-01
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- `MaintenancePart` mantém-se como **entidade standalone** nesta fase.
- Inventário gerido **manualmente** pelo Maintenance Supervisor (sem dedução automática).
- Sistema alerta quando `stockQuantity` cai abaixo de `minimumThreshold`.
- Sem relação com `AircraftModel` nesta versão.

### Action Items

- [x] Confirmar que MaintenancePart não tem relações com MaintenanceRecord ou AircraftModel
- [x] Implementar alerta quando stockQuantity < minimumThreshold

---

## Conversation 016

**Date:** 2026-04-08
**Topic:** US222 - Scheduled maintenance triggers and intervals
**Status:** `Resolved`
**Owner:** Diogo Nogueira 1241692
**Tags:** `#maintenance` `#scheduling` `#US222`

### Question

> Regarding US222, the system needs to trigger alerts when an aircraft is due for scheduled maintenance based on flight hours or calendar days.
>
> To calculate when an aircraft is "due", where should the system look for the defined intervals/limits (e.g., "requires maintenance every 500 hours or 180 days")?
> 1. Should these intervals be attributes of a Maintenance Template (meaning a specific template is triggered periodically)?
> 2. Should these intervals be defined directly on the Aircraft Model?
> 3. Or is there another way the maintenance schedule is defined?

*Nota: Follow-up enviado a 2026-04-10 para clarificar o significado de "triggered periodically":*

> By "triggered periodically", I simply meant that the system will look at the limits set in a specific template (for example, a "100-hour inspection" template set to 100 flight hours) and automatically send an alert to the team every time an aircraft reaches that specific limit. We will go ahead and add the maintenance intervals (flight hours and calendar days) to the Maintenance template as you suggested.

### Client Response

> Seems like a good idea to have the interval defined as part of the Maintenance Template. Unclear to me what you mean with "(meaning a specific template is triggered periodically)".

**Received on:** 2026-04-10
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- Os intervalos de manutenção (horas de voo e dias de calendário) são atributos da **Maintenance Template**.
- O sistema verifica os limites do template e envia alertas quando uma aeronave os atinge.
- Exemplo: template "100-hour inspection" com limite de 100 horas de voo → alerta automático.

### Action Items

- [x] Adicionar `flightHoursInterval` e `calendarDaysInterval` à entidade MaintenanceTemplate
- [x] Implementar lógica de verificação e alerta de manutenção com base nesses campos

---

## Conversation 017

**Date:** 2026-04-26
**Topic:** US101 - Aircraft manufacturer details
**Status:** `Resolved`
**Owner:** Diogo Nogueira 1241692
**Tags:** `#aircraft` `#manufacturer` `#US101`

### Question

> Regarding the registration of an aircraft model (US101), we need to specify its manufacturer.
>
> Could you clarify how these manufacturers should be handled?
>
> 1. Are the manufacturers just simple text names that the Backoffice Operator types in when creating the model?
> 2. Does AISafe need to maintain a separate, controlled list of manufacturers in the system, storing extra details about them (such as contact information, origin, or a unique identifier) before an aircraft model can be linked to them?
> 3. Are there specific pre-defined manufacturers that exist in this system, or is this list completely open-ended and created on the fly?

### Client Response

> We will not manage different manufacturers in the application. Manufacturers should come from a fixed list, either by configuration or in each version of the application.

**Received on:** 2026-04-27
**Responded by:** Nuno Pereira NAP

### Notes & Clarifications

- Manufacturers are **not** a managed entity in the system — no CRUD for manufacturers.
- The manufacturer list is **fixed**, defined by configuration or hardcoded per application version.
- AircraftModel references a manufacturer from this fixed list (no separate Manufacturer entity needed).

### Action Items

- [x] Define manufacturer as a fixed enum/config list, not a managed entity
- [x] AircraftModel references manufacturer from the fixed list

---

<!-- 
  HOW TO ADD A NEW CONVERSATION
  1. Add a new row to the Index table above.
  2. Copy the block below and paste at the bottom of the file.
  3. Increment the number and fill in the fields.

## Conversation XXX

**Date:** YYYY-MM-DD  
**Topic:**   
**Status:** `Open`  
**Owner:**   
**Tags:**  

Question

>  

Client Response

>  

**Received on:**  
**Responded by:**  

Notes & Clarifications

- 

Action Items

- [ ] 

---
-->