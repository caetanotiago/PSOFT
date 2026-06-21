# Guião de Demonstração — Swagger UI (Airports, WP#2A + WP#2B)

Guião passo-a-passo para demonstrar **ao vivo**, no Swagger UI, todos os endpoints
relacionados com aeroportos: os já existentes da WP#2A (US106-109) e os novos da
WP#2B (US207-211), incluindo o endpoint de rotas por aeroporto (US209, que vive no
módulo de rotas mas é claramente "sobre aeroportos").

## Pré-requisitos

1. Arrancar a aplicação:
   ```
   cd aisafe
   ./mvnw spring-boot:run -Dmaven.test.skip=true
   ```
2. Esperar por `Tomcat started on port 8080` e `Bootstrapping concluído com sucesso!` no log.
3. Abrir `http://localhost:8080/swagger-ui.html` no browser.

Ao arrancar, o `Bootstrapper` já cria 4 aeroportos e 5 utilizadores — não é preciso
criar nada à mão para começar a demonstração.

### Aeroportos já existentes (seed)

| IATA | Nome | Cidade | País | Região | Estado |
|---|---|---|---|---|---|
| `LIS` | Humberto Delgado | Lisboa | Portugal | Europa | OPERATIONAL |
| `OPO` | Francisco Sá Carneiro | Porto | Portugal | Europa | OPERATIONAL |
| `FAO` | Faro | Faro | Portugal | Europa | OPERATIONAL |
| `MAD` | Adolfo Suárez Madrid-Barajas | Madrid | Espanha | Europa | OPERATIONAL |

### Utilizadores de teste (seed)

| Username | Password | Role |
|---|---|---|
| `backoffice` | `bo123` | BACKOFFICE_OPERATOR (escrita + leitura em `/airports`) |
| `atcc` | `atcc123` | ATCC (leitura em `/airports` e `/api/routes`) |

---

## Passo 0 — Autenticação (repetir sempre que o token expirar ou trocar de utilizador)

1. Expandir `POST /auth/login`, **Try it out**, body:
   ```json
   { "username": "backoffice", "password": "bo123" }
   ```
   **Execute** → `200`, copiar o valor do campo `"token"` da resposta.
2. Clicar no botão **Authorize** (cadeado no topo da página, não o de cada endpoint).
3. Colar **só o token** (sem `Bearer `) e clicar **Authorize** → **Close**.
4. A partir daqui, todos os pedidos seguem autenticados como `backoffice`.

Para demonstrar um caso de acesso negado (403), repetir o login com `atcc`/`atcc123`
e tentar um endpoint só de `BACKOFFICE_OPERATOR` (ex: `PATCH /airports/{iataCode}/status`).

---

## WP#2A — Endpoints já existentes

### 1. `POST /airports` — Registar aeroporto (US106)
**Role:** `backoffice`

Body:
```json
{
  "iataCode": "BCN",
  "name": "Josep Tarradellas Barcelona-El Prat",
  "city": "Barcelona",
  "country": "Espanha",
  "region": "Europa",
  "timezone": "Europe/Madrid",
  "latitude": 41.2974,
  "longitude": 2.0833,
  "runways": [
    { "name": "07L/25R", "length": 3352.0, "orientation": "070/250" }
  ],
  "facilities": [
    { "type": "lounge", "identifier": "VIP-T1", "description": "Sala VIP do Terminal 1" }
  ],
  "photos": [
    { "url": "https://example.com/bcn.jpg", "caption": "Vista aérea" }
  ]
}
```
**Esperado:** `201 Created`, header `Location` com o novo recurso, corpo com `_links.self`.

**Teste negativo:** repetir o mesmo pedido outra vez → `409 Conflict` (IATA code duplicado).

### 2. `GET /airports/{iataCode}` — Ver detalhes (US107)
**Role:** `backoffice` ou `atcc`

Path: `iataCode = LIS`. **Esperado:** `200`, corpo com `_links.self`, `_links.update-status`, `_links.add-certification`.

**Teste negativo:** `iataCode = ZZZ` → `404 Not Found`.

### 3. `GET /airports` — Procurar (US108)
**Role:** `backoffice` ou `atcc`

Query: `city = Lisboa`. **Esperado:** `200` com lista contendo `LIS`.

**Teste negativo:** `city = Marte` → `200` com lista **vazia** (nunca 404 — é o contrato documentado no Swagger).

### 4. `POST /airports/{iataCode}/certifications` — Certificar modelo (US106a)
**Role:** `backoffice`

Path: `iataCode = LIS`. Body:
```json
{ "manufacturer": "Boeing", "modelName": "737-800" }
```
**Esperado:** `201`/`200` com o modelo certificado na resposta.

**Teste negativo:** repetir o mesmo pedido → `409 Conflict` (modelo já certificado).

### 5. `PATCH /airports/{iataCode}/status` — Atualizar estado (US109)
**Role:** `backoffice`

Path: `iataCode = OPO`. Body:
```json
{ "state": "UNDER_MAINTENANCE" }
```
**Esperado:** `200`, `status` no corpo passa a `UNDER_MAINTENANCE`.

**Testes negativos:**
- Body `{ "state": "A_VOAR" }` → `400 Bad Request` (estado inválido).
- Repetir a mesma transição (`UNDER_MAINTENANCE` → `UNDER_MAINTENANCE`) → `409 Conflict`.
- Mesmo pedido autenticado como `atcc` → `403 Forbidden`.

