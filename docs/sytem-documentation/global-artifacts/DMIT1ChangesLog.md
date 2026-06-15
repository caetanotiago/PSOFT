# **DM IT1 CHANGES — Phase 2 / WP#3B (Enhanced Route Management, Flight Operations)**


1. **Confirmação do agregado Flight Route & Schedule:** As US212/US213 consolidam o uso do `ScheduledFlight` como Aggregate Root do *Flight Route & Schedule Aggregate* (já introduzido no IT0 por feedback do docente). O `ScheduledFlight` referencia o `Aircraft` (por identidade) e o `FlightRoute`, e embebe os Value Objects `FlightSchedule` (data/hora) e `FlightStatus` (default `Scheduled`). Não foram acrescentados atributos novos a estas classes.

2. **Route Popularity / Usage Count (US214):** Introduziu-se o conceito de *popularidade de rota* como **métrica derivada** — o número de `ScheduledFlight` por `FlightRoute`. Por ser um valor calculado (não persistido), **não** foi modelado como atributo de `FlightRoute`; foi documentado no DM através de uma `note` no agregado e adicionado ao glossário (**Route Popularity / Usage Count**) para manter a sincronização DM↔Glossário.

3. **Alternative Route / Itinerary (US216):** Introduziu-se o conceito de *rota alternativa (itinerário)* como **resultado de consulta derivado** — uma cadeia de `FlightRoute` ativas que liga dois aeroportos através de uma ou mais escalas. Não é uma entidade persistida; foi documentado no DM com uma `note` e adicionado ao glossário (**Alternative Route (Itinerary)**).

4. **Network = rotas ativas (US214/US215):** Clarificou-se (alinhado com o termo já existente **Network** no glossário e com a Conversation 010 do fórum) que tanto a listagem de rotas ativas (US214) como o cálculo de distância total da rede (US215) consideram **apenas** as `FlightRoute` com `RouteStatus` ativo.

5. **Correção do glossário:** O `glossary.md` continha a tabela de termos **duplicada** (todo o conteúdo aparecia duas vezes). A duplicação foi removida e o ficheiro reescrito como uma única tabela ordenada alfabeticamente, já incluindo os dois novos termos acima.
