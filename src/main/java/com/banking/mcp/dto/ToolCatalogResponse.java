package com.banking.mcp.dto;

import java.util.List;
import java.util.Map;

public record ToolCatalogResponse(List<Map<String, Object>> tools) {
}