Voltar a pôr o `OPO` em `OPERATIONAL` no fim, para não afetar as demonstrações seguintes
(estatísticas/rotas assumem aeroportos operacionais).

---

## WP#2B — Endpoints novos

### 6. `POST /airports/{iataCode}/facilities` — Adicionar facilidade (US207)
**Role:** `backoffice`

Path: `iataCode = LIS`. Body:
```json
{ "type": "parking", "identifier": "P3", "description": "Parque de estacionamento coberto" }
```
**Esperado:** `200`, a facilidade aparece na lista de facilidades do aeroporto.

**Teste negativo:** repetir exatamente o mesmo `type` + `identifier` → `409 Conflict`
(`DuplicateFacilityException`).

### 7. `POST /airports/{iataCode}/photos` — Adicionar foto (US207)
**Role:** `backoffice`

Path: `iataCode = LIS`. Body:
```json
{ "url": "https://example.com/lis-terminal1.jpg", "caption": "Terminal 1" }
```
**Esperado:** `200`, a foto aparece na lista de fotos do aeroporto.

**Teste negativo:** `iataCode = ZZZ` → `404 Not Found`.

### 8. `PATCH /airports/{iataCode}/details` — Atualizar horário/contactos (US208)
**Role:** `backoffice`

Path: `iataCode = FAO`. Body (exemplo com os dois campos, mas cada um é opcional —
basta um estar presente):
```json
{
  "operatingHours": { "operates24Hours": false, "opens": "06:00:00", "closes": "23:30:00" },
  "contacts": [
    { "type": "phone", "value": "+351 289 800 800", "description": "Linha geral" },
    { "type": "email", "value": "info@aeroportofaro.pt" }
  ]
}
```
**Esperado:** `200`, corpo com `operatingHours` e `contacts` atualizados.

**Testes negativos:**
- Body `{}` (sem `operatingHours` nem `contacts`) → `400 Bad Request`.
- `operatingHours` com `opens` depois de `closes` (ex: `opens: "23:00:00"`, `closes: "06:00:00"`,
  `operates24Hours: false`) → `400 Bad Request`.

### 9. `GET /airports/statistics/busiest` — Aeroportos mais movimentados (US210)
**Role:** `backoffice`

Query opcional: `limit = 2`. **Esperado:** `200`, lista ordenada por `routeCount`
descendente (sem `limit`, devolve todos).

**Teste negativo:** `limit = -1` ou `limit = abc` → `400 Bad Request`.

### 10. `GET /airports/grouped` — Agrupar por região/país (US211)
**Role:** `backoffice` ou `atcc`

Query: `by = country`. **Esperado:** `200`, lista de grupos (`groupKey` = nome do país,
`airports` = lista de aeroportos desse país, ex: `LIS`/`OPO`/`FAO` em "Portugal", `MAD`
em "Espanha").

Repetir com `by = region` → todos agrupados em `"Europa"`.

**Teste negativo:** `by = planeta` → `400 Bad Request`.

---

## US209 (módulo de rotas, mas "sobre aeroportos")

### 11. `GET /api/routes/by-airport/{iataCode}` — Rotas por aeroporto
**Role:** `atcc` (este endpoint exige sempre `ATCC`, mesmo estando autenticado como
`backoffice` vai dar `403` — bom exemplo para mostrar a separação de papéis entre módulos)

Fazer login como `atcc`/`atcc123` (Passo 0) antes deste teste.

Path: `iataCode = LIS`. **Esperado:** `200` com a lista de rotas que partem de ou
chegam a `LIS` (origem OU destino).

**Teste negativo:** `iataCode = ZZZ` → `404 Not Found` (aeroporto inexistente).

---

## Checklist rápido para a demonstração ao vivo

1. Login `backoffice` → Authorize.
2. `POST /airports` (BCN) → 201, depois repetir → 409.
3. `GET /airports/LIS` → 200; `GET /airports/ZZZ` → 404.
4. `GET /airports?city=Lisboa` → 200 com resultados; `?city=Marte` → 200 vazio.
5. `POST /airports/LIS/certifications` → 200/201, repetir → 409.
6. `PATCH /airports/OPO/status` → 200, estado invalido → 400, repetir mesmo estado → 409.
7. `POST /airports/LIS/facilities` → 200, repetir igual → 409.
8. `POST /airports/LIS/photos` → 200.
9. `PATCH /airports/FAO/details` → 200; body vazio → 400.
10. `GET /airports/statistics/busiest?limit=2` → 200.
11. `GET /airports/grouped?by=country` → 200; `by=planeta` → 400.
12. Login `atcc` → Authorize.
13. `GET /api/routes/by-airport/LIS` → 200.
14. `PATCH /airports/OPO/status` como `atcc` → 403 (mostra controlo de acesso por papel).

## Notas de troubleshooting

- **401 Unauthorized** em qualquer pedido → o token expirou ou não fizeste o passo 0
  (Authorize). Repetir o login e o Authorize.
- **403 Forbidden** → autenticado, mas com o papel errado para aquele endpoint (ver
  tabela de roles em cada secção acima).
- Se o `OPO` ficar `UNDER_MAINTENANCE` ou `CLOSED` de uma demonstração anterior, repor
  para `OPERATIONAL` antes de testar `statistics/busiest` ou `grouped`, para os números
  não surpreenderem durante a apresentação.
