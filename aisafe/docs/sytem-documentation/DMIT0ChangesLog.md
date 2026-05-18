# **DM ITO CHANGES FROM TEACHER FEEDBACK**
1. **Faltava o runway no airport:** Reintroduziu-se a classe Runway (Pista) com os atributos de comprimento e orientação, associando-a diretamente como uma lista dentro da Entidade Raiz Airport.

2. **Agregar Aircraft Model e Aircraft / Manutenção com Root no Record / Fazer o mesmo para Rotas e Voos:**  
    - Juntou-se o AircraftModel e o Aircraft no Aircraft Aggregate, onde o Aircraft (o avião físico) é o Root.
    - Juntou-se o MaintenanceRecord e o MaintenanceTemplate no Maintenance Aggregate, onde o MaintenanceRecord (o registo físico) é o Root.
    - Juntou-se o FlightRoute e o ScheduledFlight no Flight Route & Schedule Aggregate, onde o ScheduledFlight (a execução do voo) assume o papel de Root.

3. **Histórico da Rota e tempos reais:** Adicionou-se os atributos - realDeparture : LocalDateTime e - realArrival : LocalDateTime à classe ScheduledFlight para monitorizar a operação real face ao tempo estimado. Para cumprir a regra de guardar a informação e permitir "voltar atrás" (histórico), adicionou-se o Value Object RouteHistory como uma lista dentro de FlightRoute.
