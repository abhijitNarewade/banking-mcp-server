# Contributing

Thanks for considering a contribution to `banking-mcp-server`.

## Local Checks

```bash
mvn verify
docker compose up --build
```

## Pull Request Guidelines

- Keep mock banking data deterministic and clearly non-production.
- Add tests for every new tool, validation rule, and REST endpoint.
- Avoid real customer data, real credentials, and production bank integrations.
- Document new tools in `docs/mcp-tools.md` and expose them through the REST catalog.
