package io.github.whoisalphahelix.helius.netty;

import com.google.gson.JsonElement;

public interface RequestProcessor {
	JsonElement getProcessedData();
}
