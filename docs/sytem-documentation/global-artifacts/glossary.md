# **Terms, Expressions and Acronyms (TEA)**

| **_TEA_** (EN) | **_TEA_** (PT) | **_Description_** (EN) |
|:---|:---|:---|
| **Aggregate** | **Agregado** | A cluster of domain objects (Entity + Value Objects) treated as a single unit with one Aggregate Root. |
| **Aggregate Root** | **Raiz de Agregado** | The single Entity within an Aggregate that serves as the entry point for all external references. |
| **Aircraft** | **Aeronave** | A specific physical aircraft registered in the system, identified by its Registration Number. |
| **Aircraft Model** | **Modelo de Aeronave** | A generic aircraft design specification (manufacturer + model name) shared by multiple aircraft instances. |
| **Aircraft Status** | **Estado da Aeronave** | Operational state of an aircraft: Active, Inactive, Under Maintenance, or In-flight. |
| **Airport** | **Aeroporto** | A certified facility where flights originate or land, identified by its IATA Code. |
| **Airport Certification** | **Certificação de Aeroporto** | The recorded authorisation for a specific Aircraft Model to operate at an Airport. Stored as a set of `ModelDesignation` value objects within the Airport aggregate. |
| **Airport Contact** | **Contacto do Aeroporto** | A communication method for an airport, including type (e.g., Phone, Email, Fax), value, and description. |
| **Airport Details** | **Detalhes do Aeroporto** | Descriptive information about an airport: name, city, country, region, and timezone. |
| **Airport Status** | **Estado do Aeroporto** | Operational state of an airport: Operational, Closed, or Under Maintenance. |
| **API** | **API** | Application Programming Interface used for communication between system components. |
| **ATCC** | **ATCC** | Air Transport Company Collaborator — a system role with read access to airport and route data. Authorised for US107 (view airport details), US108 (search airports), and US106a (add certification). |
| **Backoffice Operator** | **Operador de Backoffice** | A system role responsible for administrative operations: registering airports (US106), adding certifications (US106a), and updating airport status (US109). |
| **Checklist** | **Lista de Verificação** | Set of maintenance tasks or verification steps defined within a Maintenance Template. |
| **Completion Notes** | **Notas de Conclusão** | Optional remarks recorded at the end of a maintenance activity, detailing outcomes or observations. |
| **Coordinates** | **Coordenadas** | Geographic location (latitude and longitude) of an airport. |
| **Entity** | **Entidade** | A domain object with a unique identity that persists over time, regardless of attribute changes. |
| **Estimated Flight Time** | **Tempo de Voo Estimado** | Expected duration of travel along a Flight Route, from departure to arrival. |
| **Facility** | **Instalação** | Structured data representing specific airport infrastructures like Terminals, Gates, or Services. |
| **Feature** | **Característica** | Specific attribute or capability of an Aircraft Model (e.g., WiFi-enabled, specific engine type). |
| **Fleet** | **Frota** | The complete set of all airplanes managed by an Air Transport Company (ATC). |
| **Flight Route** | **Rota de Voo** | A defined path between two airports (origin and destination), with distance and operational requirements. |
| **Flight Schedule** | **Horário de Voo** | The specific date and time at which a Scheduled Flight is planned to operate. |
| **Flight Status** | **Estado do Voo** | The lifecycle state of a Scheduled Flight: Scheduled, Delayed, In-flight, Completed, or Canceled. |
| **IATA** | **IATA** | International Air Transport Association; the body that defines the 3-letter airport identification codes. |
| **IATA Code** | **Código IATA** | A 3-letter identifier assigned by IATA that uniquely identifies an airport (e.g., LIS, OPO). |
| **Maintenance Component** | **Componente de Manutenção** | The aircraft subsystem targeted by a maintenance activity, categorised as Engine, Airframe, etc. |
| **Maintenance Interval** | **Intervalo de Manutenção** | The defined thresholds (flight hours or calendar days) in a template that trigger scheduled maintenance alerts. |
| **Maintenance Part** | **Peça de Manutenção** | An inventory item used during aircraft maintenance, managed by the Maintenance Supervisor. |
| **Maintenance Record** | **Registo de Manutenção** | A record documenting a maintenance activity performed on a specific aircraft, based on a template. |
| **Maintenance Template** | **Modelo de Manutenção** | A reusable template defining the type, checklist, and scope of a maintenance activity for an aircraft model. |
| **Manufacturing Date** | **Data de Fabrico** | The date on which a specific aircraft was manufactured. |
| **Model Designation** | **Designação do Modelo** | The manufacturer and commercial model name that identifies an Aircraft Model (e.g., Boeing 737). Used as a cross-aggregate identity reference for Airport Certifications. |
| **Model Specifications** | **Especificações do Modelo** | Technical parameters of an Aircraft Model: base seating capacity, fuel capacity, maximum range, cruising speed. |
| **Network** | **Rede** | The comprehensive set of active flight routes operated by the company. |
| **Part Number** | **Número da Peça** | The unique identifier for a specific Maintenance Part in the inventory. |
| **Record Details** | **Detalhes do Registo** | Core information of a Maintenance Record: description, start date, and expected duration. |
| **Region** | **Região** | A broad geographical zone (e.g., Europe, North America) used to group airports, larger than a country. |
| **Registration Number** | **Número de Registo** | A unique alphanumeric code that identifies a specific aircraft (e.g., CS-TKY). |
| **Route Distance** | **Distância da Rota** | The total fixed distance between the origin and destination airports of a route. |
| **Route ID** | **ID da Rota** | Unique system-generated identifier assigned to a Flight Route. |
| **Route Requirements** | **Requisitos da Rota** | Minimum aircraft capabilities required to operate a route: minimum range and minimum seating capacity. |
| **Runway** | **Pista** | A designated landing and take-off strip at an airport, characterised by name, length, and orientation. |
| **Scheduled Flight** | **Voo Agendado** | A planned flight instance combining a Flight Route, an Aircraft, and a specific Flight Schedule. |
| **Seating Capacity** | **Capacidade de Lugares** | The specific number of passenger seats for an individual Aircraft instance, which may vary from the base model. |
| **Stock Level** | **Nível de Stock** | The current quantity of a Maintenance Part in inventory and its minimum threshold for low-stock alerts. |
| **Template Name** | **Nome do Modelo** | Unique name that identifies a Maintenance Template within the system. |
| **Template Type** | **Tipo de Modelo de Manutenção** | Category of maintenance activity defined in a template: Inspection, Overhaul, etc. |
| **Value Object** | **Objeto de Valor** | An immutable domain object defined solely by its attributes, with no identity of its own. |
