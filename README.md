# Decentralized Academic Certificate Verification System (MVP)

Spring Boot backend that issues tamper-evident academic certificates using a simple blockchain-like append-only log.

## Run

```bash
./mvnw spring-boot:run  # or mvn spring-boot:run
```

Defaults:

- Port: `8080`
- Institution API key: `INSTITUTION_SECRET`
- Employer API key: `EMPLOYER_SECRET`
- Issuer HMAC secret: `DEMO_ISSUER_SECRET`



## Endpoints

### Issue Certificate (Institution only)

`POST /api/certs/issue`
Header: `X-API-KEY: INSTITUTION_SECRET`  
Body:

```json
{
  "studentName": "Alice",
  "institution": "SUST",
  "program": "CSE",
  "year": "2025",
  "gpa": 3.92
}
```

Response includes `certificateId`, `issuerSignature`, `blockHash`.

### Verify Certificate (Employer or Institution)

`POST /api/verify`
Header: `X-API-KEY: EMPLOYER_SECRET`  
Body:

```json
{
  "certificateId": "<from issue>",
  "issuerSignature": "<from issue>",
  "blockHash": "<from issue>"
}
```

Response:

```json
{ "valid": true }
```

### Inspect Chain

`GET /api/chain`

## Notes

- This MVP uses HMAC for signatures and stores the chain in `chain.json` (local file). I will implement Postgress later.
- For production: replace HMAC with asymmetric keys (RSA/ECDSA), add persistence, audit logs, rate limiting, and proper role-based auth.
